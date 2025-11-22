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
public class TiendaInfoResponse {
    private Long idUsuario;
    private String nombre;
    private String email;
    private boolean activo;
    private LocalDateTime fechaAfiliacion;
    private boolean verificado;
    private int suscripciones;
    private int etiquetas;
    private int productos;
}
