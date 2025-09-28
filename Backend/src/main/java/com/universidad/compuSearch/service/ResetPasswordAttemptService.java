package com.universidad.compuSearch.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class ResetPasswordAttemptService {

    private final int MAX_ATTEMPTS = 3;
    private final long BLOCK_DURATION_MILLIS = Duration.ofMinutes(30).toMillis();

    private final Map<String, Attempt> resetAttemptsCache = new ConcurrentHashMap<>();

    public void requestSucceeded(String email) {
        resetAttemptsCache.remove(email);
    }

    public void requestFailed(String email) {
        Attempt attempt = resetAttemptsCache.getOrDefault(email, new Attempt(0, Instant.now().toEpochMilli()));
        attempt.count++;
        attempt.lastAttemptTime = Instant.now().toEpochMilli();
        resetAttemptsCache.put(email, attempt);
    }

    public boolean isBlocked(String email) {
        Attempt attempt = resetAttemptsCache.get(email);
        if (attempt == null) return false;

        if (attempt.count >= MAX_ATTEMPTS) {
            long elapsed = Instant.now().toEpochMilli() - attempt.lastAttemptTime;
            if (elapsed < BLOCK_DURATION_MILLIS) return true;
            else resetAttemptsCache.remove(email);
        }
        return false;
    }

    private static class Attempt {
        int count;
        long lastAttemptTime;

        Attempt(int count, long lastAttemptTime) {
            this.count = count;
            this.lastAttemptTime = lastAttemptTime;
        }
    }
}

