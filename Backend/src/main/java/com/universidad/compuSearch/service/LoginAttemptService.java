package com.universidad.compuSearch.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {

    private final int MAX_ATTEMPTS = 5;
    private final long BLOCK_DURATION_MILLIS = Duration.ofMinutes(15).toMillis();

    private final Map<String, Attempt> attemptsCache = new ConcurrentHashMap<>();

    public void loginSucceeded(String email) {
        attemptsCache.remove(email);
    }

    public void loginFailed(String email) {
        Attempt attempt = attemptsCache.getOrDefault(email, new Attempt(0, Instant.now().toEpochMilli()));
        attempt.count++;
        attempt.lastAttemptTime = Instant.now().toEpochMilli();
        attemptsCache.put(email, attempt);
    }

    public boolean isBlocked(String email) {
        Attempt attempt = attemptsCache.get(email);
        if (attempt == null) return false;

        if (attempt.count >= MAX_ATTEMPTS) {
            long elapsed = Instant.now().toEpochMilli() - attempt.lastAttemptTime;
            if (elapsed < BLOCK_DURATION_MILLIS) return true;
            else attemptsCache.remove(email);
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