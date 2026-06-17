package Entidades;

public class Conserje {
    private Integer idConserje;
    private String nombre;
    private String password;

    public Conserje() { }
    public Conserje(Integer idConserje, String nombre, String password) {
        this.idConserje = idConserje; this.nombre = nombre; this.password = password;
    }
    public Integer getIdConserje() { return idConserje; }
    public void setIdConserje(Integer idConserje) { this.idConserje = idConserje; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
