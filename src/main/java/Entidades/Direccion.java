package Entidades;

public class Direccion {
    private final String calle, numero, departamento, piso, codigoPostal, localidad, provincia, pais;

    public Direccion(String calle, String numero, String departamento, String piso,
                     String codigoPostal, String localidad, String provincia, String pais) {
        this.calle = calle; this.numero = numero; this.departamento = departamento; this.piso = piso;
        this.codigoPostal = codigoPostal; this.localidad = localidad; this.provincia = provincia; this.pais = pais;
    }
    public String getCalle(){return calle;} public String getNumero(){return numero;}
    public String getDepartamento(){return departamento;} public String getPiso(){return piso;}
    public String getCodigoPostal(){return codigoPostal;} public String getLocalidad(){return localidad;}
    public String getProvincia(){return provincia;} public String getPais(){return pais;}

    public String enLinea() {
        StringBuilder b = new StringBuilder();
        b.append(calle).append(" ").append(numero);
        if (piso != null && !piso.isBlank())                 b.append(", Piso ").append(piso);
        if (departamento != null && !departamento.isBlank()) b.append(" Dto. ").append(departamento);
        b.append(" (").append(codigoPostal).append(") ").append(localidad).append(", ")
         .append(provincia).append(", ").append(pais);
        return b.toString();
    }
}
