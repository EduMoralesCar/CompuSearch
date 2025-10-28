package com.universidad.compusearch.exception;

/**
 * Excepciones específicas relacionadas con usuarios.
 *
 * <p>
 * Proporciona métodos estáticos para generar instancias predefinidas de
 * excepciones comunes de usuario, como no encontrado, desactivado o bloqueado.
 * </p>
 */
public class UserException extends CustomException {

    /**
     * Constructor principal de la excepción de usuario.
     *
     * @param message el mensaje de error
     * @param status  el código HTTP asociado
     * @param code    un código único que identifica el tipo de error
     */
    public UserException(String message, int status, String code) {
        super(message, status, code);
    }

    /**
     * Excepción cuando un usuario no es encontrado por su email.
     *
     * @return instancia de {@link UserException}
     */
    public static UserException notFoundEmail() {
        return new UserException("Usuario no encontrado por email", 404, "USER_NOT_FOUND_EMAIL");
    }

    /**
     * Excepción cuando un usuario no es encontrado por su nombre de usuario.
     *
     * @return instancia de {@link UserException}
     */
    public static UserException notFounUsername() {
        return new UserException("Usuario no encontrado por username", 404, "USER_NOT_FOUND_USERNAME");
    }

    /**
     * Excepción cuando un usuario no es encontrado en general.
     *
     * @return instancia de {@link UserException}
     */
    public static UserException notFound() {
        return new UserException("Usuario no encontrado", 404, "USER_NOT_FOUND");
    }

    /**
     * Excepción cuando un usuario está desactivado.
     *
     * @return instancia de {@link UserException}
     */
    public static UserException noActive() {
        return new UserException("Usuario desactivado", 403, "USER_NOT_ACTIVE");
    }

    /**
     * Excepción cuando un usuario está bloqueado temporalmente.
     *
     * @return instancia de {@link UserException}
     */
    public static UserException blocked() {
        return new UserException("Usuario bloqueado temporalmente", 403, "USER_BLOCKED");
    }
}
