package com.universidad.compusearch.service;

import org.springframework.stereotype.Service;

/**
 * Servicio que gestiona los intentos de restablecimiento de contraseña.
 * 
 * Extiende {@link AttemptService} para limitar la cantidad de solicitudes
 * fallidas de restablecimiento de contraseña por usuario, evitando el abuso del
 * sistema y posibles ataques de fuerza bruta.
 * 
 *
 * Configuración:
 * <ul>
 *   <li><b>Máximo de intentos:</b> 3</li>
 *   <li><b>Duración del bloqueo:</b> 30 minutos</li>
 * </ul>
 * 
 * Ejemplo de uso:
 * <pre>{@code
 * String key = "reset_password_" + emailUsuario;
 *
 * if (resetPasswordAttemptService.isBlocked(key)) {
 *     throw TooManyAttemptsException.password();
 * }
 *
 * resetPasswordAttemptService.fail(key);
 * }</pre>
 *
 * 
 * Este servicio se usa para controlar los intentos fallidos de restablecimiento
 * de contraseña, garantizando que un usuario no pueda realizar más de tres
 * solicitudes erróneas en un periodo de 30 minutos.
 * 
 * 
 *
 * @see AttemptService
 * @see com.universidad.compusearch.exception.TooManyAttemptsException
 */
@Service
public class ResetPasswordAttemptService extends AttemptService {

    /**
     * Constructor que configura el servicio con un límite de 3 intentos fallidos
     * antes de aplicar un bloqueo temporal de 30 minutos.
     */
    public ResetPasswordAttemptService() {
        super(3, 30);
    }
}
