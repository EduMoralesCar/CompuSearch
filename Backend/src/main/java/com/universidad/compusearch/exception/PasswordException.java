package com.universidad.compusearch.exception;

public class PasswordException extends CustomException {

    public PasswordException(String message) {
        super(message);
    }

    public static PasswordException isInvalid() {
        return new PasswordException(
                "La contraseña actual no coincide");
    }

    public static PasswordException equalOldAndNew() {
        return new PasswordException(
                "La nueva contraseña es igual a la anterior");
    }

    public static PasswordException notEquals() {
        return new PasswordException(
                "La contraseña nueva no es igual a la confirmación");
    }

    public static PasswordException notFoundData() {
        return new PasswordException(
                "Faltan datos para actualizar la contraseña");
    }
}
