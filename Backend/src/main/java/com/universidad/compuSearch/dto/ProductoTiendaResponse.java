package com.universidad.compusearch.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
