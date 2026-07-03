package DTOS;
public class ConserjeDTO {
    private final Integer idConserje;
    private final String nombre;

    public ConserjeDTO(Integer idConserje, String nombre) {
        this.idConserje = idConserje;
        this.nombre = nombre;
    }
    public Integer getIdConserje() { return idConserje; }
    public String getNombre()      { return nombre; }
}
