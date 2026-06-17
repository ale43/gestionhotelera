package Infraestructura;

import DTOS.ResponsableDTO;
import DTOS.ResultadoBajaDTO;
import DTOS.ResultadoVerificacionDTO;
import java.util.List;

/** Serializador JSON mínimo, sin dependencias externas. */
public final class Json {
    private Json() { }

    public static String escapar(String s) {
        if (s == null) return "";
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"':  b.append("\\\""); break;
                case '\\': b.append("\\\\"); break;
                case '\n': b.append("\\n");  break;
                case '\r': b.append("\\r");  break;
                case '\t': b.append("\\t");  break;
                default: if (c < 0x20) b.append(String.format("\\u%04x",(int)c)); else b.append(c);
            }
        }
        return b.toString();
    }

    public static String de(ResponsableDTO r) {
        return "{"
            + "\"idResponsable\":" + r.getIdResponsable() + ","
            + "\"razonSocial\":\"" + escapar(r.getRazonSocial()) + "\","
            + "\"cuit\":\""        + escapar(r.getCuit())        + "\","
            + "\"direccion\":\""   + escapar(r.getDireccion())   + "\","
            + "\"telefono\":\""    + escapar(r.getTelefono())    + "\","
            + "\"estado\":\""      + escapar(r.getEstado())      + "\""
            + "}";
    }

    public static String deLista(List<ResponsableDTO> lista) {
        StringBuilder b = new StringBuilder("[");
        for (int i = 0; i < lista.size(); i++) { if (i>0) b.append(","); b.append(de(lista.get(i))); }
        return b.append("]").toString();
    }

    public static String de(ResultadoVerificacionDTO v) {
        String resp = (v.getResponsable()==null) ? "null" : de(v.getResponsable());
        return "{"
            + "\"puedeEliminarse\":" + v.isPuedeEliminarse() + ","
            + "\"mensaje\":\""       + escapar(v.getMensaje()) + "\","
            + "\"responsable\":"     + resp
            + "}";
    }

    public static String de(ResultadoBajaDTO r) {
        return "{\"exito\":" + r.isExito() + ",\"mensaje\":\"" + escapar(r.getMensaje()) + "\"}";
    }
}
