package com.universidad.compusearch.entity;

/**
 * Enumeración que define los diferentes tipos de tokens
 * utilizados dentro del sistema para la autenticación y seguridad.
 *
 * Cada tipo de token cumple una función específica en los
 * procesos de autenticación, renovación de sesión y recuperación
 * de credenciales.
 *
 * <ul>
 *   <li>{@link #REFRESH}: Token utilizado para renovar el acceso sin requerir autenticación completa.</li>
 *   <li>{@link #RESET}: Token temporal usado para la recuperación o restablecimiento de contraseñas.</li>
 *   <li>{@link #ACCESS}: Token de acceso principal para autenticar solicitudes dentro del sistema.</li>
 * </ul>
 *
 * @author Jesus
 * @version 1.0
 */
public enum TipoToken {
    /** Token utilizado para renovar el acceso sin autenticación completa. */
    REFRESH,

    /** Token usado para restablecer contraseñas. */
    RESET,

    /** Token principal de acceso del usuario. */
    ACCESS
}
