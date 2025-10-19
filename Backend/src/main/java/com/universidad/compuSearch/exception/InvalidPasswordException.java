package com.universidad.compusearch.exception;

public class InvalidPasswordException extends CustomException{
    public InvalidPasswordException() {
        super("Contraseña incorrecta", 401, "INVALID_PASSWORD");
    }
}