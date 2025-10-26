package com.universidad.compusearch.service;

import org.springframework.stereotype.Service;

@Service
public class SolicitudTiendaAttempService extends AttemptService{
    
    public SolicitudTiendaAttempService(){
        super(1, 14 * 60);
    }
}
