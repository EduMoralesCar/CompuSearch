package com.universidad.compusearch.exception;

public class TiendaException extends CustomException{
    
    public TiendaException(String message){
        super(message);
    }

    public static TiendaException notFound() {
        return new TiendaException(
                "Empleado no encontrada");
    }
}
