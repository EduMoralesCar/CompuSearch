package com.universidad.compuSearch.dto;

import jakarta.validation.constraints.Email;
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

    @NotBlank(message = "Email obligatorio")
    @Email(message = "Email invalido")
    private String email;

    @NotBlank(message = "Contraseña obligatoria")
    private String contrasena;

    @NotBlank(message = "Dispositivo es obligatorio")
    private String dispositivo;
}
