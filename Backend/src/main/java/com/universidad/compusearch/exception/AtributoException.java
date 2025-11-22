package com.universidad.compusearch.exception;

public class AtributoException extends CustomException {

    public AtributoException(String message) {
        super(message);
    }

    public static AtributoException notFound() {
        return new AtributoException(
                "Atributo no encontrado");
    }
}
