package Controlador;

import DTOS.ResultadoBajaDTO;
import DTOS.ResultadoVerificacionDTO;
import Entidades.ResponsableDePago;
import Repositorios.IRepositorioFacturas;
import Repositorios.IRepositorioResponsable;
import Repositorios.RepositorioFactory;
import java.util.ArrayList;
import java.util.List;
import DTOS.ResponsableDTO;

/**
 * ControladorResponsable (GRASP Controller, STATELESS) — implementa el diagrama
 * de secuencia del CU14 al pie de la letra: prepararBaja() y confirmarEliminacion().
 * No guarda estado entre llamadas: en confirmarEliminacion vuelve a recuperar la
 * entidad desde el repositorio (ver nota del diagrama).
 */
public class ControladorResponsable {

    /**
     * CU14 — Conserje presiona "BORRAR".
     * 1) buscarPorId  2) existeFactura(res.getCuit())
     * 3) alt: si NO tiene facturas -> confirmación con res.toDTO(); si tiene -> error.
     */
    public ResultadoVerificacionDTO prepararBaja(Integer idResponsable) {
        IRepositorioResponsable repoResp = RepositorioFactory.getRepositorioResponsable();
        IRepositorioFacturas repoFact = RepositorioFactory.getRepositorioFacturas();

        ResponsableDePago res = repoResp.buscarPorId(idResponsable);
        if (res == null) {
            return new ResultadoVerificacionDTO(false, "No se encontro el responsable de pago indicado.", null);
        }

        boolean tieneFacturas = repoFact.existeFactura(res.getCuit());

        if (tieneFacturas) {   // alt tieneFacturas == true
            return new ResultadoVerificacionDTO(false,
                    "No se puede eliminar, tiene facturas asociadas.", res.toDTO());
        }
        // alt tieneFacturas == false -> mostrarConfirmacionEliminacion(res.toDTO())
        return new ResultadoVerificacionDTO(true,
                "Los datos de " + res.getDenominacion() + ", " + res.getCuit()
                + " seran eliminados del sistema.", res.toDTO());
    }

    /**
     * CU14 — Actor confirma "ELIMINAR".
     * Stateless: se recupera de nuevo la entidad, se hace la BAJA LÓGICA
     * (setEstado("ELIMINADO")) y se persiste con actualizar(res).
     */
    public ResultadoBajaDTO confirmarEliminacion(Integer idResponsable) {
        IRepositorioResponsable repoResp = RepositorioFactory.getRepositorioResponsable();

        ResponsableDePago res = repoResp.buscarPorId(idResponsable);   // re-fetch (controlador sin estado)
        if (res == null) {
            return new ResultadoBajaDTO(false, "No se encontro el responsable de pago indicado.");
        }

        res.setEstado("ELIMINADO");        // baja lógica
        repoResp.actualizar(res);

        return new ResultadoBajaDTO(true, "Eliminado.");
    }

    /** Soporte CU03 — listar/buscar responsables ACTIVOS para que la UI muestre la grilla. */
    public List<ResponsableDTO> buscar(String razonSocial, String cuit) {
        IRepositorioResponsable repoResp = RepositorioFactory.getRepositorioResponsable();
        List<ResponsableDTO> dtos = new ArrayList<>();
        for (ResponsableDePago r : repoResp.buscarPorCriterios(razonSocial, cuit)) {
            dtos.add(r.toDTO());
        }
        return dtos;
    }
}
