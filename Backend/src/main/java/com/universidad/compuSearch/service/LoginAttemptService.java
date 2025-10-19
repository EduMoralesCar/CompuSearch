package com.universidad.compusearch.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.util.Attempt;

@Service
public class LoginAttemptService {

    private static final Logger logger = LoggerFactory.getLogger(LoginAttemptService.class);

    private final int MAX_ATTEMPTS = 5;
    private final long BLOCK_DURATION_MILLIS = Duration.ofMinutes(15).toMillis();

    private final Map<String, Attempt> attemptsCache = new ConcurrentHashMap<>();

    // Remueve los intentos del cache
    public void loginSucceeded(String identificador) {
        logger.info("Intentos de login eliminados para email o usuario: {}", identificador);
        attemptsCache.remove(identificador);
    }

    // Si el login falla actualiza los intentos
    public void loginFailed(String identificador) {
        Attempt previous = attemptsCache.getOrDefault(identificador, new Attempt(0, Instant.now().toEpochMilli()));
        Attempt updated = new Attempt(previous.getCount() + 1, Instant.now().toEpochMilli());

        attemptsCache.put(identificador, updated);
        logger.warn("Intento de login fallido para email o usuario {}. Total intentos: {}", identificador, updated.getCount());
    }

    // Valida si el usuario está bloqueado
    public boolean isBlocked(String identificador) {
        Attempt attempt = attemptsCache.get(identificador);
        if (attempt == null) return false;

        if (attempt.getCount() >= MAX_ATTEMPTS) {
            long elapsed = Instant.now().toEpochMilli() - attempt.getLastAttemptTime();
            if (elapsed < BLOCK_DURATION_MILLIS) {
                logger.warn("Email o Usuario {} bloqueado. Tiempo restante: {} minutos", identificador, (BLOCK_DURATION_MILLIS - elapsed) / 60000);
                return true;
            } else {
                logger.info("Bloqueo expirado para email: {}", identificador);
                attemptsCache.remove(identificador);
            }
        }

        return false;
    }
}