package com.universidad.compusearch.service;

import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService extends AttemptService {

    public LoginAttemptService() {
        super(5, 15);
    }
}
