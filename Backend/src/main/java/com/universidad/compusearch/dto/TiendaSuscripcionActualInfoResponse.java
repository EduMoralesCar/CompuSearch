package com.universidad.compusearch.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TiendaSuscripcionActualInfoResponse {
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String estado;
    private String nombrePlan;
}
