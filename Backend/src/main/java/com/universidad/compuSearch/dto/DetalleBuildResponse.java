package com.universidad.compusearch.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DetalleBuildResponse {
    private long idProductoTienda;
    private String nombreProducto;
    private String nombreTienda;
    private int stock;
    private BigDecimal subTotal;
    private BigDecimal precio;
    private String urlProducto;
    private String categoria;
    private int cantidad;
    private List<DetalleAtributoResponse> detalles;
}
