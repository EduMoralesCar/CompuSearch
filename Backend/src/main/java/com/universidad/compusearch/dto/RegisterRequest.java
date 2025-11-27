package com.universidad.compusearch.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email es inválido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
        message = "La contraseña debe tener al menos 8 caracteres, incluyendo mayúscula, minúscula, número y carácter especial"
    )
    private String contrasena;

    @NotBlank(message = "El tipo de usuario es obligatorio")
    private String tipoUsuario;

    @NotBlank(message = "El dispositivo es obligatorio")
    private String dispositivo;
}
