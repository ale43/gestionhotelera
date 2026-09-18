package Entidades;

public class PersonaFisica extends ResponsableDePago {

    private String nombre;
    private String apellido;
    private String cuit;
    private Integer idHuesped;

    public PersonaFisica(Integer idResponsable, String nombre, String apellido, String cuit,
                         Direccion direccion, String telefono) {
        this(idResponsable, nombre, apellido, cuit, direccion, telefono, null);
    }

    public PersonaFisica(Integer idResponsable, String nombre, String apellido, String cuit,
                         Direccion direccion, String telefono, Integer idHuesped) {
        super(idResponsable, direccion, telefono);
        this.nombre = nombre;
        this.apellido = apellido;
        this.cuit = cuit;
        this.idHuesped = idHuesped;
    }

    public String getNombre()   { return nombre; }
    public String getApellido() { return apellido; }
    public Integer getIdHuesped() { return idHuesped; }
    public void setIdHuesped(Integer idHuesped) { this.idHuesped = idHuesped; }

    @Override public String getCuit()         { return cuit; }
    @Override public String getDenominacion() { return apellido + ", " + nombre; }
}
