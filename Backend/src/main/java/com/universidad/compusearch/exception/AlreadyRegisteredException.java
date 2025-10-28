package com.universidad.compusearch.exception;

/**
 * Excepción para manejar errores relacionados con datos ya registrados.
 *
 * <p>
 * Esta clase extiende {@link CustomException} y proporciona métodos estáticos
 * para generar excepciones predefinidas cuando un email o nombre de usuario
 * ya se encuentra registrado.
 * </p>
 */
public class AlreadyRegisteredException extends CustomException {

    /**
     * Constructor principal de la excepción.
     *
     * @param message Mensaje descriptivo del error
     * @param status  Código HTTP asociado
     * @param code    Código único que identifica la excepción
     */
    public AlreadyRegisteredException(String message, int status, String code) {
        super(message, status, code);
    }

    /**
     * Excepción cuando el email ya está registrado.
     *
     * @return Instancia de AlreadyRegisteredException
     */
    public static AlreadyRegisteredException email() {
        return new AlreadyRegisteredException(
                "El email ya está registrado",
                400,
                "EMAIL_REGISTERED");
    }

    /**
     * Excepción cuando el nombre de usuario ya está registrado.
     *
     * @return Instancia de AlreadyRegisteredException
     */
    public static AlreadyRegisteredException username() {
        return new AlreadyRegisteredException(
                "El nombre de usuario ya está registrado",
                400,
                "USERNAME_REGISTERED");
    }
}
