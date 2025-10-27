package com.universidad.compusearch.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para representar una respuesta de error estandarizada en la API.
 * Incluye el estado HTTP, un mensaje descriptivo y detalles de validación si aplica.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private boolean success;
    private String message;
    private int status;
    private Map<String, String> errors;
}
