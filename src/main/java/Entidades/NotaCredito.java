package Entidades;

import java.math.BigDecimal;

public class NotaCredito {
    private Integer idNotaCredito;
    private String descripcion;
    private BigDecimal monto;
    private Factura factura;

    public NotaCredito() { }

    public Integer getIdNotaCredito() { return idNotaCredito; }
    public void setIdNotaCredito(Integer idNotaCredito) { this.idNotaCredito = idNotaCredito; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }
}
