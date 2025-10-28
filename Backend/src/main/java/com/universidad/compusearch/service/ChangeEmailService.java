package com.universidad.compusearch.service;

import org.springframework.stereotype.Service;

/**
 * Servicio que controla los intentos de cambio de correo electrónico de los usuarios.
 * 
 * Extiende {@link AttemptService} para reutilizar la lógica de control de intentos,
 * estableciendo un límite de 3 intentos fallidos antes de aplicar un bloqueo temporal
 * de 24 horas (1440 minutos).
 * 
 *
 * 
 * Este servicio se utiliza en {@link com.universidad.compusearch.service.UsuarioService}
 * para evitar abusos en los cambios de dirección de correo electrónico,
 * protegiendo tanto la seguridad del usuario como la integridad del sistema.
 * 
 *
 * Configuración:
 * <ul>
 *   <li><b>Máximo de intentos:</b> 3</li>
 *   <li><b>Duración del bloqueo:</b> 24 horas</li>
 * </ul>
 *
 * Ejemplo de uso:
 * <pre>{@code
 * if (changeEmailService.isBlocked("changeEmail:15")) {
 *     throw TooManyAttemptsException.info();
 * }
 * changeEmailService.fail("changeEmail:15");
 * }</pre>
 *
 * @see AttemptService
 * @see com.universidad.compusearch.exception.TooManyAttemptsException
 */
@Service
public class ChangeEmailService extends AttemptService {

    /**
     * Constructor que inicializa el servicio con las configuraciones predefinidas
     * de número máximo de intentos (3) y duración del bloqueo (24 horas).
     */
    public ChangeEmailService() {
        super(3, 60 * 24);
    }
}
