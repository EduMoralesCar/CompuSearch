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
public class ResetPasswordAttemptService {

    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordAttemptService.class);

    private final int MAX_ATTEMPTS = 3;
    private final long BLOCK_DURATION_MILLIS = Duration.ofMinutes(30).toMillis();

    private final Map<String, Attempt> resetAttemptsCache = new ConcurrentHashMap<>();

    // Remueve los intentos del cache
    public void requestSucceeded(String email) {
        logger.info("Intentos de reseteo eliminados para email: {}", email);
        resetAttemptsCache.remove(email);
    }

    // Si algo falla en el cambio actualiza el contador
    public void requestFailed(String email) {
        Attempt previous = resetAttemptsCache.getOrDefault(email, new Attempt(0, Instant.now().toEpochMilli()));
        Attempt updated = new Attempt(previous.getCount() + 1, Instant.now().toEpochMilli());

        resetAttemptsCache.put(email, updated);
        logger.warn("Intento fallido para email {}. Total intentos: {}", email, updated.getCount());
    }

    // Valida si el usuario está bloqueado
    public boolean isBlocked(String email) {
        Attempt attempt = resetAttemptsCache.get(email);
        if (attempt == null) return false;

        if (attempt.getCount() >= MAX_ATTEMPTS) {
            long elapsed = Instant.now().toEpochMilli() - attempt.getLastAttemptTime();
            if (elapsed < BLOCK_DURATION_MILLIS) {
                logger.warn("Email {} bloqueado por {} minutos restantes", email, (BLOCK_DURATION_MILLIS - elapsed) / 60000);
                return true;
            } else {
                logger.info("Bloqueo expirado para email: {}", email);
                resetAttemptsCache.remove(email);
            }
        }

        return false;
    }
}