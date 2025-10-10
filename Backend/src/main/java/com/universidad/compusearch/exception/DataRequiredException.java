package com.universidad.compusearch.exception;

public class DataRequiredException extends CustomException {

    public DataRequiredException(String message, int status, String code) {
        super(message, status, code);
    }

    public static DataRequiredException emailRequired() {
        return new DataRequiredException("Email requerido", 400, "EMAIL_REQUIRED");
    }

    public static DataRequiredException passwordRequired() {
        return new DataRequiredException("Contraseña requerida", 400, "PASSWORD_REQUIRED");
    }
}