package com.universidad.compusearch.entity;

/**
 * Enumeración que define los distintos tipos de usuarios
 * que pueden existir dentro del sistema.
 *
 * Cada tipo de usuario tiene diferentes niveles de acceso y
 * responsabilidades dentro de la plataforma.
 *
 * <ul>
 * <li>{@link #USUARIO}: Usuario general que utiliza la aplicación.</li>
 * <li>{@link #EMPLEADO}: Personal interno que gestiona operaciones o
 * soporte.</li>
 * <li>{@link #TIENDA}: Representante o cuenta vinculada a una tienda.</li>
 * </ul>
 *
 */
public enum TipoUsuario {
    /** Usuario general del sistema. */
    USUARIO,

    /** Empleado con permisos administrativos o de soporte. */
    EMPLEADO,

    /** Representante o entidad asociada a una tienda. */
    TIENDA
}
