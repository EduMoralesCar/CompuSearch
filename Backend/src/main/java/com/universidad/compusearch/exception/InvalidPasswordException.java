package com.universidad.compusearch.exception;

// Excepciones de contraseña invalida
public class InvalidPasswordException extends CustomException{
    public InvalidPasswordException() {
        super("Contraseña incorrecta", 401, "INVALID_PASSWORD");
    }
}