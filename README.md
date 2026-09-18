# Gestión Hotelera — Hotel Premier (Santa Fe)

Trabajo Práctico de **Diseño de Sistemas de Información** (UTN FRSF, 2025).
Implementación del **CU14 — Dar de baja Responsable de Pago**, con autenticación
de usuarios (CU01) y el modelo de datos completo del sistema persistido en una
base de datos relacional.

**Autor:** Alejandro Ferrero

---

## Tecnologías

- **Java 24** (proyecto Maven, NetBeans)
- **PostgreSQL 17 o superior** (acceso vía JDBC, driver `org.postgresql`)
- Backend HTTP con `com.sun.net.httpserver` (sin frameworks)
- Frontend web (HTML/CSS/JS) servido por el propio backend

## Cómo ejecutar

1. **Crear la base de datos** en pgAdmin4: clic derecho en *Databases* → *Create* →
   *Database…* → nombre: `gestion_hotelera`.
2. **Configurar la conexión.** Copiar `db.properties.example` como
   `db.properties` en la raíz del proyecto y completar la contraseña del usuario
   `postgres` de esa máquina:

   ```
   db.host=localhost
   db.puerto=5432
   db.base=gestion_hotelera
   db.usuario=postgres
   db.password=su_contraseña
   ```

   `db.properties` está excluido del control de versiones: cada máquina tiene el
   suyo y ninguna credencial viaja al repositorio. Como alternativa pueden usarse
   las variables de entorno `PGHOST`, `PGPORT`, `PGDATABASE`, `PGUSER` y
   `PGPASSWORD`, que tienen prioridad sobre el archivo.
3. **Ejecutar** el proyecto desde NetBeans (Run), o desde la consola con
   `mvn compile exec:java`. Al iniciar, `InicializadorBD` crea las 21 tablas y
   carga los datos de prueba si están vacías.
4. Abrir **http://localhost:8080** en el navegador.

Si falta la contraseña, el sistema no arranca y lo informa explícitamente en vez
de fallar con un error del driver.

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
- **CU03 — Buscar Responsable de Pago** (soporte): los dos criterios del
  enunciado (*Razón social* y *CUIT*), combinables y resueltos en SQL — no se
  trae la tabla entera a memoria.
  - *Razón social*: **contiene** (`LIKE '%texto%'`), sin distinguir mayúsculas
    ni acentos. **Desvío declarado**: el enunciado pide "empieza con"; se
    amplió a "contiene" por usabilidad, ya que el conserje puede no recordar
    cómo empieza la razón social (escribiendo `pepe` aparece
    `RESPONSABLE PEPE S.R.L.`). Volver al criterio literal es cambiar el
    patrón en `RepositorioResponsableBD.contiene()`.
  - *CUIT*: **empieza con**, tal cual el enunciado, comparando sólo dígitos:
    da igual escribirlo con o sin guiones.

  El Responsable de Pago se identifica **sólo por razón social y CUIT**: el
  tipo y número de documento pertenecen al **Huésped** (CU02/CU09/CU10/CU11),
  que es otra entidad.
- **CU14 — Dar de baja Responsable de Pago**: baja **lógica**
  (`estado = 'ELIMINADO'`), bloqueada si la firma tiene facturas asociadas.
  Carteles con el texto exacto de la especificación, cierre con cualquier tecla.
- **Validación de CUIT**: máscara `XX-XXXXXXXX-X`, prefijos válidos y dígito
  verificador (módulo 11).
- **Administración de usuarios**: alta con la regla de contraseña del CU01
  (mínimo 5 letras y 3 números no iguales ni consecutivos) y baja protegida.
  Las tres validaciones de la baja viven en el **backend**, no en la pantalla:
  no se puede eliminar el último usuario (`ULTIMO_USUARIO`), ni el usuario con
  el que se inició la sesión (`USUARIO_SESION_ACTUAL`), ni uno inexistente
  (`USUARIO_NO_EXISTE`). La pantalla además oculta el botón del propio usuario,
  pero es una comodidad visual: quien llame directamente al endpoint recibe el
  mismo rechazo.
- **Modelo de datos completo**: las 21 clases de entidad del diagrama de clases
  aprobado (más los 9 enums del diagrama y un décimo propio,
  `EstadoResponsable`) implementadas y mapeadas **una a una** a 21 tablas en
  PostgreSQL, con 23 claves foráneas. Los enums se persisten como VARCHAR: son
  listas cerradas validadas por el tipo en Java, no ameritan tabla de catálogo.

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
- **Los carteles viven en la capa de presentación**: ningún controlador ni
  repositorio arma texto de pantalla. Los controladores devuelven un
  `DTOS.CodigoResultado` (`PUEDE_ELIMINARSE`, `TIENE_FACTURAS`, `ELIMINADO`,
  `CREDENCIALES_INVALIDAS`, …) junto con los datos, y el catálogo `MENSAJES`
  de `frontend/index.html` compone la leyenda que ve el conserje. Cambiar un
  cartel —o traducir el sistema— no toca el backend.
- **Generalización (Class Table Inheritance)**: las dos jerarquías del diagrama
  se mapean con **una tabla por clase**. La PK de cada subtabla es a la vez FK
  a la tabla padre, de modo que la herencia queda visible en el DER:
  - `responsable_de_pago` → `persona_fisica` / `persona_juridica`
  - `medio_de_pago` → `efectivo`, `moneda_extranjera`, `cheque_propio`,
    `cheque_tercero`, `tarjeta_de_credito`, `tarjeta_de_debito`

  Los padres conservan una columna discriminadora `tipo`. Es redundante con la
  presencia de la fila en la subtabla, pero evita consultar seis tablas para
  saber contra cuál hacer el JOIN.
- **Configuración fuera del código**: `ConexionBD` no tiene datos de conexión
  escritos. Los lee de variables de entorno o de `db.properties`, que no se
  versiona. El mismo código corre en cualquier máquina sin recompilar y sin que
  ninguna credencial quede en el repositorio.

El diseño sigue los **diagramas de secuencia aprobados** por la cátedra: la
verificación de facturas la realiza `IRepositorioFacturas.existeFactura(idResponsable)`
invocada desde `ControladorResponsable.prepararBaja()`.

## Desvíos declarados respecto del diagrama de clases

El modelo físico reproduce el diagrama aprobado clase por clase. Las
diferencias que quedan son deliberadas y se documentan acá:

1. **`CUIT` está en `responsable_de_pago`, no en `persona_juridica`.** El
   diagrama se lo asigna únicamente a la persona jurídica, pero el CU03 pide
   buscar por CUIT sin distinguir el tipo y los carteles del CU14 dicen
   *"Los datos de razón social, CUIT"* para cualquier responsable. Dejarlo en
   la subclase dejaría a las personas físicas sin CUIT y el caso de uso no
   cerraría.
2. **`persona_fisica` tiene `nombre` y `apellido` propios.** El diagrama no le
   da atributos y la hace apuntar a `Huesped`, de donde saldría el nombre. Como
   el CU09 (alta de huésped) no forma parte de esta entrega, una persona física
   sin nombre propio no tendría qué mostrar en la grilla del CU03. La asociación
   del diagrama **sí está modelada**: `persona_fisica.id_huesped` es FK a
   `huesped` y la clase `PersonaFisica` expone `getIdHuesped()`; queda en NULL
   hasta que exista el CU09 que la cargue.
3. **`estado` en `responsable_de_pago`.** No está en el diagrama; es lo que
   permite la baja lógica que el CU14 exige. En el modelo de objetos es el enum
   `EstadoResponsable` (`ACTIVO` / `ELIMINADO`), coherente con `EstadoReserva`,
   `EstadoHabitacion` y `EstadoFactura`: la baja se hace con
   `responsable.darDeBaja()`, no escribiendo el literal a mano.
4. **Columna discriminadora `tipo`** en las dos tablas padre (ver arriba).
5. **`factura.fecha`.** El diagrama no la incluye; una factura sin fecha no es
   representable en el dominio real.
6. **`direccion.numero` y `.piso` son VARCHAR**, no `int`, para admitir valores
   como `1234 bis`, `S/N` o `PB`.
7. **Los importes son `DECIMAL(12,2)`**, no `float`. El punto flotante binario
   no representa exactamente los valores decimales y acumula error de redondeo
   en operaciones monetarias.
8. **Búsqueda por razón social "contiene"** en lugar de "empieza con" (ver la
   sección del CU03).
9. **No hay capa `Service`.** Los diagramas de secuencia muestran
   `Controlador → Service → Repositorio`. Acá el controlador invoca
   directamente la interfaz del repositorio: para el alcance de este CU la capa
   intermedia no agregaría comportamiento, sólo delegación.

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

## Limitaciones conocidas

- **Las contraseñas de los conserjes se guardan en texto plano** en la columna
  `conserje.password`. Es lo que modela el enunciado del CU01, pero en un
  sistema real irían con hash y sal (bcrypt o similar). Se documenta acá para
  que quede claro que es una decisión de alcance, no un descuido.
- **No hay sesión en el servidor.** El backend es sin estado: la pantalla
  informa qué usuario está logueado y el controlador lo usa para negarse a
  borrarlo. Es una validación de consistencia, no de autenticación; con
  sesiones o tokens el servidor no dependería de lo que declare el cliente.
- **La persistencia cubre los casos de uso entregados.** Las 21 clases del
  modelo de dominio están implementadas, pero sólo hay repositorios para
  `conserje`, `direccion`, `responsable_de_pago` (+ sus dos subtablas) y
  `factura`, que es lo que CU01, CU03 y CU14 necesitan. Las 15 tablas restantes
  existen con su estructura y sus claves foráneas, listas para los casos de uso
  que las usen.
