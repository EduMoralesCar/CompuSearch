package com.universidad.compusearch.exception;

/**
 * Excepción para manejar errores relacionados con contraseñas.
 *
 * <p>
 * Esta clase extiende {@link CustomException} y proporciona métodos estáticos
 * para generar excepciones predefinidas en escenarios comunes de gestión de contraseñas.
 * </p>
 */
public class PasswordException extends CustomException {

    /**
     * Constructor principal de la excepción.
     *
     * @param message Mensaje descriptivo del error
     * @param status  Código HTTP asociado
     * @param code    Código único que identifica la excepción
     */
    public PasswordException(String message, int status, String code) {
        super(message, status, code);
    }

    /**
     * Excepción cuando la contraseña actual no coincide.
     *
     * @return Instancia de PasswordException
     */
    public static PasswordException isInvalid() {
        return new PasswordException(
                "La contraseña actual no coincide",
                401,
                "INVALID_CURRENT_PASSWORD");
    }

    /**
     * Excepción cuando la nueva contraseña es igual a la anterior.
     *
     * @return Instancia de PasswordException
     */
    public static PasswordException equalOldAndNew() {
        return new PasswordException(
                "La nueva contraseña es igual a la anterior",
                409,
                "PASSWORDS_MATCH");
    }

    /**
     * Excepción cuando la nueva contraseña no coincide con la confirmación.
     *
     * @return Instancia de PasswordException
     */
    public static PasswordException notEquals() {
        return new PasswordException(
                "La contraseña nueva no es igual a la confirmación",
                400,
                "CONFIRMATION_MISMATCH");
    }

    /**
     * Excepción cuando faltan datos para actualizar la contraseña.
     *
     * @return Instancia de PasswordException
     */
    public static PasswordException notFoundData() {
        return new PasswordException(
                "Faltan datos para actualizar la contraseña",
                400,
                "MISSING_FIELDS");
    }
}
