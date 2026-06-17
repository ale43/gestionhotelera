package Entidades;

public class TipoHabitacion {
    private Integer idTipoHabitacion;
    private String descripcion;
    private int cantidadCamasKingSize;
    private int cantidadCamasDobles;
    private int cantidadCamasIndividuales;

    public TipoHabitacion() { }
    public TipoHabitacion(Integer idTipoHabitacion, String descripcion, int cantidadCamasKingSize,
                          int cantidadCamasDobles, int cantidadCamasIndividuales) {
        this.idTipoHabitacion = idTipoHabitacion; this.descripcion = descripcion;
        this.cantidadCamasKingSize = cantidadCamasKingSize; this.cantidadCamasDobles = cantidadCamasDobles;
        this.cantidadCamasIndividuales = cantidadCamasIndividuales;
    }
    public Integer getIdTipoHabitacion() { return idTipoHabitacion; }
    public void setIdTipoHabitacion(Integer idTipoHabitacion) { this.idTipoHabitacion = idTipoHabitacion; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getCantidadCamasKingSize() { return cantidadCamasKingSize; }
    public void setCantidadCamasKingSize(int v) { this.cantidadCamasKingSize = v; }
    public int getCantidadCamasDobles() { return cantidadCamasDobles; }
    public void setCantidadCamasDobles(int v) { this.cantidadCamasDobles = v; }
    public int getCantidadCamasIndividuales() { return cantidadCamasIndividuales; }
    public void setCantidadCamasIndividuales(int v) { this.cantidadCamasIndividuales = v; }
}
