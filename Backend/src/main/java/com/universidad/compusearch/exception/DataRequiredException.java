package com.universidad.compusearch.exception;

public class DataRequiredException extends CustomException {

    public DataRequiredException(String message) {
        super(message);
    }

    public static DataRequiredException emailRequired() {
        return new DataRequiredException("Email requerido");
    }

    public static DataRequiredException passwordRequired() {
        return new DataRequiredException("Contraseña requerida");
    }
}
