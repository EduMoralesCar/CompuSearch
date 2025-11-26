package com.universidad.compusearch.dto;

import java.time.LocalDateTime;

import com.universidad.compusearch.entity.TiendaAPI;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TiendaDashboardResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private String telefono;
    private String direccion;
    private String urlPagina;
    private boolean verificado;
    private LocalDateTime fechaAfiliacion;

    private String logoBase64;

    private int totalProductos;
    private int totalEtiquetas;
    private int totalSuscripciones;
    
    private TiendaAPI tienda;

    private SusTiendaResponse plan;
}

