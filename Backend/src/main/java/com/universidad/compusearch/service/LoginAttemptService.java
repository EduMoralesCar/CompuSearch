package com.universidad.compusearch.service;

import org.springframework.stereotype.Service;

// Servicio de intentos de login
@Service
public class LoginAttemptService extends AttemptService {
    public LoginAttemptService() {
        super(5, 15);
    }
}