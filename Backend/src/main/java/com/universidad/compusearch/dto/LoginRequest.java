package com.universidad.compusearch.dto;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Email o Username obligatorio")
    private String identificador;

    @NotBlank(message = "Contraseña obligatoria")
    private String contrasena;

    @NotBlank(message = "Dispositivo es obligatorio")
    private String dispositivo;

    @NonNull
    private boolean recordar;
}