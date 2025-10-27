package com.universidad.compusearch.exception;

// Excepciones de token
public class TokenException extends CustomException{
    public TokenException(String message, int status, String code) {
        super(message, status, code);
    }

    public static TokenException notFound(String type) {
        return new TokenException(type + " token no encontrado", 404, type.toUpperCase() + "_TOKEN_NOT_FOUND");
    }

    public static TokenException expired(String type) {
        return new TokenException(type + " token ha expirado", 401, type.toUpperCase() + "_TOKEN_EXPIRED");
    }

    public static TokenException invalid(String type) {
        return new TokenException(type + " token inválido", 401, type.toUpperCase() + "_TOKEN_INVALID");
    }

    public static TokenException invalidType() {
        return new TokenException("Token inválido", 401, "TOKEN_INVALID");
    }

    public static TokenException expiredType() {
        return new TokenException("Token expirado", 401, "TOKEN_EXPIRED");
    }
}