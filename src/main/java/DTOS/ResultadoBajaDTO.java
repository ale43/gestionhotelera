package DTOS;

/**
 * Resultado de una operación de escritura (baja del CU14, alta/baja de usuarios).
 * Igual que ResultadoVerificacionDTO: código, no texto. La leyenda que ve el
 * conserje la resuelve la capa de presentación.
 */
public class ResultadoBajaDTO {
    private final boolean exito;
    private final CodigoResultado codigo;

    public ResultadoBajaDTO(boolean exito, CodigoResultado codigo){ this.exito = exito; this.codigo = codigo; }
    public boolean isExito(){return exito;}
    public CodigoResultado getCodigo(){return codigo;}
}
