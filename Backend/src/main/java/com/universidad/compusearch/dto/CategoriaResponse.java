package com.universidad.compusearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoriaResponse {

    private String nombre;

    private String descripcion;

    private String nombreImagen;
}
