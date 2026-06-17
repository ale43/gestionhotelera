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
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC (PostgreSQL) de IRepositorioResponsable.
 * Lee la columna "tipo" para devolver la subclase correcta (PersonaFisica /
 * PersonaJuridica), como indica la nota del diagrama de secuencia.
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
        String sql = SELECT_BASE + "WHERE r.estado = 'ACTIVO' ORDER BY r.id_responsable";
        String rsCrit = razonSocial == null ? "" : razonSocial.trim().toUpperCase();
        String cuCrit  = cuit == null ? "" : cuit.trim();

        List<ResponsableDePago> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ResponsableDePago r = construir(rs);
                boolean okRs = rsCrit.isEmpty() || r.getDenominacion().toUpperCase().startsWith(rsCrit);
                boolean okCu = cuCrit.isEmpty() || r.getCuit().startsWith(cuCrit);
                if (okRs && okCu) lista.add(r);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscarPorCriterios: " + e.getMessage(), e);
        }
        return lista;
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
