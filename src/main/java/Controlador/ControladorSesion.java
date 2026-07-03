package Controlador;

import DTOS.ConserjeDTO;
import DTOS.ResultadoBajaDTO;
import Repositorios.IRepositorioConserje;
import Repositorios.RepositorioFactory;
import java.util.List;
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
            return new ResultadoBajaDTO(false, "Debe completar el nombre de usuario y la contraseña.");
        }
        String n = nombre.trim();
        if (!n.matches("[a-zA-Z0-9]{3,30}")) {
            return new ResultadoBajaDTO(false,
                    "El nombre de usuario debe tener entre 3 y 30 caracteres alfanumericos, sin espacios.");
        }
        String errorClave = validarPassword(password);
        if (errorClave != null) {
            return new ResultadoBajaDTO(false, errorClave);
        }
        if (repo.existeNombre(n)) {
            return new ResultadoBajaDTO(false, "¡CUIDADO! El nombre de usuario ya existe en el sistema.");
        }
        boolean ok = repo.crear(n, password);
        if (!ok) return new ResultadoBajaDTO(false, "No se pudo crear el usuario.");
        return new ResultadoBajaDTO(true,
                "El usuario " + n.toLowerCase() + " ha sido cargado al sistema.");
    }

    public ResultadoBajaDTO eliminarUsuario(String nombre) {
        IRepositorioConserje repo = RepositorioFactory.getRepositorioConserje();

        if (nombre == null || nombre.isBlank()) {
            return new ResultadoBajaDTO(false, "Debe indicar el usuario a eliminar.");
        }
        if (!repo.existeNombre(nombre.trim())) {
            return new ResultadoBajaDTO(false, "El usuario no existe en el sistema.");
        }
        if (repo.contar() <= 1) {
            return new ResultadoBajaDTO(false,
                    "No se puede eliminar: debe existir al menos un usuario en el sistema.");
        }
        boolean ok = repo.eliminar(nombre.trim());
        if (!ok) return new ResultadoBajaDTO(false, "No se pudo eliminar el usuario.");
        return new ResultadoBajaDTO(true,
                "El usuario " + nombre.trim().toLowerCase() + " ha sido eliminado del sistema.");
    }
    private String validarPassword(String password) {
        int letras = 0;
        StringBuilder digitos = new StringBuilder();
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) letras++;
            else if (Character.isDigit(c)) digitos.append(c);
        }
        if (letras < 5 || digitos.length() < 3) {
            return "La contraseña debe tener al menos 5 letras y 3 números.";
        }
        String d = digitos.toString();
        boolean todosIguales = true, creciente = true, decreciente = true;
        for (int i = 1; i < d.length(); i++) {
            if (d.charAt(i) != d.charAt(0)) todosIguales = false;
            if (d.charAt(i) - d.charAt(i - 1) != 1)  creciente = false;
            if (d.charAt(i) - d.charAt(i - 1) != -1) decreciente = false;
        }
        if (todosIguales || creciente || decreciente) {
            return "Los números de la contraseña no pueden ser iguales ni consecutivos "
                 + "en forma creciente o decreciente.";
        }
        return null;
    }
}
