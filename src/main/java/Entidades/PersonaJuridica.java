package Entidades;

/** Subclase Persona Jurídica (las "firmas": razón social + CUIT). Es lo que el ABM CU12/13/14 maneja. */
public class PersonaJuridica extends ResponsableDePago {

    private String razonSocial;
    private String cuit;

    public PersonaJuridica(Integer idResponsable, String razonSocial, String cuit,
                           Direccion direccion, String telefono) {
        super(idResponsable, direccion, telefono);
        this.razonSocial = razonSocial;
        this.cuit = cuit;
    }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
    public void setCuit(String cuit) { this.cuit = cuit; }

    @Override public String getCuit()         { return cuit; }
    @Override public String getDenominacion() { return razonSocial; }
}
