package DTOS;

public class ResponsableDTO {
    private final Integer idResponsable;
    private final String razonSocial;  
    private final String cuit;
    private final String direccion;
    private final String telefono;
    private final String estado;

    public ResponsableDTO(Integer idResponsable, String razonSocial, String cuit,
                          String direccion, String telefono, String estado) {
        this.idResponsable = idResponsable; this.razonSocial = razonSocial; this.cuit = cuit;
        this.direccion = direccion; this.telefono = telefono; this.estado = estado;
    }
    public Integer getIdResponsable(){return idResponsable;} public String getRazonSocial(){return razonSocial;}
    public String getCuit(){return cuit;} public String getDireccion(){return direccion;}
    public String getTelefono(){return telefono;} public String getEstado(){return estado;}
}