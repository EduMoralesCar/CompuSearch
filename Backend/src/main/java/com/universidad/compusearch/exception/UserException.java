package com.universidad.compusearch.exception;

public class UserException extends CustomException {

    public UserException(String message) {
        super(message);
    }

    public static UserException notFoundEmail(String email) {
        return new UserException("Usuario con email " + email + " no encontrado.");
    }

    public static UserException notFoundUsername(String username) {
        return new UserException("Usuario con username " + username + " no encontrado.");
    }

    public static UserException notFoundId(Long id) {
        return new UserException("Usuario con id " + id + " no encontrado.");
    }

    public static UserException notFound() {
        return new UserException("Usuario no encontrado.");
    }

    public static UserException noActive() {
        return new UserException("Usuario desactivado.");
    }

    public static UserException blocked() {
        return new UserException("Usuario bloqueado temporalmente.");
    }

    public static UserException emailAlredyRegistered(String email){
        return new UserException("El email " + email + " ya esta en uso.");
    }

    public static UserException usernameAlredyRegistered(String username){
        return new UserException("El username " + username + " ya esta en uso.");
    }

    public static UserException invalidPassword(){
        return new UserException("Contraseña incorrecta.");
    }
}
