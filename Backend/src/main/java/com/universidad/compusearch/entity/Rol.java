package com.universidad.compusearch.entity;

/**
 * Enum que representa los diferentes roles asignables a los empleados del sistema.
 * 
 * Cada rol define el nivel de acceso y las responsabilidades que puede tener
 * un empleado dentro de la aplicación.
 * 
 * <ul>
 *   <li><b>ADMIN:</b> Tiene acceso total al sistema y puede gestionar usuarios, tiendas, y configuraciones.</li>
 *   <li><b>MONITOREO:</b> Supervisa la actividad de los usuarios y las tiendas, detectando irregularidades o incidencias.</li>
 *   <li><b>SOPORTE:</b> Brinda asistencia técnica o funcional a los usuarios y tiendas registradas.</li>
 * </ul>
 * 
 */
public enum Rol {
    ADMIN,
    MONITOREO,
    SOPORTE
}
