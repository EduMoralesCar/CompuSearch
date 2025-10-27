package com.universidad.compusearch.entity;

/**
 * Enum que representa los posibles estados de una solicitud dentro del sistema.
 * <p>
 * Los estados permiten identificar el progreso o resultado del proceso de evaluación
 * de una solicitud realizada por un usuario o empleado.
 * </p>
 *
 * <ul>
 *   <li>{@code OBSERVACION}: La solicitud requiere correcciones o información adicional.</li>
 *   <li>{@code RECHAZADO}: La solicitud fue evaluada y no aprobada.</li>
 *   <li>{@code ACEPTADO}: La solicitud fue aprobada exitosamente.</li>
 * </ul>
 *
 * <p>
 * Este enum puede utilizarse en entidades relacionadas con procesos de revisión,
 * validación o aprobación de solicitudes.
 * </p>
 *
 * @author Jesus
 * @version 1.0
 */
public enum EstadoSolicitud {
    /** La solicitud presenta observaciones o requiere ajustes. */
    OBSERVACION,

    /** La solicitud ha sido rechazada. */
    RECHAZADO,

    /** La solicitud ha sido aceptada. */
    ACEPTADO
}
