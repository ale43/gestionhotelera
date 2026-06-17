package Entidades;

import java.time.LocalDate;

public class Huesped {
    private Integer idHuesped;
    private String nombre;
    private String apellido;
    private String nroDocumento;
    private TipoDocumento tipoDocumento;
    private String cuit;
    private PosicionIVA posicionIVA;
    private int edad;
    private String telefono;
    private Direccion direccion;
    private String email;
    private LocalDate fechaNacimiento;
    private String nacionalidad;
    private String ocupacion;
    private Estadia estadia;

    public Huesped() { }

    public Integer getIdHuesped() { return idHuesped; }
    public void setIdHuesped(Integer idHuesped) { this.idHuesped = idHuesped; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getNroDocumento() { return nroDocumento; }
    public void setNroDocumento(String nroDocumento) { this.nroDocumento = nroDocumento; }
    public TipoDocumento getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(TipoDocumento tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    public String getCuit() { return cuit; }
    public void setCuit(String cuit) { this.cuit = cuit; }
    public PosicionIVA getPosicionIVA() { return posicionIVA; }
    public void setPosicionIVA(PosicionIVA posicionIVA) { this.posicionIVA = posicionIVA; }
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public Direccion getDireccion() { return direccion; }
    public void setDireccion(Direccion direccion) { this.direccion = direccion; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate f) { this.fechaNacimiento = f; }
    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
    public String getOcupacion() { return ocupacion; }
    public void setOcupacion(String ocupacion) { this.ocupacion = ocupacion; }
    public Estadia getEstadia() { return estadia; }
    public void setEstadia(Estadia estadia) { this.estadia = estadia; }
}
