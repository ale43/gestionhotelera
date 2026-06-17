package Entidades;

import java.math.BigDecimal;

public class Pago {
    private Integer idPago;
    private BigDecimal monto;
    private Factura factura;

    public Pago() { }

    public Integer getIdPago() { return idPago; }
    public void setIdPago(Integer idPago) { this.idPago = idPago; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }
}
