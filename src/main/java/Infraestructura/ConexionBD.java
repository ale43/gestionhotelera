package Infraestructura;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ConexionBD {

    private static final String ARCHIVO = "db.properties";

    private static final Properties CONFIG = cargarArchivo();

    private static final String HOST     = valor("db.host",     "PGHOST",     "localhost");
    private static final String PUERTO   = valor("db.puerto",   "PGPORT",     "5432");
    private static final String BASE     = valor("db.base",     "PGDATABASE", "gestion_hotelera");
    private static final String USUARIO  = valor("db.usuario",  "PGUSER",     "postgres");
    private static final String PASSWORD = obligatorio("db.password", "PGPASSWORD");

    private static final String URL = "jdbc:postgresql://" + HOST + ":" + PUERTO + "/" + BASE;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Falta el driver PostgreSQL: agrega la dependencia en el pom.xml", e);
        }
    }

    private ConexionBD() { }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    private static Properties cargarArchivo() {
        Properties p = new Properties();
        Path ruta = Path.of(ARCHIVO);
        if (Files.exists(ruta)) {
            try (InputStream in = Files.newInputStream(ruta)) {
                p.load(in);
            } catch (IOException e) {
                throw new RuntimeException("No se pudo leer " + ARCHIVO + ": " + e.getMessage(), e);
            }
        }
        return p;
    }

    private static String valor(String clave, String variableEntorno, String porDefecto) {
        String delEntorno = System.getenv(variableEntorno);
        if (delEntorno != null && !delEntorno.isBlank()) return delEntorno.trim();
        return CONFIG.getProperty(clave, porDefecto).trim();
    }

    private static String obligatorio(String clave, String variableEntorno) {
        String v = System.getenv(variableEntorno);
        if (v == null || v.isBlank()) v = CONFIG.getProperty(clave);
        if (v == null || v.isBlank()) {
            throw new RuntimeException(
                "Falta la contrasena de la base de datos.\n"
              + "  Cree el archivo '" + ARCHIVO + "' en la raiz del proyecto "
              + "(puede copiar db.properties.example y completarlo),\n"
              + "  o defina la variable de entorno " + variableEntorno + ".");
        }
        return v.trim();
    }
}
