package com.universidad.compusearch.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO que representa la información detallada de un producto
 * disponible en una tienda, incluyendo atributos técnicos,
 * precios y datos de la tienda asociada.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductoInfoResponse {
    private String nombreProducto;
    private String marca;
    private String modelo;
    private String descripcion;
    private String urlImagen;
    private String urlProducto;
    private String nombreTienda;
    private int stock;
    private BigDecimal precio;
    private List<DetalleAtributoResponse> atributos;
}
