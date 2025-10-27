package com.universidad.compusearch.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO que representa la información de un producto
 * disponible en una tienda específica.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductoTiendaResponse {
    private long idProductoTienda;
    private String nombreProducto;
    private BigDecimal precio;
    private int stock;
    private String urlImagen;
    private String nombreTienda;
}
