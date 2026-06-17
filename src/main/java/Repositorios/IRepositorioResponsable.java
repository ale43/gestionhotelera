package Repositorios;

import Entidades.ResponsableDePago;
import java.util.List;

public interface IRepositorioResponsable {
    ResponsableDePago buscarPorId(Integer idResponsable);
    void actualizar(ResponsableDePago responsable);
    List<ResponsableDePago> buscarPorCriterios(String razonSocial, String cuit);
}