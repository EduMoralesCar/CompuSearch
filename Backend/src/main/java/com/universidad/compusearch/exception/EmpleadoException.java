package com.universidad.compusearch.exception;

public class EmpleadoException extends CustomException{
    
    public EmpleadoException(String message){
        super(message);
    }

    public static EmpleadoException notFound() {
        return new EmpleadoException(
                "Empleado no encontrada");
    }
}
