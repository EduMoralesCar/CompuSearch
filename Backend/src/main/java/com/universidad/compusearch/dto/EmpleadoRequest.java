package com.universidad.compusearch.dto;

import com.universidad.compusearch.entity.Rol;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class EmpleadoRequest {

    @NotBlank(message = "El username no puede estar vacío.")
    @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres.")
    private String username;

    @Email(message = "El formato del email es inválido.")
    @NotBlank(message = "El email no puede estar vacío.")
    private String email;

    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    private String password;

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres.")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío.")
    @Size(max = 50, message = "El apellido no puede exceder los 50 caracteres.")
    private String apellido;

    @NotNull(message = "El rol no puede ser nulo.")
    private Rol rol;
}