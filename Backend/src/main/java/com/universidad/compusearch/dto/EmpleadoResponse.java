package com.universidad.compusearch.dto;

import java.time.LocalDateTime;

import com.universidad.compusearch.entity.Rol;

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
public class EmpleadoResponse {
    private Long idUsuario;
    private String username;
    private String email;
    private boolean activo;
    private LocalDateTime fechaRegistro;
    private String password;
    private String nombre;
    private String apellido;
    private Rol rol;
    private LocalDateTime fechaAsignacion;
}
