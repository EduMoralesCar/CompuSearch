package com.universidad.compusearch.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO utilizado para representar la respuesta de una incidencia.
 * Contiene la información básica del reporte realizado por un usuario.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IncidenciaRequest {

    @NotBlank(message = "El id de usuario es obligatorio")
    private Long idUsuario;

    @NotBlank(message = "El titulo es obligatorio")
    private String titulo;

    @NotBlank(message = "La descripcion es obligatorio")
    private String descripcion;
}
