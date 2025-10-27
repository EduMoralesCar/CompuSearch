package com.universidad.compusearch.exception;

/**
 * Excepción para manejar errores relacionados con datos requeridos.
 *
 * <p>
 * Esta clase extiende {@link CustomException} y proporciona métodos estáticos
 * para generar excepciones predefinidas cuando faltan datos obligatorios como
 * email o contraseña.
 * </p>
 */
public class DataRequiredException extends CustomException {

    /**
     * Constructor principal de la excepción.
     *
     * @param message Mensaje descriptivo del error
     * @param status  Código HTTP asociado
     * @param code    Código único que identifica la excepción
     */
    public DataRequiredException(String message, int status, String code) {
        super(message, status, code);
    }

    /**
     * Excepción cuando el email es requerido pero no se proporciona.
     *
     * @return Instancia de DataRequiredException
     */
    public static DataRequiredException emailRequired() {
        return new DataRequiredException("Email requerido", 400, "EMAIL_REQUIRED");
    }

    /**
     * Excepción cuando la contraseña es requerida pero no se proporciona.
     *
     * @return Instancia de DataRequiredException
     */
    public static DataRequiredException passwordRequired() {
        return new DataRequiredException("Contraseña requerida", 400, "PASSWORD_REQUIRED");
    }
}
