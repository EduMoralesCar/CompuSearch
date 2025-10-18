package com.universidad.compusearch.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

// Clase auxiliar para errores customizados
@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException{
    private final int status;
    private final String code;

    public CustomException(String message, int status, String code){
        super(message);
        this.status = status;
        this.code = code;
    }
}