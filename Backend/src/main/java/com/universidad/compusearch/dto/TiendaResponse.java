package com.universidad.compusearch.dto;

import java.util.List;

import com.universidad.compusearch.entity.Etiqueta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TiendaResponse {
    private String nombre;
    private String descripcion;
    private String telefono;
    private String direccion;
    private String logo;
    private String urlPagina;
    private List<Etiqueta> etiquetas;
}
