package com.universidad.compusearch.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PagoHistorialResponse {
    private Long id;
    private String nombrePlan;
    private BigDecimal precio;
    private String estadoPago;
    private LocalDateTime fechaPago;
    private LocalDateTime fechaInicioSuscripcion;
    private LocalDateTime fechaFinSuscripcion;
    private String transactionId;
}
