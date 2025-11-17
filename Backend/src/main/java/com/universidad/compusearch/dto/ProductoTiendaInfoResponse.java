package com.universidad.compusearch.dto;

import java.math.BigDecimal;

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
public class ProductoTiendaInfoResponse {
    private long idProductoTienda;
    private String nombre;
    private String categoria;
    private BigDecimal precio;
    private int stock;
    private String urlProducto;
    private String urlImagen;
    private boolean habilitado;
}
