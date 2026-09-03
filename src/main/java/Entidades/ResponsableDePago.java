package Entidades;

import DTOS.ResponsableDTO;

/**
 * Clase ABSTRACTA "Responsable de Pago" del diagrama, generalizada en
 * PersonaFisica y PersonaJuridica. El diagrama de secuencia del CU14 usa, sobre
 * "res", los métodos getCuit(), setEstado() y toDTO() de forma polimórfica:
 * cualquiera de las dos subclases responde a ellos.
 *
 * La baja del CU14 es LÓGICA: estado pasa a "ELIMINADO" (no se borra de la BD).
 */
public abstract class ResponsableDePago {

    protected final Integer idResponsable;   // ID_Responsable del diagrama
    protected String estado;                 // "ACTIVO" / "ELIMINADO"
    protected Direccion direccion;
    protected String telefono;

    protected ResponsableDePago(Integer idResponsable, Direccion direccion, String telefono) {
        this.idResponsable = idResponsable;
        this.direccion = direccion;
        this.telefono = telefono;
        this.estado = "ACTIVO";
    }

    // --- comunes a ambas subclases ---
    public Integer getIdResponsable() { return idResponsable; }
    public String getEstado()         { return estado; }
    public void setEstado(String estado) { this.estado = estado; }   // <- usado en la baja lógica
    public Direccion getDireccion()   { return direccion; }
    public String getTelefono()       { return telefono; }

    // --- polimórficos (cada subclase los resuelve) ---
    public abstract String getCuit();
    public abstract String getDenominacion();   // razón social o "apellido, nombre"

    /** Arma el DTO para la vista (res.toDTO() del diagrama). */
    public ResponsableDTO toDTO() {
        return new ResponsableDTO(idResponsable, getDenominacion(), getCuit(),
                                  direccion.enLinea(), telefono, estado);
    }
}
