package com.universidad.compusearch.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UltimaTiendaResponse {
    private Long idTienda;
    private String nombreTienda;
    private boolean verificado;
    private LocalDateTime fechaRegistro;
}
