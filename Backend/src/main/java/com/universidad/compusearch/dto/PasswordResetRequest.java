package com.universidad.compusearch.dto;

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
public class PasswordResetRequest {
    
    @NotBlank(message = "La contraseña actual es obligatoria")
    private String actualConstrasena;

    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
        message = "La contraseña debe tener al menos 8 caracteres, incluyendo mayúscula, minúscula, número y carácter especial"
    )
    private String contrasenaNueva;

    @NotBlank(message = "La contraseña confirmada es obligatoria")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
        message = "La contraseña debe tener al menos 8 caracteres, incluyendo mayúscula, minúscula, número y carácter especial"
    )
    private String contrasenaConfirmada;
}
