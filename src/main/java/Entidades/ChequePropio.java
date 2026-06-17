package Entidades;

import java.time.LocalDate;

public class ChequePropio extends MedioDePago {
    private int numeroChequePropio;
    private LocalDate fecha;
    private String banco;
    private String beneficiario;

    public ChequePropio() { }
    public int getNumeroChequePropio() { return numeroChequePropio; }
    public void setNumeroChequePropio(int v) { this.numeroChequePropio = v; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getBanco() { return banco; }
    public void setBanco(String banco) { this.banco = banco; }
    public String getBeneficiario() { return beneficiario; }
    public void setBeneficiario(String beneficiario) { this.beneficiario = beneficiario; }
}
