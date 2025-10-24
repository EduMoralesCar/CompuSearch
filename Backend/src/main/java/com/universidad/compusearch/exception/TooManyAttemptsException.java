package com.universidad.compusearch.exception;

// Excepciones de muchos intentos
public class TooManyAttemptsException extends CustomException{
    
    public TooManyAttemptsException(){
        super("Demasiados intentos", 429, "RESET_TOO_MANY_ATTEMPTS");
    }
}