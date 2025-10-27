package com.universidad.compusearch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO utilizado para restablecer la contraseña del usuario.
 * <p>
 * Contiene el token de verificación y la nueva contraseña que cumple con los
 * requisitos de seguridad.
 * </p>
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResetPasswordRequest {

    /** Token de validación enviado al usuario (por correo o enlace). */
    @NotBlank(message = "El token es obligatorio")
    private String token;

    /** Nueva contraseña del usuario con validación de complejidad. */
    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
        message = "La contraseña debe tener al menos 8 caracteres, incluyendo mayúscula, minúscula, número y carácter especial"
    )
    private String contrasena;
}
