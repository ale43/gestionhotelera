package DTOS;

/**
 * Códigos de resultado que devuelven los controladores.
 *
 * La capa de negocio informa QUÉ pasó; el texto que ve el conserje lo arma la
 * capa de presentación (frontend/index.html). De esta forma los carteles del
 * enunciado no viven en el backend: si mañana cambia una leyenda, o el sistema
 * se traduce, no se toca ni el controlador ni el repositorio.
 */
public enum CodigoResultado {

    // --- CU14 / CU03: verificación previa a la baja ---
    PUEDE_ELIMINARSE,
    TIENE_FACTURAS,
    RESPONSABLE_NO_ENCONTRADO,

    // --- CU14: confirmación de la baja ---
    ELIMINADO,

    // --- CU01: autenticación ---
    AUTENTICADO,
    CREDENCIALES_INVALIDAS,

    // --- Administración de usuarios (conserjes) ---
    DATOS_INCOMPLETOS,
    NOMBRE_USUARIO_INVALIDO,
    PASSWORD_LONGITUD,
    PASSWORD_NUMEROS_CONSECUTIVOS,
    USUARIO_YA_EXISTE,
    USUARIO_NO_EXISTE,
    ULTIMO_USUARIO,
    USUARIO_CREADO,
    USUARIO_ELIMINADO,
    ERROR_INESPERADO
}
