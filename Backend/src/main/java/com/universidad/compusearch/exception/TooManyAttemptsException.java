package com.universidad.compusearch.exception;

/**
 * Excepción para manejar casos de demasiados intentos en distintas operaciones.
 *
 * <p>
 * Esta clase extiende {@link CustomException} y proporciona métodos estáticos
 * que lanzan excepciones predefinidas cuando se excede el límite de intentos en
 * acciones específicas, como login, incidentes o reseteo de contraseña.
 * </p>
 */
public class TooManyAttemptsException extends CustomException {

    /**
     * Constructor principal de la excepción.
     *
     * @param message Mensaje descriptivo del error
     * @param status  Código HTTP asociado
     * @param code    Código único que identifica la excepción
     */
    public TooManyAttemptsException(String message, int status, String code) {
        super(message, status, code);
    }

    /**
     * Excepción para indicar que se ha superado el límite de intentos de login.
     *
     * @return Nunca retorna, siempre lanza la excepción
     */
    public static TooManyAttemptsException login() {
        throw new TooManyAttemptsException(
                "Haz alcanzado el limite de intentos de inicio de sesión, intentalo más tarde...",
                429,
                "LOGIN_TOO_MANY_ATTEMPTS"
        );
    }

    /**
     * Excepción para indicar que se ha superado el límite de creación de incidentes.
     *
     * @return Nunca retorna, siempre lanza la excepción
     */
    public static TooManyAttemptsException incident() {
        throw new TooManyAttemptsException(
                "Haz alcanzado el limite de solicitudes enviadas, intentalo más tarde...",
                429,
                "INCIDENT_TOO_MANY_ATTEMPTS"
        );
    }

    /**
     * Excepción para indicar que se ha superado el límite de intentos de reseteo de contraseña.
     *
     * @return Nunca retorna, siempre lanza la excepción
     */
    public static TooManyAttemptsException resetPassword() {
        throw new TooManyAttemptsException(
                "Haz alcanzado el limite de intentos de reseto de contraseña, intentalo más tarde...",
                429,
                "RESET_PASSWORD_TOO_MANY_ATTEMPTS"
        );
    }

    /**
     * Excepción para indicar que se ha superado el límite de actualizaciones de información del usuario.
     *
     * @return Nunca retorna, siempre lanza la excepción
     */
    public static TooManyAttemptsException info() {
        throw new TooManyAttemptsException(
                "Haz alcanzado el limite de actualizaciones de tu informacion, intentalo más tarde...",
                429,
                "INFO_USER_PASSWORD_TOO_MANY_ATTEMPTS"
        );
    }

    /**
     * Excepción para indicar que se ha superado el límite de solicitudes enviadas.
     *
     * @return Nunca retorna, siempre lanza la excepción
     */
    public static TooManyAttemptsException solicitud() {
        throw new TooManyAttemptsException(
                "Haz envio una solicitud hoy, intentalo más tarde...",
                429,
                "INFO_USER_PASSWORD_TOO_MANY_ATTEMPTS"
        );
    }
}
