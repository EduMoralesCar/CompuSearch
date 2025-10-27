package com.universidad.compusearch.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO utilizado para solicitar el restablecimiento de contraseña.
 * Contiene el correo electrónico del usuario y el dispositivo desde el cual realiza la solicitud.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ForgotPasswordRequest {
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Dispositivo es obligatorio")
    private String dispositivo;
}
