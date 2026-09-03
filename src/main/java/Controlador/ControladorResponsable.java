package Controlador;

import DTOS.CodigoResultado;
import DTOS.ResponsableDTO;
import DTOS.ResultadoBajaDTO;
import DTOS.ResultadoVerificacionDTO;
import Entidades.ResponsableDePago;
import Repositorios.IRepositorioFacturas;
import Repositorios.IRepositorioResponsable;
import Repositorios.RepositorioFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador del CU03 (buscar) y del CU14 (dar de baja).
 *
 * No arma ningún texto de pantalla: devuelve un CodigoResultado y los datos.
 * Los carteles del enunciado los compone la capa de presentación.
 */
public class ControladorResponsable {

    public ResultadoVerificacionDTO prepararBaja(Integer idResponsable) {
        IRepositorioResponsable repoResp = RepositorioFactory.getRepositorioResponsable();
        IRepositorioFacturas repoFact = RepositorioFactory.getRepositorioFacturas();

        ResponsableDePago res = repoResp.buscarPorId(idResponsable);
        if (res == null) {
            return new ResultadoVerificacionDTO(false, CodigoResultado.RESPONSABLE_NO_ENCONTRADO, null);
        }

        boolean tieneFacturas = repoFact.existeFactura(res.getCuit());

        if (tieneFacturas) {   // alt tieneFacturas == true
            return new ResultadoVerificacionDTO(false, CodigoResultado.TIENE_FACTURAS, res.toDTO());
        }
        return new ResultadoVerificacionDTO(true, CodigoResultado.PUEDE_ELIMINARSE, res.toDTO());
    }

    public ResultadoBajaDTO confirmarEliminacion(Integer idResponsable) {
        IRepositorioResponsable repoResp = RepositorioFactory.getRepositorioResponsable();

        ResponsableDePago res = repoResp.buscarPorId(idResponsable);
        if (res == null) {
            return new ResultadoBajaDTO(false, CodigoResultado.RESPONSABLE_NO_ENCONTRADO);
        }

        res.setEstado("ELIMINADO");
        repoResp.actualizar(res);

        return new ResultadoBajaDTO(true, CodigoResultado.ELIMINADO);
    }

    /** CU03 — Buscar Responsable de Pago por razón social y/o CUIT. */
    public List<ResponsableDTO> buscar(String razonSocial, String cuit) {
        IRepositorioResponsable repoResp = RepositorioFactory.getRepositorioResponsable();
        List<ResponsableDTO> dtos = new ArrayList<>();
        for (ResponsableDePago r : repoResp.buscarPorCriterios(razonSocial, cuit)) {
            dtos.add(r.toDTO());
        }
        return dtos;
    }
}
