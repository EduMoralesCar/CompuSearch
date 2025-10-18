package com.universidad.compusearch.service;

import org.springframework.stereotype.Service;

// Servicio de intentos de reseto de contraseña
@Service
public class ResetPasswordAttemptService extends AttemptService {
    public ResetPasswordAttemptService() {
        super(3, 30);
    }
}