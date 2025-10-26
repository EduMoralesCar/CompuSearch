package com.universidad.compusearch.service;

import org.springframework.stereotype.Service;

@Service
public class ChangeEmailService extends AttemptService{
    public ChangeEmailService(){
        super(3, 60 * 24);
    }
}
