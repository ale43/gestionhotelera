package Repositorios;

import DTOS.ConserjeDTO;
import Infraestructura.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RepositorioConserjeBD implements IRepositorioConserje {

    @Override
    public boolean autenticar(String nombre, String password) {
        String sql = "SELECT 1 FROM conserje WHERE LOWER(nombre) = LOWER(?) AND password = ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error autenticando conserje: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ConserjeDTO> listar() {
        String sql = "SELECT id_conserje, nombre FROM conserje ORDER BY id_conserje";
        List<ConserjeDTO> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new ConserjeDTO(rs.getInt("id_conserje"), rs.getString("nombre")));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Error listando conserjes: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existeNombre(String nombre) {
        String sql = "SELECT 1 FROM conserje WHERE LOWER(nombre) = LOWER(?)";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error verificando conserje: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean crear(String nombre, String password) {
        
        String sql = "INSERT INTO conserje (id_conserje, nombre, password) " +
                     "VALUES ((SELECT COALESCE(MAX(id_conserje),0)+1 FROM conserje), ?, ?)";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre.trim().toLowerCase());
            ps.setString(2, password);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Error creando conserje: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(String nombre) {
        String sql = "DELETE FROM conserje WHERE LOWER(nombre) = LOWER(?)";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando conserje: " + e.getMessage(), e);
        }
    }

    @Override
    public int contar() {
        try (Connection con = ConexionBD.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM conserje")) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("Error contando conserjes: " + e.getMessage(), e);
        }
    }
}
