package Entidades;

/** Subclase Persona Física (por ej. un huésped que es responsable de su propia factura). */
public class PersonaFisica extends ResponsableDePago {

    private String nombre;
    private String apellido;
    private String cuit;
    private String nroDocumento;    // DNI / LC / LE — criterio de búsqueda del CU03
    private String tipoDocumento;

    public PersonaFisica(Integer idResponsable, String nombre, String apellido, String cuit,
                         String nroDocumento, String tipoDocumento,
                         Direccion direccion, String telefono) {
        super(idResponsable, direccion, telefono);
        this.nombre = nombre;
        this.apellido = apellido;
        this.cuit = cuit;
        this.nroDocumento = nroDocumento;
        this.tipoDocumento = tipoDocumento;
    }

    /** Constructor histórico (sin documento), se mantiene por compatibilidad. */
    public PersonaFisica(Integer idResponsable, String nombre, String apellido, String cuit,
                         Direccion direccion, String telefono) {
        this(idResponsable, nombre, apellido, cuit, null, null, direccion, telefono);
    }

    public String getNombre()   { return nombre; }
    public String getApellido() { return apellido; }
    public String getTipoDocumento() { return tipoDocumento; }

    @Override public String getCuit()         { return cuit; }
    @Override public String getNroDocumento() { return nroDocumento; }
    @Override public String getDenominacion() { return apellido + ", " + nombre; }
}
