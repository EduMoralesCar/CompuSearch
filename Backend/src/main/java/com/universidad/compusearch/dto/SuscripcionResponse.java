package com.universidad.compusearch.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.universidad.compusearch.entity.EstadoSuscripcion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuscripcionResponse {
    private Long idSuscripcion;
    private String nombrePlan;
    private EstadoSuscripcion estado;
    private BigDecimal precio;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin; 
}
