package DTOS;

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
