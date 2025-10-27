package com.universidad.compusearch.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Clase base para excepciones personalizadas en la aplicación.
 *
 * Extiende {@link RuntimeException} y añade información adicional
 * como código de estado HTTP y un código único para identificar la excepción.
 * Todas las excepciones específicas de la aplicación deberían extender esta clase.
 *
 * Ejemplo de uso:
 * <pre>
 *     throw new CustomException("Mensaje de error", 400, "ERROR_CODE");
 * </pre>
 */
@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {

    /** Código HTTP asociado a la excepción */
    private final int status;

    /** Código único que identifica la excepción */
    private final String code;

    /**
     * Constructor principal.
     *
     * @param message Mensaje descriptivo del error
     * @param status  Código HTTP asociado
     * @param code    Código único que identifica la excepción
     */
    public CustomException(String message, int status, String code) {
        super(message);
        this.status = status;
        this.code = code;
    }
}
