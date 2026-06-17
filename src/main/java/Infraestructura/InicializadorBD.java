package Infraestructura;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Crea TODAS las tablas del modelo (si no existen) y carga datos de prueba.
 * - Las tablas del CU14 (direccion, responsable_de_pago, factura) ya existían: se
 *   mantienen y a factura se le agregan columnas con ALTER ... IF NOT EXISTS.
 * - El resto de las entidades del diagrama se crean acá (Conserje, Habitacion,
 *   Reserva, Estadia, Consumo, Huesped, NotaCredito, Pago, MedioDePago).
 * La jerarquía de medios de pago se mapea con "tabla única" (columna tipo).
 */
public final class InicializadorBD {

    private InicializadorBD() { }

    public static void inicializar() {
        try (Connection con = ConexionBD.getConnection(); Statement st = con.createStatement()) {
            
            st.execute(
                "CREATE TABLE IF NOT EXISTS direccion (" +
                "  id_direccion INT PRIMARY KEY," +
                "  calle VARCHAR(100), numero VARCHAR(20), departamento VARCHAR(20), piso VARCHAR(20)," +
                "  cod_postal VARCHAR(20), localidad VARCHAR(80), provincia VARCHAR(80), pais VARCHAR(80))");

            st.execute(
                "CREATE TABLE IF NOT EXISTS responsable_de_pago (" +
                "  id_responsable INT PRIMARY KEY," +
                "  tipo VARCHAR(10) NOT NULL, estado VARCHAR(15) NOT NULL," +
                "  razon_social VARCHAR(150), nombre VARCHAR(80), apellido VARCHAR(80)," +
                "  cuit VARCHAR(20), telefono VARCHAR(40), id_direccion INT," +
                "  CONSTRAINT fk_resp_dir FOREIGN KEY (id_direccion) REFERENCES direccion(id_direccion))");

            st.execute(
                "CREATE TABLE IF NOT EXISTS factura (" +
                "  id_factura INT PRIMARY KEY, fecha DATE, cuit_responsable VARCHAR(20), monto DECIMAL(12,2))");
            st.execute("ALTER TABLE factura ADD COLUMN IF NOT EXISTS tipo VARCHAR(2)");
            st.execute("ALTER TABLE factura ADD COLUMN IF NOT EXISTS estado VARCHAR(15)");

            st.execute(
                "CREATE TABLE IF NOT EXISTS conserje (" +
                "  id_conserje INT PRIMARY KEY, nombre VARCHAR(80), password VARCHAR(80))");

            st.execute(
                "CREATE TABLE IF NOT EXISTS tipo_habitacion (" +
                "  id_tipo_habitacion INT PRIMARY KEY, descripcion VARCHAR(100)," +
                "  cantidad_camas_kingsize INT, cantidad_camas_dobles INT, cantidad_camas_individuales INT)");

            st.execute(
                "CREATE TABLE IF NOT EXISTS habitacion (" +
                "  id_habitacion INT PRIMARY KEY, numero VARCHAR(20), cantidad INT," +
                "  costo DECIMAL(12,2), capacidad INT, porcentaje_descuento DECIMAL(5,2)," +
                "  estado VARCHAR(20), id_tipo_habitacion INT," +
                "  CONSTRAINT fk_hab_tipo FOREIGN KEY (id_tipo_habitacion) REFERENCES tipo_habitacion(id_tipo_habitacion))");

            st.execute(
                "CREATE TABLE IF NOT EXISTS huesped (" +
                "  id_huesped INT PRIMARY KEY, nombre VARCHAR(80), apellido VARCHAR(80)," +
                "  nro_documento VARCHAR(30), tipo_documento VARCHAR(15), cuit VARCHAR(20)," +
                "  posicion_iva VARCHAR(25), edad INT, telefono VARCHAR(40), id_direccion INT," +
                "  email VARCHAR(120), fecha_nacimiento DATE, nacionalidad VARCHAR(60), ocupacion VARCHAR(80)," +
                "  CONSTRAINT fk_hue_dir FOREIGN KEY (id_direccion) REFERENCES direccion(id_direccion))");

            st.execute(
                "CREATE TABLE IF NOT EXISTS reserva (" +
                "  id_reserva INT PRIMARY KEY, estado VARCHAR(15), fecha_entrada DATE, fecha_salida DATE," +
                "  nombre_huesped VARCHAR(80), apellido_huesped VARCHAR(80), telefono_huesped VARCHAR(40)," +
                "  id_habitacion INT," +
                "  CONSTRAINT fk_res_hab FOREIGN KEY (id_habitacion) REFERENCES habitacion(id_habitacion))");

            st.execute(
                "CREATE TABLE IF NOT EXISTS estadia (" +
                "  id_estadia INT PRIMARY KEY, cantidad_huespedes INT, cantidad_habitaciones INT," +
                "  cantidad_dias INT, check_in TIMESTAMP, check_out TIMESTAMP," +
                "  id_habitacion INT, id_huesped INT, id_reserva INT," +
                "  CONSTRAINT fk_est_hab FOREIGN KEY (id_habitacion) REFERENCES habitacion(id_habitacion)," +
                "  CONSTRAINT fk_est_hue FOREIGN KEY (id_huesped) REFERENCES huesped(id_huesped)," +
                "  CONSTRAINT fk_est_res FOREIGN KEY (id_reserva) REFERENCES reserva(id_reserva))");

            st.execute(
                "CREATE TABLE IF NOT EXISTS consumo (" +
                "  id_consumo INT PRIMARY KEY, monto DECIMAL(12,2), tipo VARCHAR(25), id_estadia INT," +
                "  CONSTRAINT fk_con_est FOREIGN KEY (id_estadia) REFERENCES estadia(id_estadia))");

            st.execute(
                "CREATE TABLE IF NOT EXISTS nota_credito (" +
                "  id_nota_credito INT PRIMARY KEY, descripcion VARCHAR(200), monto DECIMAL(12,2), id_factura INT," +
                "  CONSTRAINT fk_nc_fac FOREIGN KEY (id_factura) REFERENCES factura(id_factura))");

            st.execute(
                "CREATE TABLE IF NOT EXISTS pago (" +
                "  id_pago INT PRIMARY KEY, monto DECIMAL(12,2), id_factura INT," +
                "  CONSTRAINT fk_pago_fac FOREIGN KEY (id_factura) REFERENCES factura(id_factura))");

            st.execute(
                "CREATE TABLE IF NOT EXISTS medio_de_pago (" +
                "  id_medio_de_pago INT PRIMARY KEY," +
                "  tipo VARCHAR(20) NOT NULL," +      // EFECTIVO|MONEDA_EXTRANJERA|CHEQUE_PROPIO|CHEQUE_TERCERO|TARJETA_CREDITO|TARJETA_DEBITO
                "  monto DECIMAL(12,2), fecha_de_pago DATE, id_pago INT," +
                "  tipo_moneda VARCHAR(20)," +
                "  numero_cheque INT, fecha_cheque DATE, banco VARCHAR(80), beneficiario VARCHAR(80)," +
                "  numero_tarjeta VARCHAR(30), nombre_titular VARCHAR(80), fecha_vencimiento DATE," +
                "  codigo_seguridad VARCHAR(10), limite_credito DECIMAL(12,2), saldo_disponible DECIMAL(12,2)," +
                "  emisor VARCHAR(60), tipo_tarjeta VARCHAR(20), banco_asociado VARCHAR(80)," +
                "  numero_cuenta VARCHAR(40), saldo_cuenta DECIMAL(12,2)," +
                "  CONSTRAINT fk_mp_pago FOREIGN KEY (id_pago) REFERENCES pago(id_pago))");

            // ---------- SEEDS ----------
            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM responsable_de_pago")) {
                rs.next();
                if (rs.getInt(1) == 0) sembrarCU14(st);
            }
            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM conserje")) {
                rs.next();
                if (rs.getInt(1) == 0) sembrarBasicos(st);
            }

            System.out.println("Base de datos lista (todas las tablas).");

        } catch (SQLException e) {
            throw new RuntimeException("Error inicializando la base: " + e.getMessage(), e);
        }
    }

    private static void sembrarCU14(Statement st) throws SQLException {
        st.execute("INSERT INTO direccion (id_direccion, calle, numero, departamento, piso, cod_postal, localidad, provincia, pais) VALUES " +
            "(1,'SAN MARTIN','1234','','','3000','SANTA FE','SANTA FE','ARGENTINA')," +
            "(2,'URQUIZA','850','','2','3100','PARANA','ENTRE RIOS','ARGENTINA')," +
            "(3,'9 DE JULIO','4567','B','5','3000','SANTA FE','SANTA FE','ARGENTINA')," +
            "(4,'RIVADAVIA','333','','','3000','SANTA FE','SANTA FE','ARGENTINA')");

        st.execute("INSERT INTO responsable_de_pago (id_responsable, tipo, estado, razon_social, nombre, apellido, cuit, telefono, id_direccion) VALUES " +
            "(1,'JURIDICA','ACTIVO','HOTELERIA DEL LITORAL S.A.',NULL,NULL,'30-12345678-9','0342-4550000',1)," +
            "(2,'JURIDICA','ACTIVO','TURISMO PARANA S.R.L.',NULL,NULL,'30-99887766-5','0343-4231122',2)," +
            "(3,'JURIDICA','ACTIVO','CONSULTORA DEL CENTRO S.A.S.',NULL,NULL,'27-33445566-8','0342-4889900',3)," +
            "(4,'FISICA','ACTIVO',NULL,'JUAN','PEREZ','20-31222333-4','0342-4567788',4)");

        st.execute("INSERT INTO factura (id_factura, fecha, cuit_responsable, monto, tipo, estado) VALUES " +
            "(1001,'2025-03-10','30-12345678-9',154300.00,'A','PAGADA')," +
            "(1002,'2025-04-22','30-12345678-9',90560.00,'A','PENDIENTE')," +
            "(1003,'2025-05-15','27-33445566-8',211000.00,'B','PAGADA')");
        System.out.println("Datos del CU14 cargados.");
    }

    private static void sembrarBasicos(Statement st) throws SQLException {
        st.execute("INSERT INTO conserje (id_conserje, nombre, password) VALUES " +
            "(1,'admin','admin123'),(2,'jperez','clave456')");

        st.execute("INSERT INTO tipo_habitacion (id_tipo_habitacion, descripcion, cantidad_camas_kingsize, cantidad_camas_dobles, cantidad_camas_individuales) VALUES " +
            "(1,'Suite',1,1,0),(2,'Estandar',0,1,2)");

        st.execute("INSERT INTO habitacion (id_habitacion, numero, cantidad, costo, capacidad, porcentaje_descuento, estado, id_tipo_habitacion) VALUES " +
            "(1,'101',1,15000.00,2,0,'LIBRE',2)," +
            "(2,'201',1,28000.00,3,10,'LIBRE',1)");
        System.out.println("Datos basicos (conserje, habitaciones) cargados.");
    }
}
