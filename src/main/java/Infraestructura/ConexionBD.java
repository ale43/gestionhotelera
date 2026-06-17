package Infraestructura;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConexionBD {

    private static final String HOST     = "localhost";
    private static final String PUERTO   = "5432";            
    private static final String BASE     = "gestion_hotelera";
    private static final String USUARIO  = "postgres";        
    private static final String PASSWORD = "hotel1234";
    

    private static final String URL = "jdbc:postgresql://" + HOST + ":" + PUERTO + "/" + BASE;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Falta el driver PostgreSQL: agregá la dependencia en el pom.xml", e);
        }
    }

    private ConexionBD() { }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}