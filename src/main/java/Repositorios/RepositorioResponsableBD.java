package Repositorios;

import Entidades.Direccion;
import Entidades.PersonaFisica;
import Entidades.PersonaJuridica;
import Entidades.ResponsableDePago;
import Infraestructura.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC (PostgreSQL) de IRepositorioResponsable.
 * Lee la columna "tipo" para devolver la subclase correcta (PersonaFisica /
 * PersonaJuridica), como indica la nota del diagrama de secuencia.
 *
 * Criterios de búsqueda (CU03):
 *   - razonSocial : CONTIENE, sin distinguir mayúsculas ni acentos
 *   - cuit        : EMPIEZA CON, comparando sólo dígitos
 * El filtrado se resuelve en SQL (no se trae toda la tabla a memoria).
 */
public class RepositorioResponsableBD implements IRepositorioResponsable {

    private static final String SELECT_BASE =
        "SELECT r.id_responsable, r.tipo, r.estado, r.razon_social, r.nombre, r.apellido, r.cuit, r.telefono, " +
        "       d.calle, d.numero, d.departamento, d.piso, d.cod_postal, d.localidad, d.provincia, d.pais " +
        "FROM responsable_de_pago r LEFT JOIN direccion d ON r.id_direccion = d.id_direccion ";

    @Override
    public ResponsableDePago buscarPorId(Integer idResponsable) {
        String sql = SELECT_BASE + "WHERE r.id_responsable = ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idResponsable);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? construir(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscarPorId: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizar(ResponsableDePago responsable) {
        // En el CU14 sólo cambia el estado (baja lógica).
        String sql = "UPDATE responsable_de_pago SET estado = ? WHERE id_responsable = ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, responsable.getEstado());
            ps.setInt(2, responsable.getIdResponsable());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizar: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ResponsableDePago> buscarPorCriterios(String razonSocial, String cuit) {
        String rsCrit = razonSocial == null ? "" : razonSocial.trim();
        String cuCrit = soloDigitos(cuit);

        StringBuilder sql = new StringBuilder(SELECT_BASE + "WHERE r.estado = 'ACTIVO'");
        List<String> params = new ArrayList<>();

        // Razón social (jurídica) o "APELLIDO, NOMBRE" (física) que CONTENGA el texto.
        if (!rsCrit.isEmpty()) {
            sql.append(" AND (").append(sinAcentos("COALESCE(r.razon_social,'')")).append(" LIKE ?")
               .append(" OR ").append(sinAcentos("COALESCE(r.apellido,'') || ', ' || COALESCE(r.nombre,'')")).append(" LIKE ?")
               .append(")");
            String patron = contiene(rsCrit);
            params.add(patron);
            params.add(patron);
        }

        // CUIT: "empieza con" (criterio del enunciado), ignorando guiones y puntos.
        if (!cuCrit.isEmpty()) {
            sql.append(" AND ").append(soloDigitosSql("r.cuit")).append(" LIKE ?");
            params.add(escaparLike(cuCrit) + "%");
        }

        sql.append(" ORDER BY r.id_responsable");

        List<ResponsableDePago> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setString(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(construir(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscarPorCriterios: " + e.getMessage(), e);
        }
        return lista;
    }

    // ------------------------------------------------------------------
    // Helpers de normalización (el mismo criterio en SQL y en Java)
    // ------------------------------------------------------------------

    /** Expresión SQL que pasa la columna a mayúsculas y le quita los acentos. */
    private static String sinAcentos(String expr) {
        return "translate(upper(" + expr + "), "
             + "'ÁÀÄÂÃÉÈËÊÍÌÏÎÓÒÖÔÕÚÙÜÛÑÇ', "
             + "'AAAAAEEEEIIIIOOOOOUUUUNC')";
    }

    /** Expresión SQL que deja sólo los dígitos de una columna (quita - . y espacios). */
    private static String soloDigitosSql(String expr) {
        return "translate(COALESCE(" + expr + ",''), '-. ', '')";
    }

    /** Patrón LIKE "contiene", normalizado igual que la columna. */
    private static String contiene(String criterio) {
        return "%" + escaparLike(normalizar(criterio)) + "%";
    }

    /** Mayúsculas y sin acentos, para que el criterio matchee lo que devuelve sinAcentos(). */
    private static String normalizar(String s) {
        String sinTildes = Normalizer.normalize(s, Normalizer.Form.NFD)
                                     .replaceAll("\\p{M}+", "");
        return sinTildes.toUpperCase();
    }

    /** Neutraliza los comodines de LIKE que pueda escribir el usuario. */
    private static String escaparLike(String s) {
        return s.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }

    private static String soloDigitos(String s) {
        return s == null ? "" : s.replaceAll("\\D", "");
    }

    private ResponsableDePago construir(ResultSet rs) throws SQLException {
        Direccion dir = new Direccion(
            rs.getString("calle"), rs.getString("numero"), rs.getString("departamento"), rs.getString("piso"),
            rs.getString("cod_postal"), rs.getString("localidad"), rs.getString("provincia"), rs.getString("pais"));

        Integer id  = rs.getInt("id_responsable");
        String tipo = rs.getString("tipo");
        String cuit = rs.getString("cuit");
        String tel  = rs.getString("telefono");

        ResponsableDePago r;
        if ("JURIDICA".equals(tipo)) {
            r = new PersonaJuridica(id, rs.getString("razon_social"), cuit, dir, tel);
        } else {
            r = new PersonaFisica(id, rs.getString("nombre"), rs.getString("apellido"), cuit, dir, tel);
        }
        r.setEstado(rs.getString("estado"));
        return r;
    }
}
