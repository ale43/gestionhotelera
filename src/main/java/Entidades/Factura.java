package Entidades;

import java.math.BigDecimal;
import java.time.LocalDate;


public class Factura {
    private Integer idFactura;
    private LocalDate fecha;
    private String cuitResponsable;
    private BigDecimal monto;
    private TipoFactura tipo;
    private EstadoFactura estado;

    public Factura() { }

    public Factura(Integer idFactura, LocalDate fecha, String cuitResponsable, BigDecimal monto) {
        this.idFactura = idFactura; this.fecha = fecha; this.cuitResponsable = cuitResponsable; this.monto = monto;
    }

    public Integer getIdFactura() { return idFactura; }
    public void setIdFactura(Integer idFactura) { this.idFactura = idFactura; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getCuitResponsable() { return cuitResponsable; }
    public void setCuitResponsable(String cuitResponsable) { this.cuitResponsable = cuitResponsable; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public TipoFactura getTipo() { return tipo; }
    public void setTipo(TipoFactura tipo) { this.tipo = tipo; }
    public EstadoFactura getEstado() { return estado; }
    public void setEstado(EstadoFactura estado) { this.estado = estado; }
}
