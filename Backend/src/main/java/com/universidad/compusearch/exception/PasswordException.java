package com.universidad.compusearch.exception;

public class PasswordException extends CustomException {
    public PasswordException(String message, int status, String code) {
        super(message, status, code);
    }

    public static PasswordException isInvalid() {
        return new PasswordException("La contraseña actual no coincide", 401, "INVALID_CURRENT_PASSWORD");
    }

    public static PasswordException equalOldAndNew() {
        return new PasswordException("La nueva contraseña es igual a la anterior", 409, "PASSWORDS_MATCH");
    }

    public static PasswordException notEquals() {
        return new PasswordException("La contraseña nueva no es igual a la confirmación", 400, "CONFIRMATION_MISMATCH");
    }

    public static PasswordException notFoundData() {
        return new PasswordException("Faltan datos para actualizar la contraseña", 400, "MISSING_FIELDS");
    }
}
