package com.universidad.compusearch.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

// Servicio auxiliar reutilizable para almacenar
// intentos en cache
@Slf4j
public class AttemptService {

    private final int maxAttempts;
    private final Cache<String, Attempt> cache;

    public AttemptService(int maxAttempts, long blockDurationMinutes) {
        this.maxAttempts = maxAttempts;
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(blockDurationMinutes, TimeUnit.MINUTES)
                .build();
    }

    // Cuando el usuario pasa el intento
    public void success(String key) {
        log.info("Intentos eliminados para: {}", key);
        cache.invalidate(key);
    }

    // Cuando el usuario falla el intento
    public void fail(String key) {
        Attempt attempt = cache.getIfPresent(key);
        if (attempt == null) {
            attempt = new Attempt(1);
        } else {
            attempt.increment();
        }
        cache.put(key, attempt);
        log.warn("Intento fallido para {}. Total intentos: {}", key, attempt.getCount());
    }

    // El usuario es bloqueado si falla los
    // maximos intentos permitidos
    public boolean isBlocked(String key) {
        Attempt attempt = cache.getIfPresent(key);
        if (attempt == null) return false;
        boolean blocked = attempt.getCount() >= maxAttempts;
        if (blocked) {
            log.warn("{} bloqueado. Intentos: {}", key, attempt.getCount());
        }
        return blocked;
    }

    // Clase auxiliar para los intentos
    @AllArgsConstructor
    @Getter
    private static class Attempt {
        private int count;

        public void increment() {
            this.count++;
        }
    }
}
