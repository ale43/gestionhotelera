package Entidades;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TarjetaDebito extends MedioDePago {
    private String numeroTarjetaDebito;
    private String nombreTitular;
    private LocalDate fechaVencimiento;
    private String codigoSeguridad;
    private TipoTarjeta tipo;
    private String bancoAsociado;
    private String numeroCuenta;
    private BigDecimal saldoCuenta;

    public TarjetaDebito() { }
    public String getNumeroTarjetaDebito() { return numeroTarjetaDebito; }
    public void setNumeroTarjetaDebito(String v) { this.numeroTarjetaDebito = v; }
    public String getNombreTitular() { return nombreTitular; }
    public void setNombreTitular(String v) { this.nombreTitular = v; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate v) { this.fechaVencimiento = v; }
    public String getCodigoSeguridad() { return codigoSeguridad; }
    public void setCodigoSeguridad(String v) { this.codigoSeguridad = v; }
    public TipoTarjeta getTipo() { return tipo; }
    public void setTipo(TipoTarjeta tipo) { this.tipo = tipo; }
    public String getBancoAsociado() { return bancoAsociado; }
    public void setBancoAsociado(String v) { this.bancoAsociado = v; }
    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String v) { this.numeroCuenta = v; }
    public BigDecimal getSaldoCuenta() { return saldoCuenta; }
    public void setSaldoCuenta(BigDecimal v) { this.saldoCuenta = v; }
}
