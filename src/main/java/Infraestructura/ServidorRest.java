package Infraestructura;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import Controlador.ControladorResponsable;
import DTOS.ResponsableDTO;
import DTOS.ResultadoBajaDTO;
import DTOS.ResultadoVerificacionDTO;
import java.io.IOException;
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
 * Servidor REST con el HttpServer del JDK. Traduce HTTP -> ControladorResponsable.
 *   GET  /api/responsables?razonSocial=&cuit=    -> buscar (CU03)
 *   GET  /api/responsables/{id}/preparar          -> prepararBaja(id)
 *   POST /api/responsables/{id}/eliminar           -> confirmarEliminacion(id)  (baja lógica)
 */
public class ServidorRest {

    private static final String BASE = "/api/responsables";
    private final int puerto;
    private final ControladorResponsable controlador = new ControladorResponsable();

    public ServidorRest(int puerto) { this.puerto = puerto; }

    public void iniciar() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(puerto), 0);
        server.createContext(BASE, this::manejarResponsables);
        server.createContext("/", this::servirFrontend);
        server.setExecutor(null);
        server.start();
        System.out.println("Servidor REST iniciado en http://localhost:" + puerto + "/");
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
