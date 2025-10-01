package com.universidad.compuSearch.exception;

public class TooManyAttemptsException extends CustomException{
    
    public TooManyAttemptsException(){
        super("Demasiados intentos", 429, "RESET_TOO_MANY_ATTEMPTS");
    }
}
