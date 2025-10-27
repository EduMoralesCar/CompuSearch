package com.universidad.compusearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO que representa la información básica de una categoría.
 * 
 * <p>Se utiliza para enviar datos de categorías al cliente,
 * incluyendo su nombre, descripción y la imagen asociada.</p>
 * 
 * <ul>
 *   <li><b>nombre:</b> Nombre de la categoría.</li>
 *   <li><b>descripcion:</b> Breve descripción de la categoría.</li>
 *   <li><b>nombreImagen:</b> Nombre del archivo de imagen que representa la categoría.</li>
 * </ul>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoriaResponse {

    /** Nombre de la categoría. */
    private String nombre;

    /** Descripción breve de la categoría. */
    private String descripcion;

    /** Nombre del archivo de imagen asociado a la categoría. */
    private String nombreImagen;
}
