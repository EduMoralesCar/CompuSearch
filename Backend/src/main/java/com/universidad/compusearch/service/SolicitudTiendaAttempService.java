package com.universidad.compusearch.service;

import org.springframework.stereotype.Service;

/**
 * Servicio que gestiona los intentos de creación de solicitudes de tienda.
 * 
 * Extiende {@link AttemptService} para aplicar un control de frecuencia sobre
 * los intentos de registro de solicitudes por usuario, previniendo el abuso del
 * sistema y evitando el envío repetido en un corto período.
 * 
 *
 * Configuración:
 * <ul>
 *   <li><b>Máximo de intentos:</b> 1</li>
 *   <li><b>Duración del bloqueo:</b> 14 horas (840 minutos)</li>
 * </ul>
 *
 * Ejemplo de uso:
 * <pre>{@code
 * String key = "solicitud_key_" + idUsuario;
 *
 * if (solicitudTiendaAttempService.isBlocked(key)) {
 *     throw TooManyAttemptsException.solicitud();
 * }
 *
 * solicitudTiendaAttempService.fail(key);
 * }</pre>
 *
 * Este servicio se utiliza dentro de {@link com.universidad.compusearch.service.SolicitudTiendaService}
 * para validar que cada usuario solo pueda enviar una solicitud dentro del
 * periodo de bloqueo establecido.
 *
 * @see AttemptService
 * @see com.universidad.compusearch.service.SolicitudTiendaService
 * @see com.universidad.compusearch.exception.TooManyAttemptsException
 */
@Service
public class SolicitudTiendaAttempService extends AttemptService {

    /**
     * Constructor que configura el servicio con un límite de 1 intento por usuario
     * antes de aplicar un bloqueo temporal de 14 horas.
     */
    public SolicitudTiendaAttempService() {
        super(1, 14 * 60);
    }
}
