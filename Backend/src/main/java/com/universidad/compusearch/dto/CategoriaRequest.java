package com.universidad.compusearch.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoriaRequest {

    @NotBlank(message = "El nombre de la categoria es obligatorio")
    private String nombre;

    @NotBlank(message = "La descripcion de la categoria es obligatorio")
    private String descripcion;

    @NotBlank(message = "El nombre de la imagen es obligatorio")
    private String nombreImagen;
}
