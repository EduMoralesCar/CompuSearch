package com.universidad.compusearch.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SusTiendaResponse {
    
    private String planActual;
    private LocalDateTime fechaInicioPlan;
    private LocalDateTime fechaFinPlan;
    private String planActivo;
}
