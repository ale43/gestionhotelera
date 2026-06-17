package Entidades;

import java.time.LocalDate;

public class Reserva {
    private Integer idReserva;
    private EstadoReserva estado;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private String nombreHuesped;
    private String apellidoHuesped;
    private String telefonoHuesped;
    private Habitacion habitacion;

    public Reserva() { }

    public Integer getIdReserva() { return idReserva; }
    public void setIdReserva(Integer idReserva) { this.idReserva = idReserva; }
    public EstadoReserva getEstado() { return estado; }
    public void setEstado(EstadoReserva estado) { this.estado = estado; }
    public LocalDate getFechaEntrada() { return fechaEntrada; }
    public void setFechaEntrada(LocalDate fechaEntrada) { this.fechaEntrada = fechaEntrada; }
    public LocalDate getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(LocalDate fechaSalida) { this.fechaSalida = fechaSalida; }
    public String getNombreHuesped() { return nombreHuesped; }
    public void setNombreHuesped(String v) { this.nombreHuesped = v; }
    public String getApellidoHuesped() { return apellidoHuesped; }
    public void setApellidoHuesped(String v) { this.apellidoHuesped = v; }
    public String getTelefonoHuesped() { return telefonoHuesped; }
    public void setTelefonoHuesped(String v) { this.telefonoHuesped = v; }
    public Habitacion getHabitacion() { return habitacion; }
    public void setHabitacion(Habitacion habitacion) { this.habitacion = habitacion; }
}
