package com.universidad.compusearch.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para registrar un nuevo usuario en el sistema.
 * <p>
 * Contiene los datos requeridos para crear una cuenta, con validaciones
 * que aseguran el formato correcto de los campos.
 * </p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterRequest {

    /** Nombre de usuario único. */
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    /** Correo electrónico válido. */
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email es inválido")
    private String email;

    /** Contraseña con requisitos de complejidad. */
    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
        message = "La contraseña debe tener al menos 8 caracteres, incluyendo mayúscula, minúscula, número y carácter especial"
    )
    private String contrasena;

    /** Tipo de usuario: USUARIO, EMPLEADO o TIENDA. */
    @NotBlank(message = "El tipo de usuario es obligatorio")
    private String tipoUsuario;

    /** Identificador del dispositivo desde el que se registra. */
    @NotBlank(message = "El dispositivo es obligatorio")
    private String dispositivo;
}
