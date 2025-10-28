package com.universidad.compusearch.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para la creación o actualización de una
 * {@link com.universidad.compusearch.entity.Categoria}.
 * 
 * <p>
 * Contiene los datos necesarios que deben ser proporcionados por el cliente
 * para crear o actualizar una categoría en el sistema.
 * </p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoriaRequest {

    /**
     * Nombre de la categoría.
     * <p>
     * Este campo es obligatorio y no puede estar vacío.
     * </p>
     */
    @NotBlank(message = "El nombre de la categoria es obligatorio")
    private String nombre;

    /**
     * Descripción de la categoría.
     * <p>
     * Este campo es obligatorio y no puede estar vacío.
     * </p>
     */
    @NotBlank(message = "La descripcion de la categoria es obligatorio")
    private String descripcion;

    /**
     * Nombre de la imagen asociada a la categoría.
     * <p>
     * Este campo es obligatorio y no puede estar vacío.
     * </p>
     */
    @NotBlank(message = "El nombre de la imagen es obligatorio")
    private String nombreImagen;
}
