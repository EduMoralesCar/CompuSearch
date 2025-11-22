package com.universidad.compusearch.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Servicio genérico para gestionar intentos de acción (como inicio de sesión,
 * solicitudes o cambios de datos) con un límite máximo antes de aplicar un bloqueo temporal.
 * 
 * Utiliza una caché en memoria (basada en {@link CacheBuilder}) para almacenar
 * los intentos de cada usuario o clave, los cuales expiran automáticamente tras un periodo definido.
 *
 * 
 * Este servicio puede ser reutilizado por otros servicios como
 * {@link com.universidad.compusearch.service.SolicitudTiendaAttempService} o
 * {@link com.universidad.compusearch.service.ChangeEmailService}.
 *
 * Ejemplo de uso:
 * <pre>{@code
 * AttemptService attemptService = new AttemptService(3, 15);
 * if (attemptService.isBlocked("user_123")) {
 *     throw new TooManyAttemptsException();
 * }
 * attemptService.fail("user_123");
 * }</pre>
 *
 */
@Slf4j
public class AttemptService {

    private final int maxAttempts;
    private final Cache<String, Attempt> cache;

    /**
     * Crea una instancia del servicio de intentos.
     *
     * @param maxAttempts         número máximo de intentos permitidos antes de bloquear.
     * @param blockDurationMinutes duración del bloqueo en minutos después de que se alcancen los intentos máximos.
     */
    public AttemptService(int maxAttempts, long blockDurationMinutes) {
        this.maxAttempts = maxAttempts;
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(blockDurationMinutes, TimeUnit.MINUTES)
                .build();
    }

    /**
     * Registra un intento exitoso, eliminando cualquier registro previo de intentos fallidos.
     *
     * @param key identificador único del usuario o acción (por ejemplo: "login_user_5").
     */
    public void success(String key) {
        log.info("Intentos eliminados para: {}", key);
        cache.invalidate(key);
    }

    /**
     * Registra un intento fallido. Si el usuario ya tenía intentos anteriores, se incrementa el contador.
     *
     * @param key identificador único del usuario o acción.
     */
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

    /**
     * Verifica si el usuario o acción identificada por la clave se encuentra bloqueada.
     * <p>
     * Un usuario se considera bloqueado si el número de intentos registrados es
     * mayor o igual al máximo permitido.
     * </p>
     *
     * @param key identificador único del usuario o acción.
     * @return {@code true} si está bloqueado, {@code false} en caso contrario.
     */
    public boolean isBlocked(String key) {
        Attempt attempt = cache.getIfPresent(key);
        if (attempt == null) return false;
        boolean blocked = attempt.getCount() >= maxAttempts;
        if (blocked) {
            log.warn("{} bloqueado. Intentos: {}", key, attempt.getCount());
        }
        return blocked;
    }

    /**
     * Clase auxiliar que representa el conteo de intentos asociados a una clave.
     */
    @AllArgsConstructor
    @Getter
    private static class Attempt {
        private int count;

        /**
         * Incrementa el número de intentos registrados.
         */
        public void increment() {
            this.count++;
        }
    }
}
