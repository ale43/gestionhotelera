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

public class ControladorResponsable {

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
        return new ResultadoVerificacionDTO(true,
                "Los datos de " + res.getDenominacion() + ", " + res.getCuit()
                + " seran eliminados del sistema.", res.toDTO());
    }

    public ResultadoBajaDTO confirmarEliminacion(Integer idResponsable) {
        IRepositorioResponsable repoResp = RepositorioFactory.getRepositorioResponsable();

        ResponsableDePago res = repoResp.buscarPorId(idResponsable);   
        if (res == null) {
            return new ResultadoBajaDTO(false, "No se encontro el responsable de pago indicado.");
        }

        res.setEstado("ELIMINADO");      
        repoResp.actualizar(res);

        return new ResultadoBajaDTO(true, "Eliminado.");
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
