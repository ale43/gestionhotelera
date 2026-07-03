package Infraestructura;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import Controlador.ControladorResponsable;
import Controlador.ControladorSesion;
import DTOS.ConserjeDTO;
import DTOS.ResponsableDTO;
import DTOS.ResultadoBajaDTO;
import DTOS.ResultadoVerificacionDTO;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servidor REST con el HttpServer del JDK. Traduce HTTP -> Controladores.
 *   POST /api/login       (usuario=&clave=)   -> CU01 autenticar (tabla conserje)
 *   GET  /api/conserjes                        -> listar usuarios
 *   POST /api/conserjes    (usuario=&clave=)   -> alta de usuario
 *   POST /api/conserjes/eliminar (usuario=)    -> baja de usuario
 *   GET  /api/responsables?razonSocial=&cuit=  -> buscar (CU03)
 *   GET  /api/responsables/{id}/preparar       -> prepararBaja(id)
 *   POST /api/responsables/{id}/eliminar       -> confirmarEliminacion(id)  (baja logica)
 */
public class ServidorRest {

    private static final String BASE = "/api/responsables";
    private static final String BASE_CONSERJES = "/api/conserjes";
    private final int puerto;
    private final ControladorResponsable controlador = new ControladorResponsable();
    private final ControladorSesion controladorSesion = new ControladorSesion();

    public ServidorRest(int puerto) { this.puerto = puerto; }

    public void iniciar() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(puerto), 0);
        server.createContext(BASE, this::manejarResponsables);
        server.createContext(BASE_CONSERJES, this::manejarConserjes);
        server.createContext("/api/login", this::manejarLogin);
        server.createContext("/", this::servirFrontend);
        server.setExecutor(null);
        server.start();
        System.out.println("Servidor REST iniciado en http://localhost:" + puerto + "/");
    }

    /** CU01 - Autenticar Usuario contra la tabla `conserje` (PostgreSQL). */
    private void manejarLogin(HttpExchange ex) throws IOException {
        agregarCors(ex);
        String metodo = ex.getRequestMethod();
        if ("OPTIONS".equalsIgnoreCase(metodo)) { responder(ex, 204, ""); return; }
        if (!"POST".equalsIgnoreCase(metodo)) {
            responderJson(ex, 405, "{\"mensaje\":\"Metodo no permitido\"}");
            return;
        }
        try {
            Map<String, String> datos = parsearQuery(leerCuerpo(ex.getRequestBody()));
            String usuario = datos.get("usuario");
            String clave = datos.get("clave");

            boolean ok = controladorSesion.autenticar(usuario, clave);
            if (ok) {
                responderJson(ex, 200, "{\"exito\":true,\"nombre\":\""
                        + Json.escapar(usuario.trim().toUpperCase()) + "\"}");
            } else {
                // Mensaje EXACTO del CU01 del enunciado
                responderJson(ex, 200, "{\"exito\":false,"
                        + "\"mensaje\":\"El usuario o la contrase\u00f1a no son v\u00e1lidos\"}");
            }
        } catch (Exception e) {
            responderJson(ex, 500, "{\"exito\":false,\"mensaje\":\""
                    + Json.escapar(String.valueOf(e.getMessage())) + "\"}");
        }
    }

    /** Administracion de usuarios (tabla conserje): listar, alta, baja. */
    private void manejarConserjes(HttpExchange ex) throws IOException {
        agregarCors(ex);
        String metodo = ex.getRequestMethod();
        if ("OPTIONS".equalsIgnoreCase(metodo)) { responder(ex, 204, ""); return; }

        String resto = ex.getRequestURI().getPath().substring(BASE_CONSERJES.length());
        if (resto.startsWith("/")) resto = resto.substring(1);

        try {
            // GET /api/conserjes -> listar
            if (resto.isEmpty() && "GET".equalsIgnoreCase(metodo)) {
                List<ConserjeDTO> lista = controladorSesion.listarUsuarios();
                responderJson(ex, 200, Json.deListaConserjes(lista));
                return;
            }
            // POST /api/conserjes -> alta (body: usuario=&clave=)
            if (resto.isEmpty() && "POST".equalsIgnoreCase(metodo)) {
                Map<String, String> datos = parsearQuery(leerCuerpo(ex.getRequestBody()));
                ResultadoBajaDTO r = controladorSesion.crearUsuario(datos.get("usuario"), datos.get("clave"));
                responderJson(ex, 200, Json.de(r));
                return;
            }
            // POST /api/conserjes/eliminar -> baja (body: usuario=)
            if ("eliminar".equals(resto) && "POST".equalsIgnoreCase(metodo)) {
                Map<String, String> datos = parsearQuery(leerCuerpo(ex.getRequestBody()));
                ResultadoBajaDTO r = controladorSesion.eliminarUsuario(datos.get("usuario"));
                responderJson(ex, 200, Json.de(r));
                return;
            }
            responderJson(ex, 404, "{\"mensaje\":\"Recurso no encontrado\"}");
        } catch (Exception e) {
            responderJson(ex, 500, "{\"exito\":false,\"mensaje\":\""
                    + Json.escapar(String.valueOf(e.getMessage())) + "\"}");
        }
    }

    private void manejarResponsables(HttpExchange ex) throws IOException {
        agregarCors(ex);
        String metodo = ex.getRequestMethod();
        if ("OPTIONS".equalsIgnoreCase(metodo)) { responder(ex, 204, ""); return; }

        String resto = ex.getRequestURI().getPath().substring(BASE.length());
        if (resto.startsWith("/")) resto = resto.substring(1);
        String[] seg = resto.isEmpty() ? new String[0] : resto.split("/");

        try {
            if (seg.length == 0 && "GET".equalsIgnoreCase(metodo)) {
                Map<String,String> q = parsearQuery(ex.getRequestURI().getRawQuery());
                List<ResponsableDTO> lista = controlador.buscar(q.get("razonSocial"), q.get("cuit"));
                responderJson(ex, 200, Json.deLista(lista));
                return;
            }
            if (seg.length == 2 && "GET".equalsIgnoreCase(metodo) && "preparar".equals(seg[1])) {
                Integer id = parsearId(seg[0]);
                if (id == null) { responderJson(ex, 400, "{\"mensaje\":\"Id invalido\"}"); return; }
                ResultadoVerificacionDTO v = controlador.prepararBaja(id);
                responderJson(ex, 200, Json.de(v));
                return;
            }
            if (seg.length == 2 && "POST".equalsIgnoreCase(metodo) && "eliminar".equals(seg[1])) {
                Integer id = parsearId(seg[0]);
                if (id == null) { responderJson(ex, 400, "{\"mensaje\":\"Id invalido\"}"); return; }
                ResultadoBajaDTO r = controlador.confirmarEliminacion(id);
                responderJson(ex, 200, Json.de(r));
                return;
            }
            responderJson(ex, 404, "{\"mensaje\":\"Recurso no encontrado\"}");
        } catch (Exception e) {
            responderJson(ex, 500, "{\"mensaje\":\"" + Json.escapar(String.valueOf(e.getMessage())) + "\"}");
        }
    }

    private void servirFrontend(HttpExchange ex) throws IOException {
        agregarCors(ex);
        if ("OPTIONS".equalsIgnoreCase(ex.getRequestMethod())) { responder(ex, 204, ""); return; }
        String path = ex.getRequestURI().getPath();
        if (path.equals("/") || path.equals("/index.html")) {
            ex.getResponseHeaders().set("Content-Type","text/html; charset=utf-8");
            responder(ex, 200, leerFrontend());
        } else responder(ex, 404, "No encontrado");
    }

    private String leerFrontend() {
        String[] cand = {"frontend/index.html","./frontend/index.html","../frontend/index.html","index.html"};
        for (String c : cand) {
            try { Path p = Paths.get(c); if (Files.exists(p)) return new String(Files.readAllBytes(p), StandardCharsets.UTF_8); }
            catch (Exception ignored) { }
        }
        return "<!doctype html><meta charset='utf-8'><h1>Falta frontend/index.html</h1>";
    }

    private String leerCuerpo(InputStream is) throws IOException {
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    private void agregarCors(HttpExchange ex) {
        Headers h = ex.getResponseHeaders();
        h.set("Access-Control-Allow-Origin", "*");
        h.set("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
        h.set("Access-Control-Allow-Headers", "Content-Type");
    }

    private Integer parsearId(String s) {
        try { return Integer.valueOf(URLDecoder.decode(s, StandardCharsets.UTF_8).trim()); }
        catch (NumberFormatException e) { return null; }
    }

    private Map<String,String> parsearQuery(String raw) {
        Map<String,String> m = new HashMap<>();
        if (raw == null) return m;
        for (String par : raw.split("&")) {
            if (par.isEmpty()) continue;
            int i = par.indexOf('=');
            String k = (i<0)? par : par.substring(0,i);
            String v = (i<0)? "" : par.substring(i+1);
            k = URLDecoder.decode(k, StandardCharsets.UTF_8);
            v = URLDecoder.decode(v, StandardCharsets.UTF_8);
            m.put(k, v);
        }
        return m;
    }

    private void responderJson(HttpExchange ex, int codigo, String cuerpo) throws IOException {
        ex.getResponseHeaders().set("Content-Type","application/json; charset=utf-8");
        responder(ex, codigo, cuerpo);
    }

    private void responder(HttpExchange ex, int codigo, String cuerpo) throws IOException {
        byte[] bytes = cuerpo.getBytes(StandardCharsets.UTF_8);
        if (bytes.length == 0) { ex.sendResponseHeaders(codigo, -1); ex.close(); }
        else { ex.sendResponseHeaders(codigo, bytes.length); try (OutputStream os = ex.getResponseBody()) { os.write(bytes); } }
    }
}
