package Infraestructura;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
                "CREATE TABLE IF NOT EXISTS persona_juridica (" +
                "  id_responsable INT PRIMARY KEY," +
                "  razon_social VARCHAR(150)," +
                "  CONSTRAINT fk_pj_resp FOREIGN KEY (id_responsable) REFERENCES responsable_de_pago(id_responsable))");

            st.execute(
                "CREATE TABLE IF NOT EXISTS persona_fisica (" +
                "  id_responsable INT PRIMARY KEY," +
                "  nombre VARCHAR(80), apellido VARCHAR(80)," +
                "  CONSTRAINT fk_pf_resp FOREIGN KEY (id_responsable) REFERENCES responsable_de_pago(id_responsable))");
            st.execute(
                "CREATE TABLE IF NOT EXISTS factura (" +
                "  id_factura INT PRIMARY KEY, fecha DATE, monto DECIMAL(12,2))");
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
                "  tipo VARCHAR(20) NOT NULL," +
                "  monto DECIMAL(12,2), fecha_de_pago DATE, id_pago INT," +
                "  tipo_moneda VARCHAR(20)," +
                "  numero_cheque INT, fecha_cheque DATE, banco VARCHAR(80), beneficiario VARCHAR(80)," +
                "  numero_tarjeta VARCHAR(30), nombre_titular VARCHAR(80), fecha_vencimiento DATE," +
                "  codigo_seguridad VARCHAR(10), limite_credito DECIMAL(12,2), saldo_disponible DECIMAL(12,2)," +
                "  emisor VARCHAR(60), tipo_tarjeta VARCHAR(20), banco_asociado VARCHAR(80)," +
                "  numero_cuenta VARCHAR(40), saldo_cuenta DECIMAL(12,2)," +
                "  CONSTRAINT fk_mp_pago FOREIGN KEY (id_pago) REFERENCES pago(id_pago))");

            crearSubtablaMedio(st, "efectivo", "");
            crearSubtablaMedio(st, "moneda_extranjera",
                "  tipo_moneda VARCHAR(20),");
            crearSubtablaMedio(st, "cheque_propio",
                "  numero_cheque INT, fecha DATE, banco VARCHAR(80), beneficiario VARCHAR(80),");
            crearSubtablaMedio(st, "cheque_tercero",
                "  numero_cheque INT, fecha DATE, banco VARCHAR(80), beneficiario VARCHAR(80),");
            crearSubtablaMedio(st, "tarjeta_de_credito",
                "  numero_tarjeta VARCHAR(30), nombre_titular VARCHAR(80), fecha_vencimiento DATE," +
                "  codigo_seguridad VARCHAR(10), limite_credito DECIMAL(12,2)," +
                "  saldo_disponible DECIMAL(12,2), emisor VARCHAR(60),");
            crearSubtablaMedio(st, "tarjeta_de_debito",
                "  numero_tarjeta VARCHAR(30), nombre_titular VARCHAR(80), fecha_vencimiento DATE," +
                "  codigo_seguridad VARCHAR(10), tipo_tarjeta VARCHAR(20), banco_asociado VARCHAR(80)," +
                "  numero_cuenta VARCHAR(40), saldo_cuenta DECIMAL(12,2),");

            for (String col : new String[]{
                    "tipo_moneda", "numero_cheque", "fecha_cheque", "banco", "beneficiario",
                    "numero_tarjeta", "nombre_titular", "fecha_vencimiento", "codigo_seguridad",
                    "limite_credito", "saldo_disponible", "emisor", "tipo_tarjeta",
                    "banco_asociado", "numero_cuenta", "saldo_cuenta"}) {
                st.execute("ALTER TABLE medio_de_pago DROP COLUMN IF EXISTS " + col);
            }

            migrarSubclasesResponsable(st);

            st.execute("ALTER TABLE responsable_de_pago DROP COLUMN IF EXISTS nro_documento");
            st.execute("ALTER TABLE responsable_de_pago DROP COLUMN IF EXISTS tipo_documento");

            st.execute("ALTER TABLE huesped ADD COLUMN IF NOT EXISTS id_estadia INT");
            st.execute("ALTER TABLE factura ADD COLUMN IF NOT EXISTS id_estadia INT");
            st.execute("ALTER TABLE factura ADD COLUMN IF NOT EXISTS id_responsable INT");
            st.execute("ALTER TABLE persona_fisica ADD COLUMN IF NOT EXISTS id_huesped INT");

            moverIdHuespedAPersonaFisica(st);

            agregarFk(st, "huesped", "fk_hue_est", "id_estadia", "estadia(id_estadia)");
            agregarFk(st, "factura", "fk_fac_est", "id_estadia", "estadia(id_estadia)");
            agregarFk(st, "factura", "fk_fac_resp", "id_responsable", "responsable_de_pago(id_responsable)");
            agregarFk(st, "persona_fisica", "fk_pf_hue", "id_huesped", "huesped(id_huesped)");

            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM responsable_de_pago")) {
                rs.next();
                if (rs.getInt(1) == 0) sembrarCU14(st);
            }
            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM conserje")) {
                rs.next();
                if (rs.getInt(1) == 0) sembrarBasicos(st);
            }
            corregirCuitDePrueba(st);
            sembrarResponsablesAdicionales(st);
            vincularFacturasConResponsable(st);
            st.execute("ALTER TABLE factura DROP COLUMN IF EXISTS cuit_responsable");

            System.out.println("Base de datos lista (todas las tablas).");

        } catch (SQLException e) {
            throw new RuntimeException("Error inicializando la base: " + e.getMessage(), e);
        }
    }

    private static void crearSubtablaMedio(Statement st, String tabla, String propias) throws SQLException {
        st.execute(
            "CREATE TABLE IF NOT EXISTS " + tabla + " (" +
            "  id_medio_de_pago INT PRIMARY KEY," + propias +
            "  CONSTRAINT fk_" + tabla + "_medio FOREIGN KEY (id_medio_de_pago)" +
            "  REFERENCES medio_de_pago(id_medio_de_pago))");
    }

    private static boolean existeColumna(Statement st, String tabla, String columna) throws SQLException {
        try (ResultSet rs = st.executeQuery(
                "SELECT 1 FROM information_schema.columns " +
                "WHERE table_schema = 'public' AND table_name = '" + tabla + "' " +
                "AND column_name = '" + columna + "'")) {
            return rs.next();
        }
    }

    private static void migrarSubclasesResponsable(Statement st) throws SQLException {
        if (!existeColumna(st, "responsable_de_pago", "razon_social")) return;

        int pj = st.executeUpdate(
            "INSERT INTO persona_juridica (id_responsable, razon_social) " +
            "SELECT id_responsable, razon_social FROM responsable_de_pago WHERE tipo = 'JURIDICA' " +
            "ON CONFLICT (id_responsable) DO NOTHING");
        int pf = st.executeUpdate(
            "INSERT INTO persona_fisica (id_responsable, nombre, apellido) " +
            "SELECT id_responsable, nombre, apellido FROM responsable_de_pago WHERE tipo = 'FISICA' " +
            "ON CONFLICT (id_responsable) DO NOTHING");

        st.execute("ALTER TABLE responsable_de_pago DROP COLUMN IF EXISTS razon_social");
        st.execute("ALTER TABLE responsable_de_pago DROP COLUMN IF EXISTS nombre");
        st.execute("ALTER TABLE responsable_de_pago DROP COLUMN IF EXISTS apellido");

        if (pj + pf > 0) {
            System.out.println("Subclases migradas: " + pj + " juridica(s), " + pf + " fisica(s).");
        }
    }

    private static void moverIdHuespedAPersonaFisica(Statement st) throws SQLException {
        if (!existeColumna(st, "responsable_de_pago", "id_huesped")) return;

        st.executeUpdate(
            "UPDATE persona_fisica pf SET id_huesped = r.id_huesped " +
            "FROM responsable_de_pago r " +
            "WHERE r.id_responsable = pf.id_responsable AND r.id_huesped IS NOT NULL");
        st.execute("ALTER TABLE responsable_de_pago DROP COLUMN id_huesped");
    }

    private static void agregarFk(Statement st, String tabla, String nombre,
                                  String columna, String referencia) throws SQLException {
        boolean existe;
        try (ResultSet rs = st.executeQuery(
                "SELECT 1 FROM information_schema.table_constraints " +
                "WHERE constraint_schema = 'public' AND table_name = '" + tabla + "' " +
                "AND constraint_name = '" + nombre + "'")) {
            existe = rs.next();
        }
        if (!existe) {
            st.execute("ALTER TABLE " + tabla + " ADD CONSTRAINT " + nombre +
                       " FOREIGN KEY (" + columna + ") REFERENCES " + referencia);
        }
    }

    private static void vincularFacturasConResponsable(Statement st) throws SQLException {
        if (!existeColumna(st, "factura", "cuit_responsable")) return;

        int vinculadas = st.executeUpdate(
            "UPDATE factura f SET id_responsable = r.id_responsable " +
            "FROM responsable_de_pago r " +
            "WHERE f.id_responsable IS NULL AND r.cuit = f.cuit_responsable");
        if (vinculadas > 0) {
            System.out.println("Facturas vinculadas al responsable: " + vinculadas + ".");
        }
    }

    private static void sembrarCU14(Statement st) throws SQLException {
        st.execute("INSERT INTO direccion (id_direccion, calle, numero, departamento, piso, cod_postal, localidad, provincia, pais) VALUES " +
            "(1,'SAN MARTIN','1234','','','3000','SANTA FE','SANTA FE','ARGENTINA')," +
            "(2,'URQUIZA','850','','2','3100','PARANA','ENTRE RIOS','ARGENTINA')," +
            "(3,'9 DE JULIO','4567','B','5','3000','SANTA FE','SANTA FE','ARGENTINA')," +
            "(4,'RIVADAVIA','333','','','3000','SANTA FE','SANTA FE','ARGENTINA')," +
            "(5,'BELGRANO','2020','','1','3000','SANTA FE','SANTA FE','ARGENTINA')");

        st.execute("INSERT INTO responsable_de_pago (id_responsable, tipo, estado, cuit, telefono, id_direccion) VALUES " +
            "(1,'JURIDICA','ACTIVO','30-12345678-1','0342-4550000',1)," +
            "(2,'JURIDICA','ACTIVO','30-99887766-7','0343-4231122',2)," +
            "(3,'JURIDICA','ACTIVO','33-44556677-8','0342-4889900',3)," +
            "(4,'FISICA','ACTIVO','20-31222333-4','0342-4567788',4)," +
            "(5,'FISICA','ACTIVO','20-28444555-5','0342-4551234',5)");

        st.execute("INSERT INTO persona_juridica (id_responsable, razon_social) VALUES " +
            "(1,'HOTELERIA DEL LITORAL S.A.')," +
            "(2,'TURISMO PARANA S.R.L.')," +
            "(3,'CONSULTORA DEL CENTRO S.A.S.') " +
            "ON CONFLICT (id_responsable) DO NOTHING");

        st.execute("INSERT INTO persona_fisica (id_responsable, nombre, apellido) VALUES " +
            "(4,'JUAN','PEREZ')," +
            "(5,'JOSE','PEPE') " +
            "ON CONFLICT (id_responsable) DO NOTHING");

        st.execute("INSERT INTO factura (id_factura, fecha, id_responsable, monto, tipo, estado) VALUES " +
            "(1001,'2025-03-10',1,154300.00,'A','PAGADA')," +
            "(1002,'2025-04-22',1,90560.00,'A','PENDIENTE')," +
            "(1003,'2025-05-15',3,211000.00,'B','PAGADA')");
        System.out.println("Datos del CU14 cargados.");
    }

    private static void sembrarResponsablesAdicionales(Statement st) throws SQLException {
        st.execute("INSERT INTO direccion (id_direccion, calle, numero, departamento, piso, cod_postal, localidad, provincia, pais) VALUES " +
            "(6,'BV. GALVEZ','1425','','','3000','SANTA FE','SANTA FE','ARGENTINA')," +
            "(7,'25 DE MAYO','2380','A','3','3000','SANTA FE','SANTA FE','ARGENTINA')," +
            "(8,'AVELLANEDA','765','','','3000','SANTA FE','SANTA FE','ARGENTINA')," +
            "(9,'LOPEZ Y PLANES','1190','','2','3000','SANTA FE','SANTA FE','ARGENTINA')," +
            "(10,'RUTA 168 KM 4','S/N','','','3000','SANTA FE','SANTA FE','ARGENTINA')," +
            "(11,'MITRE','3345','B','1','3100','PARANA','ENTRE RIOS','ARGENTINA')," +
            "(12,'SAN LORENZO','990','','','2000','ROSARIO','SANTA FE','ARGENTINA')," +
            "(13,'ITUZAINGO','540','','','3000','SANTA FE','SANTA FE','ARGENTINA')," +
            "(14,'CRESPO','2210','C','4','3000','SANTA FE','SANTA FE','ARGENTINA')," +
            "(15,'ALBERDI','1680','','','3000','SANTA FE','SANTA FE','ARGENTINA') " +
            "ON CONFLICT (id_direccion) DO NOTHING");

        st.execute("INSERT INTO responsable_de_pago (id_responsable, tipo, estado, cuit, telefono, id_direccion) VALUES " +
            "(6,'JURIDICA','ACTIVO','30-61234509-7','0342-4551100',6)," +
            "(7,'JURIDICA','ACTIVO','33-70112233-5','0343-4229090',7)," +
            "(8,'JURIDICA','ACTIVO','30-58990011-8','0342-4562200',8)," +
            "(9,'JURIDICA','ACTIVO','33-69801122-5','0342-4573311',9)," +
            "(10,'JURIDICA','ACTIVO','30-62445566-1','0342-4584422',10)," +
            "(11,'JURIDICA','ACTIVO','34-55667788-5','0343-4235533',11)," +
            "(12,'JURIDICA','ACTIVO','30-57889900-2','0341-4246644',12)," +
            "(13,'FISICA','ACTIVO','27-30455678-7','0342-4597755',13)," +
            "(14,'FISICA','ACTIVO','20-29334455-9','0342-4608866',14)," +
            "(15,'FISICA','ACTIVO','27-33221144-2','0342-4619977',15) " +
            "ON CONFLICT (id_responsable) DO NOTHING");

        st.execute("INSERT INTO persona_juridica (id_responsable, razon_social) VALUES " +
            "(6,'ESTANCIA LOS ALAMOS S.A.')," +
            "(7,'VIAJES DEL PARANA S.R.L.')," +
            "(8,'CONSTRUCTORA SAN JERONIMO S.A.')," +
            "(9,'LABORATORIOS COSTA S.A.S.')," +
            "(10,'TRANSPORTE RIO SALADO S.R.L.')," +
            "(11,'AGROPECUARIA EL PEPE S.A.')," +
            "(12,'SEGUROS DEL LITORAL S.A.') " +
            "ON CONFLICT (id_responsable) DO NOTHING");

        st.execute("INSERT INTO persona_fisica (id_responsable, nombre, apellido) VALUES " +
            "(13,'MARIA','GOMEZ')," +
            "(14,'CARLOS','PEPETTI')," +
            "(15,'LUCIA','FERNANDEZ') " +
            "ON CONFLICT (id_responsable) DO NOTHING");

        st.execute("INSERT INTO factura (id_factura, fecha, id_responsable, monto, tipo, estado) VALUES " +
            "(1004,'2025-06-02',6,88400.00,'A','PAGADA')," +
            "(1005,'2025-07-19',9,132750.00,'A','PENDIENTE')," +
            "(1006,'2025-08-08',6,45900.00,'B','PAGADA') " +
            "ON CONFLICT (id_factura) DO NOTHING");
    }

    private static void corregirCuitDePrueba(Statement st) throws SQLException {
        String[][] cambios = {
            {"30-12345678-9", "30-12345678-1"},
            {"30-99887766-5", "30-99887766-7"},
            {"27-33445566-8", "33-44556677-8"},
            {"20-28444555-1", "20-28444555-5"},
            {"30-55666777-2", "30-55666777-9"}
        };
        boolean hayCuitEnFactura = existeColumna(st, "factura", "cuit_responsable");
        int corregidos = 0;
        for (String[] c : cambios) {
            corregidos += st.executeUpdate(
                "UPDATE responsable_de_pago SET cuit = '" + c[1] + "' WHERE cuit = '" + c[0] + "'");
            if (hayCuitEnFactura) {
                st.executeUpdate(
                    "UPDATE factura SET cuit_responsable = '" + c[1] + "' WHERE cuit_responsable = '" + c[0] + "'");
            }
        }
        if (corregidos > 0) {
            System.out.println("CUIT de prueba corregidos: " + corregidos + " responsable(s).");
        }
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
