package Entidades;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TarjetaCredito extends MedioDePago {
    private String numeroTarjetaCredito;
    private String nombreTitular;
    private LocalDate fechaVencimiento;
    private String codigoSeguridad;
    private BigDecimal limiteCredito;
    private BigDecimal saldoDisponible;
    private String emisor;

    public TarjetaCredito() { }
    public String getNumeroTarjetaCredito() { return numeroTarjetaCredito; }
    public void setNumeroTarjetaCredito(String v) { this.numeroTarjetaCredito = v; }
    public String getNombreTitular() { return nombreTitular; }
    public void setNombreTitular(String v) { this.nombreTitular = v; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate v) { this.fechaVencimiento = v; }
    public String getCodigoSeguridad() { return codigoSeguridad; }
    public void setCodigoSeguridad(String v) { this.codigoSeguridad = v; }
    public BigDecimal getLimiteCredito() { return limiteCredito; }
    public void setLimiteCredito(BigDecimal v) { this.limiteCredito = v; }
    public BigDecimal getSaldoDisponible() { return saldoDisponible; }
    public void setSaldoDisponible(BigDecimal v) { this.saldoDisponible = v; }
    public String getEmisor() { return emisor; }
    public void setEmisor(String emisor) { this.emisor = emisor; }
}
