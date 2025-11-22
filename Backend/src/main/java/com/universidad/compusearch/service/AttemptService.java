package com.universidad.compusearch.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class AttemptService {

    private final int maxAttempts;
    private final Cache<String, Attempt> cache;

    // Constructor
    public AttemptService(int maxAttempts, long blockDurationMinutes) {
        this.maxAttempts = maxAttempts;
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(blockDurationMinutes, TimeUnit.MINUTES)
                .build();
    }

    // Intento exitoso
    public void success(String key) {
        if (key == null) return;
        log.info("Intentos eliminados para: {}", key);
        cache.invalidate(key);
    }

    // Intento incorrecto
    public void fail(String key) {
        if (key == null) return;
        Attempt attempt = cache.getIfPresent(key);
        if (attempt == null) {
            attempt = new Attempt(1);
        } else {
            attempt.increment();
        }
        cache.put(key, attempt);
        log.warn("Intento fallido para {}. Total intentos: {}", key, attempt.getCount());
    }

    // Verificar si esta bloqueado
    public boolean isBlocked(String key) {
        if (key == null) return false;
        Attempt attempt = cache.getIfPresent(key);
        if (attempt == null) return false;
        boolean blocked = attempt.getCount() >= maxAttempts;
        if (blocked) {
            log.warn("{} bloqueado. Intentos: {}", key, attempt.getCount());
        }
        return blocked;
    }

    // Clase auxiliar
    @AllArgsConstructor
    @Getter
    private static class Attempt {
        private int count;

        // contador
        public void increment() {
            this.count++;
        }
    }
}
