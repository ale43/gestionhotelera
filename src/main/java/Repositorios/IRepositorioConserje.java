package Repositorios;

import DTOS.ConserjeDTO;
import java.util.List;

public interface IRepositorioConserje {

    boolean autenticar(String nombre, String password);

    List<ConserjeDTO> listar();

    boolean existeNombre(String nombre);

    boolean crear(String nombre, String password);

    boolean eliminar(String nombre);

    int contar();
}
