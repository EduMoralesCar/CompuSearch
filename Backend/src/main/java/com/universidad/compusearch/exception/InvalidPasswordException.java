package com.universidad.compusearch.exception;

/**
 * Excepción lanzada cuando la contraseña ingresada es incorrecta o inválida.
 *
 * <p>
 * Esta excepción extiende {@link CustomException} y utiliza un código de estado HTTP 401 (No autorizado).
 * </p>
 *
 * <p>
 * Código interno: <b>INVALID_PASSWORD</b>
 * Mensaje por defecto: "Contraseña incorrecta"
 * </p>
 */
public class InvalidPasswordException extends CustomException {

    /**
     * Constructor por defecto que inicializa la excepción con el mensaje y código de error predefinidos.
     */
    public InvalidPasswordException() {
        super("Contraseña incorrecta", 401, "INVALID_PASSWORD");
    }
}
