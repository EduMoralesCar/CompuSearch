package com.universidad.compusearch.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO que representa un rango de precios mínimo y máximo
 * para filtrar o mostrar productos.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RangoPrecioResponse {
    private BigDecimal precioMin;
    private BigDecimal precioMax;
}
