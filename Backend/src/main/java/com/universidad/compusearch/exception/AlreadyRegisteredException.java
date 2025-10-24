package com.universidad.compusearch.exception;

// Excepciones de datos ya registrados
public class AlreadyRegisteredException extends CustomException {

    public AlreadyRegisteredException(String message, int status, String code) {
        super(message, status, code);
    }
    
    public static AlreadyRegisteredException email() {
        return new AlreadyRegisteredException("El email ya está registrado", 400, "EMAIL_REGISTERED");
    }

    public static AlreadyRegisteredException username() {
        return new AlreadyRegisteredException("El nombre de usuario ya está registrado", 400, "USERNAME_REGISTERED");
    }
}