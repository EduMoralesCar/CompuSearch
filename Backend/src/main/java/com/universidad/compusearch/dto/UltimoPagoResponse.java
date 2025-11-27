package com.universidad.compusearch.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UltimoPagoResponse {
    private Long idPago;
    private BigDecimal monto;
    private LocalDateTime fecha;
    private String estado;
    private String tiendaNombre;
}

