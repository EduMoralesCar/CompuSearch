package com.universidad.compusearch.exception;

public class IncidenteException extends CustomException {

    public IncidenteException(String message) {
        super(message);
    }
    
    public static IncidenteException notFound() {
        return new IncidenteException("Incidente no encontrada");
    }
}
