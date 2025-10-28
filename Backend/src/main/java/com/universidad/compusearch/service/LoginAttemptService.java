package com.universidad.compusearch.service;

import org.springframework.stereotype.Service;

/**
 * Servicio que gestiona los intentos de inicio de sesión.
 * 
 * Permite controlar la cantidad de intentos fallidos de login por usuario,
 * bloqueando temporalmente a quienes excedan el límite definido.
 * 
 * 
 * 
 * Este servicio hereda de {@link AttemptService} y define:
 * <ul>
 *   <li>Límite máximo de intentos: 5</li>
 *   <li>Duración del bloqueo: 15 minutos</li>
 * </ul>
 * 
 * 
 */
@Service
public class LoginAttemptService extends AttemptService {

    /**
     * Constructor que inicializa el servicio con los valores de límite de intentos y duración del bloqueo.
     */
    public LoginAttemptService() {
        super(5, 15);
    }
}
