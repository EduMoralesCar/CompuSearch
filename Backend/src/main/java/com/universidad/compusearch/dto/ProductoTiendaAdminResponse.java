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
public class ProductoTiendaAdminResponse {
    
    private long idProductoTienda;
    private String nombre;
    private String marca;
    private String modelo;
    private String descripcion;
    private String categoria;
    private BigDecimal precio;
    private int stock;
    private String urlProducto;
    private String urlImagen;
    private boolean habilitado;
    private long idProductoApi;
    private int visitas;
    private int clicks;
    private int builds;
}
