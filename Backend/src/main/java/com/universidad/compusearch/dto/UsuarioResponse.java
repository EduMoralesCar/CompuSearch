package com.universidad.compusearch.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UsuarioResponse {
    private Long idUsuario;
    private String username;
    private String email;
    private boolean activo;
    private LocalDateTime fechaRegistro;
    private int cantidadBuilds;
    private int cantidadIncidentes;
    private int cantidadSolicitudes;
}
