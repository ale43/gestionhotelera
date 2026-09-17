package DTOS;

public class ResultadoBajaDTO {
    private final boolean exito;
    private final CodigoResultado codigo;

    public ResultadoBajaDTO(boolean exito, CodigoResultado codigo){ this.exito = exito; this.codigo = codigo; }
    public boolean isExito(){return exito;}
    public CodigoResultado getCodigo(){return codigo;}
}
