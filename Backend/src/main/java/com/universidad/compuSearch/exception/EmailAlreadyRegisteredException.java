package com.universidad.compuSearch.exception;

public class EmailAlreadyRegisteredException extends CustomException {
    public EmailAlreadyRegisteredException() {
        super("El email ya está registrado", 400, "EMAIL_REGISTERED");
    }
}
