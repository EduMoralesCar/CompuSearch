package com.universidad.compusearch.service;

import org.springframework.stereotype.Service;

@Service
public class ResetPasswordAttemptService extends AttemptService {

    public ResetPasswordAttemptService() {
        super(3, 30);
    }
}
