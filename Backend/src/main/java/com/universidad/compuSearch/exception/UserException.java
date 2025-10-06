package com.universidad.compusearch.exception;

public class UserException extends CustomException{

    public UserException(String message, int status, String code) {
        super(message, status, code);
    }

    public static UserException notFoundEmail() {
        return new UserException("Usuario no encontrado por email", 404, "USER_NOT_FOUND_EMAIL");
    }

    public static UserException notFounUsername() {
        return new UserException("Usuario no encontrado por email", 404, "USER_NOT_FOUND_USERNAME");
    }

    public static UserException notFound() {
        return new UserException("Usuario no encontrado", 404, "USER_NOT_FOUND");
    }
    
    public static UserException noActive() {
        return new UserException("Usuario desactivado", 403, "USER_NOT_ACTIVE");
    }
    
    public static UserException blocked() {
        return new UserException("Usuario bloqueado temporalmente", 403, "USER_BLOCKED");
    } 
}