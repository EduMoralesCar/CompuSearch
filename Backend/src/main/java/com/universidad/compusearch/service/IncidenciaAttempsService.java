package com.universidad.compusearch.service;

import org.springframework.stereotype.Service;

@Service
public class IncidenciaAttempsService extends AttemptService {

    public IncidenciaAttempsService() {
        super(3, 60 * 24);
    }
}
