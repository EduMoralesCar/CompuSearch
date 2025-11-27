package com.universidad.compusearch.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
