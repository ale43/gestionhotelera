package DTOS;

public class ResultadoVerificacionDTO {
    private final boolean puedeEliminarse;
    private final String mensaje;
    private final ResponsableDTO responsable;

    public ResultadoVerificacionDTO(boolean puedeEliminarse, String mensaje, ResponsableDTO responsable) {
        this.puedeEliminarse = puedeEliminarse; this.mensaje = mensaje; this.responsable = responsable;
    }
    public boolean isPuedeEliminarse(){return puedeEliminarse;} public String getMensaje(){return mensaje;}
    public ResponsableDTO getResponsable(){return responsable;}
}
