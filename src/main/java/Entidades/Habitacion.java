package Entidades;

import java.math.BigDecimal;

public class Habitacion {
    private Integer idHabitacion;
    private String numero;
    private int cantidad;
    private BigDecimal costo;
    private int capacidad;
    private BigDecimal porcentajeDescuento;
    private EstadoHabitacion estado;
    private TipoHabitacion tipoHabitacion;

    public Habitacion() { }
    public Habitacion(Integer idHabitacion, String numero, int cantidad, BigDecimal costo, int capacidad,
                      BigDecimal porcentajeDescuento, EstadoHabitacion estado, TipoHabitacion tipoHabitacion) {
        this.idHabitacion = idHabitacion; this.numero = numero; this.cantidad = cantidad; this.costo = costo;
        this.capacidad = capacidad; this.porcentajeDescuento = porcentajeDescuento; this.estado = estado;
        this.tipoHabitacion = tipoHabitacion;
    }
    public Integer getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(Integer idHabitacion) { this.idHabitacion = idHabitacion; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public BigDecimal getCosto() { return costo; }
    public void setCosto(BigDecimal costo) { this.costo = costo; }
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    public BigDecimal getPorcentajeDescuento() { return porcentajeDescuento; }
    public void setPorcentajeDescuento(BigDecimal v) { this.porcentajeDescuento = v; }
    public EstadoHabitacion getEstado() { return estado; }
    public void setEstado(EstadoHabitacion estado) { this.estado = estado; }
    public TipoHabitacion getTipoHabitacion() { return tipoHabitacion; }
    public void setTipoHabitacion(TipoHabitacion tipoHabitacion) { this.tipoHabitacion = tipoHabitacion; }
}
