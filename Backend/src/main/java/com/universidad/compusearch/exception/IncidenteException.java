package com.universidad.compusearch.exception;

public class IncidenteException extends CustomException {
    public IncidenteException(String message, int status, String code) {
        super(message, status, code);
    }

    public static IncidenteException notFound(){
        return new IncidenteException("Incidente no encontrada", 404, "INCIDENTE_NOT_FOUND");
    }
}
