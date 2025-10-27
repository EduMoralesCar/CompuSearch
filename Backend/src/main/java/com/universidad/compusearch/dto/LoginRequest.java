package com.universidad.compusearch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO utilizado para manejar las solicitudes de inicio de sesión.
 * 
 * Contiene los campos necesarios para autenticar a un usuario.
 */
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

    @NotNull(message = "El campo recordar es obligatorio")
    private Boolean recordar;
}
