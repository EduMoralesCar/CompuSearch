package com.universidad.compusearch.dto;

import java.time.LocalDateTime;

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
public class IncidenteResponse {
    private Long idIncidente;
    private String nombreUsuario;
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private boolean revisado;
}
