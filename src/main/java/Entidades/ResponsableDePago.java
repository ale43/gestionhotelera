package Entidades;

import DTOS.ResponsableDTO;

public abstract class ResponsableDePago {

    protected final Integer idResponsable;
    protected String estado;
    protected Direccion direccion;
    protected String telefono;

    protected ResponsableDePago(Integer idResponsable, Direccion direccion, String telefono) {
        this.idResponsable = idResponsable;
        this.direccion = direccion;
        this.telefono = telefono;
        this.estado = "ACTIVO";
    }

    public Integer getIdResponsable() { return idResponsable; }
    public String getEstado()         { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Direccion getDireccion()   { return direccion; }
    public String getTelefono()       { return telefono; }

    public abstract String getCuit();
    public abstract String getDenominacion();

    public ResponsableDTO toDTO() {
        return new ResponsableDTO(idResponsable, getDenominacion(), getCuit(),
                                  direccion.enLinea(), telefono, estado);
    }
}
