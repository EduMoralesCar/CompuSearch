package com.universidad.compusearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO utilizado para devolver mensajes simples en las respuestas HTTP,
 * como confirmaciones o notificaciones de error.
 *
 * Ejemplo:
 * {
 *   "message": "Contraseña actualizada correctamente"
 * }
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MessageResponse {
    private String message;
}
