package com.universidad.compuSearch.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.universidad.compuSearch.util.Attempt;

@Service
public class LoginAttemptService {

    // Maximos intentos
    private final int MAX_ATTEMPTS = 5;
    // Minutos bloqueado por fallar
    private final long BLOCK_DURATION_MILLIS = Duration.ofMinutes(15).toMillis();

    private final Map<String, Attempt> attemptsCache = new ConcurrentHashMap<>();

    // Remueve los intentos del cache
    public void loginSucceeded(String email) {
        attemptsCache.remove(email);
    }

    // Si el login falla actualiza los intentos
    public void loginFailed(String email) {
        Attempt attempt = attemptsCache.getOrDefault(email, new Attempt(0, Instant.now().toEpochMilli()));
        attempt.count++;
        attempt.lastAttemptTime = Instant.now().toEpochMilli();
        attemptsCache.put(email, attempt);
    }

    // Valida si el usuario esta bloqueado
    public boolean isBlocked(String email) {

        // Si no hay intentos almacenados devuelve false
        Attempt attempt = attemptsCache.get(email);
        if (attempt == null) return false;

        // Reseta los intentos del usuario si ya ha pasado el numero
        // de intentos permitidos
        if (attempt.count >= MAX_ATTEMPTS) {
            long elapsed = Instant.now().toEpochMilli() - attempt.lastAttemptTime;
            if (elapsed < BLOCK_DURATION_MILLIS) return true;
            else attemptsCache.remove(email);
        }

        // Devuelve false si no ha llegado al maximo de intentos
        return false;
    }

    
}