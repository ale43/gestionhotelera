package Repositorios;

import Infraestructura.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepositorioFacturasBD implements IRepositorioFacturas {

    @Override
    public boolean existeFactura(String cuit) {
        String sql = "SELECT COUNT(*) FROM factura WHERE cuit_responsable = ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cuit);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error existeFactura: " + e.getMessage(), e);
        }
    }
}