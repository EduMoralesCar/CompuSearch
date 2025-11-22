package com.universidad.compusearch.exception;

public class TooManyAttemptsException extends CustomException {

    public TooManyAttemptsException(String message) {
        super(message);
    }

    public static TooManyAttemptsException login() {
        throw new TooManyAttemptsException("Haz alcanzado el limite de intentos, intentalo más tarde...");
    }

    public static TooManyAttemptsException incident() {
        throw new TooManyAttemptsException(
                "Haz alcanzado el limite de incidentes enviados, intentalo más tarde...");
    }

    public static TooManyAttemptsException resetPassword() {
        throw new TooManyAttemptsException(
                "Haz alcanzado el limite de intentos de reseto de contraseña, intentalo más tarde...");
    }

    public static TooManyAttemptsException info() {
        throw new TooManyAttemptsException(
                "Haz alcanzado el limite de actualizaciones de tu informacion, intentalo más tarde...");
    }

    public static TooManyAttemptsException solicitud() {
        throw new TooManyAttemptsException(
                "Haz alcanzado el limite de solicitudes enviados, intentalo más tarde...");
    }
}
