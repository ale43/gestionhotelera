package Entidades;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class MedioDePago {
    protected Integer idMedioDePago;
    protected BigDecimal monto;
    protected LocalDate fechaDePago;
    protected Pago pago;

    public Integer getIdMedioDePago() { return idMedioDePago; }
    public void setIdMedioDePago(Integer idMedioDePago) { this.idMedioDePago = idMedioDePago; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public LocalDate getFechaDePago() { return fechaDePago; }
    public void setFechaDePago(LocalDate fechaDePago) { this.fechaDePago = fechaDePago; }
    public Pago getPago() { return pago; }
    public void setPago(Pago pago) { this.pago = pago; }
}
