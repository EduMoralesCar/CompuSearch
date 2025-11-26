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
public class UltimoEmpleadoResponse {
    private Long idEmpleado;
    private String nombre;
    private String email;
    private LocalDateTime fechaRegistro;
}

