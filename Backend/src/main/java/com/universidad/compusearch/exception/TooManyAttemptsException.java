package com.universidad.compusearch.exception;

// Excepciones de muchos intentos
public class TooManyAttemptsException extends CustomException{
    
    public TooManyAttemptsException(String message, int status, String code){
        super(message, status, code);
    }

    public static TooManyAttemptsException login(){
        throw new TooManyAttemptsException("Haz alcanzado el limite de intentos de inicio de sesión, intentalo más tarde...", 429, "LOGIN_TOO_MANY_ATTEMPTS");
    }

    public static TooManyAttemptsException incident(){
        throw new TooManyAttemptsException("Haz alcanzado el limite de solicitudes enviadas, intentalo más tarde...", 429, "INCIDENT_TOO_MANY_ATTEMPTS");
    }
    
    public static TooManyAttemptsException resetPassword(){
        throw new TooManyAttemptsException("Haz alcanzado el limite de intentos de reseto de contraseña, intentalo más tarde...", 429, "RESET_PASSWORD_TOO_MANY_ATTEMPTS");
    }

    public static TooManyAttemptsException info(){
        throw new TooManyAttemptsException("Haz alcanzado el limite de actualizaciones de tu informacion, intentalo más tarde...", 429, "INFO_USER_PASSWORD_TOO_MANY_ATTEMPTS");
    }

    public static TooManyAttemptsException solicitud(){
        throw new TooManyAttemptsException("Haz envio una solicitud hoy, intentalo más tarde...", 429, "INFO_USER_PASSWORD_TOO_MANY_ATTEMPTS");
    }
}