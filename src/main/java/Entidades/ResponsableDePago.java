package Entidades;

import DTOS.ResponsableDTO;

public abstract class ResponsableDePago {

    protected final Integer idResponsable;
    protected EstadoResponsable estado;
    protected Direccion direccion;
    protected String telefono;

    protected ResponsableDePago(Integer idResponsable, Direccion direccion, String telefono) {
        this.idResponsable = idResponsable;
        this.direccion = direccion;
        this.telefono = telefono;
        this.estado = EstadoResponsable.ACTIVO;
    }

    public Integer getIdResponsable() { return idResponsable; }
    public EstadoResponsable getEstado() { return estado; }
    public void setEstado(EstadoResponsable estado) { this.estado = estado; }
    public Direccion getDireccion()   { return direccion; }
    public String getTelefono()       { return telefono; }

    public boolean estaEliminado() { return estado == EstadoResponsable.ELIMINADO; }

    public void darDeBaja() { this.estado = EstadoResponsable.ELIMINADO; }

    public abstract String getCuit();
    public abstract String getDenominacion();

    public ResponsableDTO toDTO() {
        return new ResponsableDTO(idResponsable, getDenominacion(), getCuit(),
                                  direccion.enLinea(), telefono, estado.name());
    }
}
