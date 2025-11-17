package com.universidad.compusearch.exception;

public class IncidenteException extends CustomException {

    /**
     * Constructor que inicializa la excepción con mensaje, código HTTP y código interno.
     *
     * @param message Mensaje descriptivo de la excepción
     * @param status Código HTTP asociado
     * @param code Código interno de la excepción
     */
    public IncidenteException(String message) {
        super(message);
    }

    /**
     * Devuelve una excepción preconfigurada indicando que el incidente no fue encontrado.
     *
     * @return IncidenteException con mensaje y código predefinidos
     */
    public static IncidenteException notFound() {
        return new IncidenteException("Incidente no encontrada");
    }
}
