package DTOS;

public class ResultadoBajaDTO {
    private final boolean exito;
    private final String mensaje;
    public ResultadoBajaDTO(boolean exito, String mensaje){ this.exito = exito; this.mensaje = mensaje; }
    public boolean isExito(){return exito;} public String getMensaje(){return mensaje;}
}
