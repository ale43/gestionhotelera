package Repositorios;

import Entidades.ResponsableDePago;
import java.util.List;

public interface IRepositorioResponsable {
    ResponsableDePago buscarPorId(Integer idResponsable);
    void actualizar(ResponsableDePago responsable);

    /**
     * CU03 — Buscar Responsable de Pago. Los criterios vacíos o nulos no filtran.
     *
     * @param razonSocial texto que debe CONTENER la razón social (jurídica)
     *                    o "apellido, nombre" (física)
     * @param cuit        CUIT por el que debe EMPEZAR (se ignoran guiones y puntos)
     */
    List<ResponsableDePago> buscarPorCriterios(String razonSocial, String cuit);
}
