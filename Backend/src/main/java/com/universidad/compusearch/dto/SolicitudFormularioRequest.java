package com.universidad.compusearch.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO utilizado para recibir la información del formulario
 * cuando un usuario envía una solicitud para convertirse en tienda.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SolicitudFormularioRequest {

    /**
     * Contiene los datos del formulario en formato JSON.
     * Este campo es obligatorio y debe tener contenido válido.
     */
    @NotBlank(message = "Los datos del formulario son obligatorios")
    private String datosFormulario;
}
