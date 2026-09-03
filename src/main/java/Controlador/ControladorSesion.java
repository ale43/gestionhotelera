package Controlador;

import DTOS.CodigoResultado;
import DTOS.ConserjeDTO;
import DTOS.ResultadoBajaDTO;
import Repositorios.IRepositorioConserje;
import Repositorios.RepositorioFactory;
import java.util.List;

/**
 * CU01 (autenticar) y administración de conserjes.
 * Devuelve códigos, nunca leyendas de pantalla: los textos los arma la vista.
 */
public class ControladorSesion {

    public boolean autenticar(String nombre, String password) {
        if (nombre == null || nombre.isBlank() || password == null || password.isBlank()) {
            return false;
        }
        IRepositorioConserje repo = RepositorioFactory.getRepositorioConserje();
        return repo.autenticar(nombre.trim(), password);
    }

    public List<ConserjeDTO> listarUsuarios() {
        return RepositorioFactory.getRepositorioConserje().listar();
    }

    public ResultadoBajaDTO crearUsuario(String nombre, String password) {
        IRepositorioConserje repo = RepositorioFactory.getRepositorioConserje();

        if (nombre == null || nombre.isBlank() || password == null || password.isBlank()) {
            return new ResultadoBajaDTO(false, CodigoResultado.DATOS_INCOMPLETOS);
        }
        String n = nombre.trim();
        if (!n.matches("[a-zA-Z0-9]{3,30}")) {
            return new ResultadoBajaDTO(false, CodigoResultado.NOMBRE_USUARIO_INVALIDO);
        }
        CodigoResultado errorClave = validarPassword(password);
        if (errorClave != null) {
            return new ResultadoBajaDTO(false, errorClave);
        }
        if (repo.existeNombre(n)) {
            return new ResultadoBajaDTO(false, CodigoResultado.USUARIO_YA_EXISTE);
        }
        boolean ok = repo.crear(n, password);
        if (!ok) return new ResultadoBajaDTO(false, CodigoResultado.ERROR_INESPERADO);
        return new ResultadoBajaDTO(true, CodigoResultado.USUARIO_CREADO);
    }

    public ResultadoBajaDTO eliminarUsuario(String nombre) {
        IRepositorioConserje repo = RepositorioFactory.getRepositorioConserje();

        if (nombre == null || nombre.isBlank()) {
            return new ResultadoBajaDTO(false, CodigoResultado.DATOS_INCOMPLETOS);
        }
        if (!repo.existeNombre(nombre.trim())) {
            return new ResultadoBajaDTO(false, CodigoResultado.USUARIO_NO_EXISTE);
        }
        if (repo.contar() <= 1) {
            return new ResultadoBajaDTO(false, CodigoResultado.ULTIMO_USUARIO);
        }
        boolean ok = repo.eliminar(nombre.trim());
        if (!ok) return new ResultadoBajaDTO(false, CodigoResultado.ERROR_INESPERADO);
        return new ResultadoBajaDTO(true, CodigoResultado.USUARIO_ELIMINADO);
    }

    /** Regla de contraseña del CU01. Devuelve null si es válida. */
    private CodigoResultado validarPassword(String password) {
        int letras = 0;
        StringBuilder digitos = new StringBuilder();
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) letras++;
            else if (Character.isDigit(c)) digitos.append(c);
        }
        if (letras < 5 || digitos.length() < 3) {
            return CodigoResultado.PASSWORD_LONGITUD;
        }
        String d = digitos.toString();
        boolean todosIguales = true, creciente = true, decreciente = true;
        for (int i = 1; i < d.length(); i++) {
            if (d.charAt(i) != d.charAt(0)) todosIguales = false;
            if (d.charAt(i) - d.charAt(i - 1) != 1)  creciente = false;
            if (d.charAt(i) - d.charAt(i - 1) != -1) decreciente = false;
        }
        if (todosIguales || creciente || decreciente) {
            return CodigoResultado.PASSWORD_NUMEROS_CONSECUTIVOS;
        }
        return null;
    }
}
