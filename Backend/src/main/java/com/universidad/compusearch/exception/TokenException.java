package com.universidad.compusearch.exception;

public class TokenException extends CustomException {

    public TokenException(String message) {
        super(message);
    }

    public static TokenException notFound(String type) {
        return new TokenException(type + " token no encontrado");
    }

    public static TokenException expired(String type) {
        return new TokenException(type + " token ha expirado");
    }

    public static TokenException invalid(String type) {
        return new TokenException(type + " token inválido");
    }

    public static TokenException invalidType() {
        return new TokenException("Token inválido");
    }

    public static TokenException expiredType() {
        return new TokenException("Token expirado");
    }
}
