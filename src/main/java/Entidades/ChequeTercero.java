package Entidades;

import java.time.LocalDate;

public class ChequeTercero extends MedioDePago {
    private int numeroChequeTercero;
    private LocalDate fecha;
    private String banco;
    private String beneficiario;

    public ChequeTercero() { }
    public int getNumeroChequeTercero() { return numeroChequeTercero; }
    public void setNumeroChequeTercero(int v) { this.numeroChequeTercero = v; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getBanco() { return banco; }
    public void setBanco(String banco) { this.banco = banco; }
    public String getBeneficiario() { return beneficiario; }
    public void setBeneficiario(String beneficiario) { this.beneficiario = beneficiario; }
}
