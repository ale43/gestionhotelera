package DTOS;

public class ResponsableDTO {
    private final Integer idResponsable;
    private final String razonSocial;
    private final String cuit;
    private final String nroDocumento;   // vacío para Persona Jurídica
    private final String direccion;
    private final String telefono;
    private final String estado;

    public ResponsableDTO(Integer idResponsable, String razonSocial, String cuit, String nroDocumento,
                          String direccion, String telefono, String estado) {
        this.idResponsable = idResponsable; this.razonSocial = razonSocial; this.cuit = cuit;
        this.nroDocumento = nroDocumento;
        this.direccion = direccion; this.telefono = telefono; this.estado = estado;
    }
    public Integer getIdResponsable(){return idResponsable;} public String getRazonSocial(){return razonSocial;}
    public String getCuit(){return cuit;} public String getNroDocumento(){return nroDocumento;}
    public String getDireccion(){return direccion;}
    public String getTelefono(){return telefono;} public String getEstado(){return estado;}
}
