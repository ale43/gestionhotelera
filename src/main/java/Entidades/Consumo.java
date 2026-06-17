package Entidades;

import java.math.BigDecimal;

public class Consumo {
    private Integer idConsumo;
    private BigDecimal monto;
    private TipoConsumo tipo;
    private Estadia estadia;

    public Consumo() { }

    public Integer getIdConsumo() { return idConsumo; }
    public void setIdConsumo(Integer idConsumo) { this.idConsumo = idConsumo; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public TipoConsumo getTipo() { return tipo; }
    public void setTipo(TipoConsumo tipo) { this.tipo = tipo; }
    public Estadia getEstadia() { return estadia; }
    public void setEstadia(Estadia estadia) { this.estadia = estadia; }
}
