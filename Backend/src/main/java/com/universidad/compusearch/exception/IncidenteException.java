package com.universidad.compusearch.exception;

/**
 * Excepción lanzada cuando ocurre un error relacionado con incidentes.
 *
 * <p>
 * Esta excepción extiende {@link CustomException} y se utiliza para manejar errores específicos
 * del módulo de incidentes.
 * </p>
 *
 * <p>
 * Código de error de ejemplo: <b>INCIDENTE_NOT_FOUND</b>
 * Mensaje por defecto: "Incidente no encontrada"
 * Código HTTP asociado: 404
 * </p>
 */
public class IncidenteException extends CustomException {

    /**
     * Constructor que inicializa la excepción con mensaje, código HTTP y código interno.
     *
     * @param message Mensaje descriptivo de la excepción
     * @param status Código HTTP asociado
     * @param code Código interno de la excepción
     */
    public IncidenteException(String message, int status, String code) {
        super(message, status, code);
    }

    /**
     * Devuelve una excepción preconfigurada indicando que el incidente no fue encontrado.
     *
     * @return IncidenteException con mensaje y código predefinidos
     */
    public static IncidenteException notFound() {
        return new IncidenteException("Incidente no encontrada", 404, "INCIDENTE_NOT_FOUND");
    }
}
