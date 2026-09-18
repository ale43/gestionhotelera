package Entidades;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Factura {
    private Integer idFactura;
    private LocalDate fecha;
    private Integer idResponsable;
    private Integer idEstadia;
    private BigDecimal monto;
    private TipoFactura tipo;
    private EstadoFactura estado;

    public Factura() { }

    public Factura(Integer idFactura, LocalDate fecha, Integer idResponsable, BigDecimal monto) {
        this.idFactura = idFactura; this.fecha = fecha; this.idResponsable = idResponsable; this.monto = monto;
    }

    public Integer getIdFactura() { return idFactura; }
    public void setIdFactura(Integer idFactura) { this.idFactura = idFactura; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public Integer getIdResponsable() { return idResponsable; }
    public void setIdResponsable(Integer idResponsable) { this.idResponsable = idResponsable; }
    public Integer getIdEstadia() { return idEstadia; }
    public void setIdEstadia(Integer idEstadia) { this.idEstadia = idEstadia; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public TipoFactura getTipo() { return tipo; }
    public void setTipo(TipoFactura tipo) { this.tipo = tipo; }
    public EstadoFactura getEstado() { return estado; }
    public void setEstado(EstadoFactura estado) { this.estado = estado; }
}
