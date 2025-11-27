package com.universidad.compusearch.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PlanResponse {

    private Long idPlan;
    private String nombre;
    private Integer duracionMeses;
    private BigDecimal precioMensual;
    private String descripcion;
    private String beneficios;
    private boolean activo; 
    private int suscripciones;
}