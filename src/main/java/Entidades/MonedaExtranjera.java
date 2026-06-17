package Entidades;

public class MonedaExtranjera extends MedioDePago {
    private Integer idMonedaExtranjera;
    private TipoMoneda tipo;

    public MonedaExtranjera() { }
    public Integer getIdMonedaExtranjera() { return idMonedaExtranjera; }
    public void setIdMonedaExtranjera(Integer v) { this.idMonedaExtranjera = v; }
    public TipoMoneda getTipo() { return tipo; }
    public void setTipo(TipoMoneda tipo) { this.tipo = tipo; }
}
