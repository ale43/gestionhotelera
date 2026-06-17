package Entidades;

/** Subclase Persona Física (por ej. un huésped que es responsable de su propia factura). */
public class PersonaFisica extends ResponsableDePago {

    private String nombre;
    private String apellido;
    private String cuit;

    public PersonaFisica(Integer idResponsable, String nombre, String apellido, String cuit,
                         Direccion direccion, String telefono) {
        super(idResponsable, direccion, telefono);
        this.nombre = nombre;
        this.apellido = apellido;
        this.cuit = cuit;
    }

    public String getNombre()   { return nombre; }
    public String getApellido() { return apellido; }

    @Override public String getCuit()         { return cuit; }
    @Override public String getDenominacion() { return apellido + ", " + nombre; }
}
