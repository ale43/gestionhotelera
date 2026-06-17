package Entidades;

import java.time.LocalDateTime;

public class Estadia {
    private Integer idEstadia;
    private int cantidadHuespedes;
    private int cantidadHabitaciones;
    private int cantidadDias;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Habitacion habitacion;
    private Huesped huesped;
    private Integer idReserva;

    public Estadia() { }

    public Integer getIdEstadia() { return idEstadia; }
    public void setIdEstadia(Integer idEstadia) { this.idEstadia = idEstadia; }
    public int getCantidadHuespedes() { return cantidadHuespedes; }
    public void setCantidadHuespedes(int v) { this.cantidadHuespedes = v; }
    public int getCantidadHabitaciones() { return cantidadHabitaciones; }
    public void setCantidadHabitaciones(int v) { this.cantidadHabitaciones = v; }
    public int getCantidadDias() { return cantidadDias; }
    public void setCantidadDias(int v) { this.cantidadDias = v; }
    public LocalDateTime getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDateTime checkIn) { this.checkIn = checkIn; }
    public LocalDateTime getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDateTime checkOut) { this.checkOut = checkOut; }
    public Habitacion getHabitacion() { return habitacion; }
    public void setHabitacion(Habitacion habitacion) { this.habitacion = habitacion; }
    public Huesped getHuesped() { return huesped; }
    public void setHuesped(Huesped huesped) { this.huesped = huesped; }
    public Integer getIdReserva() { return idReserva; }
    public void setIdReserva(Integer idReserva) { this.idReserva = idReserva; }
}
