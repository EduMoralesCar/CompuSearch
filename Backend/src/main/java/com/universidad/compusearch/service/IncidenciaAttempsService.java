package com.universidad.compusearch.service;

import org.springframework.stereotype.Service;

/**
 * Servicio encargado de controlar los intentos de registro o envío de incidencias
 * dentro del sistema.
 * 
 * Extiende {@link AttemptService} para aprovechar la lógica genérica de control
 * de intentos fallidos y bloqueos temporales. Este servicio ayuda a prevenir
 * el abuso del sistema de incidencias mediante el bloqueo temporal de usuarios
 * que exceden el número máximo permitido de intentos.
 *
 * Configuración:
 * <ul>
 *   <li><b>Máximo de intentos:</b> 3</li>
 *   <li><b>Duración del bloqueo:</b> 24 horas (1440 minutos)</li>
 * </ul>
 *
 * Ejemplo de uso:
 * <pre>{@code
 * String key = "incidencia:" + idUsuario;
 *
 * if (incidenciaAttempsService.isBlocked(key)) {
 *     throw TooManyAttemptsException.incidencia();
 * }
 *
 * incidenciaAttempsService.fail(key);
 * }</pre>
 *
 * 
 * Puede integrarse en controladores o servicios relacionados con la gestión de
 * incidencias, reportes o solicitudes de soporte técnico.
 * 
 *
 * @see AttemptService
 * @see com.universidad.compusearch.exception.TooManyAttemptsException
 */
@Service
public class IncidenciaAttempsService extends AttemptService {

    /**
     * Constructor que configura el servicio con un límite de 3 intentos fallidos
     * antes de aplicar un bloqueo temporal de 24 horas.
     */
    public IncidenciaAttempsService() {
        super(3, 60 * 24);
    }
}
