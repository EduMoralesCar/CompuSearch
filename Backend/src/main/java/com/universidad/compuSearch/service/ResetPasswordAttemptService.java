package com.universidad.compuSearch.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.universidad.compuSearch.util.Attempt;

@Service
public class ResetPasswordAttemptService {

    // Maximo intento para cambiar contraseña
    private final int MAX_ATTEMPTS = 3;
    // Tiempo de espera de 30 min
    private final long BLOCK_DURATION_MILLIS = Duration.ofMinutes(30).toMillis();

    private final Map<String, Attempt> resetAttemptsCache = new ConcurrentHashMap<>();

    // Remueve los intentos del cache
    public void requestSucceeded(String email) {
        resetAttemptsCache.remove(email);
    }

    // Si algo falla en el cambio acutliza el contador
    public void requestFailed(String email) {
        Attempt attempt = resetAttemptsCache.getOrDefault(email, new Attempt(0, Instant.now().toEpochMilli()));
        attempt.count++;
        attempt.lastAttemptTime = Instant.now().toEpochMilli();
        resetAttemptsCache.put(email, attempt);
    }

    // Valida si el usuario esta bloqueado
    public boolean isBlocked(String email) {

        // Si no hay intentos almacenados devuelve false
        Attempt attempt = resetAttemptsCache.get(email);
        if (attempt == null) return false;

        // Reseta los intentos del usuario si ya ha pasado el numero
        // de intentos permitidos
        if (attempt.count >= MAX_ATTEMPTS) {
            long elapsed = Instant.now().toEpochMilli() - attempt.lastAttemptTime;
            if (elapsed < BLOCK_DURATION_MILLIS) return true;
            else resetAttemptsCache.remove(email);
        }

        // Devuelve false si no ha llegado al maximo de intentos
        return false;
    }
}

