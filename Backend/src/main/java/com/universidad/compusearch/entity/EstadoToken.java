package com.universidad.compusearch.entity;

/**
 * Enum que representa los posibles estados de un token dentro del sistema de autenticación.
 * <p>
 * Este enumerado permite controlar la validez de los tokens generados para los usuarios,
 * facilitando la gestión de seguridad y la revocación de accesos cuando sea necesario.
 * </p>
 *
 * <ul>
 *   <li>{@code ACTIVO}: El token se encuentra vigente y puede ser utilizado para autenticar solicitudes.</li>
 *   <li>{@code EXPIRADO}: El token ha superado su tiempo de validez y ya no puede usarse.</li>
 *   <li>{@code REVOCADO}: El token ha sido invalidado manualmente por motivos de seguridad o cierre de sesión.</li>
 * </ul>
 *
 * <p>
 * Este enum suele emplearse junto con entidades relacionadas con la gestión de tokens JWT o sesiones seguras.
 * </p>
 *
 * @author Jesus
 * @version 1.0
 */
public enum EstadoToken {

    /** El token está activo y autorizado para su uso. */
    ACTIVO,

    /** El token ha caducado y ya no es válido. */
    EXPIRADO,

    /** El token ha sido revocado manualmente o por razones de seguridad. */
    REVOCADO
}
