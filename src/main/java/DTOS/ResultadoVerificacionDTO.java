package DTOS;

/**
 * Resultado de la verificación previa del CU14.
 * No lleva texto: lleva el CÓDIGO de lo que pasó y los datos del responsable.
 * El cartel ("Los datos de razón social, CUIT serán eliminados del sistema")
 * lo arma la capa de presentación con esos datos.
 */
public class ResultadoVerificacionDTO {
    private final boolean puedeEliminarse;
    private final CodigoResultado codigo;
    private final ResponsableDTO responsable;

    public ResultadoVerificacionDTO(boolean puedeEliminarse, CodigoResultado codigo, ResponsableDTO responsable) {
        this.puedeEliminarse = puedeEliminarse; this.codigo = codigo; this.responsable = responsable;
    }
    public boolean isPuedeEliminarse(){return puedeEliminarse;}
    public CodigoResultado getCodigo(){return codigo;}
    public ResponsableDTO getResponsable(){return responsable;}
}
