package com.universidad.compusearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IncidenciaResponse {
    private Long idUsuario;
    private String titulo;
    private String descripcion;
}
