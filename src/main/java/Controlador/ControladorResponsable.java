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

public class ControladorResponsable {

    public ResultadoVerificacionDTO prepararBaja(Integer idResponsable) {
        IRepositorioResponsable repoResp = RepositorioFactory.getRepositorioResponsable();
        IRepositorioFacturas repoFact = RepositorioFactory.getRepositorioFacturas();

        ResponsableDePago res = repoResp.buscarPorId(idResponsable);
        if (res == null) {
            return new ResultadoVerificacionDTO(false, CodigoResultado.RESPONSABLE_NO_ENCONTRADO, null);
        }

        boolean tieneFacturas = repoFact.existeFactura(res.getIdResponsable());

        if (tieneFacturas) {
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

        res.darDeBaja();
        repoResp.actualizar(res);

        return new ResultadoBajaDTO(true, CodigoResultado.ELIMINADO);
    }

    public List<ResponsableDTO> buscar(String razonSocial, String cuit) {
        IRepositorioResponsable repoResp = RepositorioFactory.getRepositorioResponsable();
        List<ResponsableDTO> dtos = new ArrayList<>();
        for (ResponsableDePago r : repoResp.buscarPorCriterios(razonSocial, cuit)) {
            dtos.add(r.toDTO());
        }
        return dtos;
    }
}
