package com.universidad.compusearch.exception;

// Excepciones de atributo
public class AtributoException extends CustomException {

    public AtributoException(String message, int status, String code) {
        super(message, status, code);
    }
    
    public static AtributoException notFound() {
        return new AtributoException("Atributo no encontrada", 404, "BUILD_NOT_FOUND");
    }
}