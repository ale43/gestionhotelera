# Gestión Hotelera — Hotel Premier (Santa Fe)

Trabajo Práctico de **Diseño de Sistemas de Información** (UTN FRSF, 2025).
Implementación del **CU14 — Dar de baja Responsable de Pago**, con autenticación
de usuarios (CU01) y el modelo de datos completo del sistema persistido en una
base de datos relacional.

**Autor:** Alejandro Ferrero

---

## Tecnologías

- **Java 24** (proyecto Maven, NetBeans)
- **PostgreSQL 17** (acceso vía JDBC, driver `org.postgresql`)
- Backend HTTP con `com.sun.net.httpserver` (sin frameworks)
- Frontend web (HTML/CSS/JS) servido por el propio backend

## Cómo ejecutar

1. **Crear la base de datos** en pgAdmin4: clic derecho en *Databases* → *Create* →
   *Database…* → nombre: `gestion_hotelera`.
2. **Configurar la contraseña** del usuario `postgres` en
   `src/main/java/Infraestructura/ConexionBD.java` (constante `PASSWORD`).
3. **Ejecutar** el proyecto desde NetBeans (Run). Al iniciar, `InicializadorBD`
   crea las 13 tablas y carga los datos de prueba si están vacías.
4. Abrir **http://localhost:8080** en el navegador.

### Usuarios de prueba

| Usuario | Contraseña |
|---------|------------|
| admin   | admin123   |
| jperez  | clave456   |

Los usuarios se administran desde la pantalla "Usuarios del sistema" (alta/baja
contra la tabla `conserje`) o directamente en la base.

## Funcionalidad implementada

- **CU01 — Autenticar Usuario**: login validado contra la tabla `conserje`,
  con los mensajes de error del enunciado.
- **CU03 — Buscar Responsable de Pago** (soporte): búsqueda por razón social y
  CUIT con criterio "empieza con".
- **CU14 — Dar de baja Responsable de Pago**: baja **lógica**
  (`estado = 'ELIMINADO'`), bloqueada si la firma tiene facturas asociadas.
  Carteles idénticos a la especificación, cierre con cualquier tecla.
- **Validación de CUIT**: máscara `XX-XXXXXXXX-X`, prefijos válidos y dígito
  verificador (módulo 11).
- **Administración de usuarios**: alta con la regla de contraseña del CU01
  (mínimo 5 letras y 3 números no iguales ni consecutivos), baja protegida
  (no permite eliminar el último usuario ni la sesión actual).
- **Modelo de datos completo**: todas las entidades del diagrama de clases
  (17 clases + 9 enums) implementadas y mapeadas a tablas en PostgreSQL.

## Arquitectura y patrones

Recorrido de una petición:

```
Navegador → ServidorRest → Controlador → IRepositorio (interfaz) → RepositorioBD (JDBC) → PostgreSQL
                                ↓
                         Entidad → DTO → JSON
```

- **Repository**: interfaces `IRepositorioResponsable`, `IRepositorioFacturas`,
  `IRepositorioConserje` con implementaciones JDBC intercambiables.
- **Factory**: `RepositorioFactory` centraliza la creación de repositorios.
- **DTO**: la capa de presentación nunca expone entidades (ni contraseñas).
- **Controller (GRASP) sin estado**: re-consulta la entidad antes de mutarla.
- **Generalización**: `ResponsableDePago` (abstracta) → `PersonaFisica` /
  `PersonaJuridica`, mapeo a tabla única con columna discriminadora `tipo`.

El diseño sigue los **diagramas de secuencia aprobados** por la cátedra: la
verificación de facturas la realiza `IRepositorioFacturas.existeFactura(cuit)`
invocada desde `ControladorResponsable.prepararBaja()`.

## Estructura del proyecto

```
src/main/java/
├── Controlador/       ControladorResponsable, ControladorSesion
├── DTOS/              ResponsableDTO, ResultadoVerificacionDTO, ResultadoBajaDTO, ConserjeDTO
├── Entidades/         Modelo completo (ResponsableDePago, Factura, Habitacion, Reserva, ...)
├── Infraestructura/   ConexionBD, InicializadorBD, ServidorRest, Json
├── Repositorios/      Interfaces I... + implementaciones ...BD + RepositorioFactory
└── isi/deso/gestionhotelera/  Clase principal
frontend/
└── index.html         Interfaz web (login + CU03/CU14 + usuarios)
```
