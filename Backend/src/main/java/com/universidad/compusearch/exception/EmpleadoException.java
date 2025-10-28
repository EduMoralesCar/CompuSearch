package com.universidad.compusearch.exception;

/**
 * Excepción para manejar errores relacionados con etiquetas.
 *
 * <p>
 * Esta clase extiende {@link CustomException} y proporciona métodos estáticos
 * para generar excepciones predefinidas cuando un empleado no se encuentra
 * o está en uso.
 * </p>
 */
public class EmpleadoException extends CustomException{
    
    /**
     * Constructor principal de la excepción.
     *
     * @param message Mensaje descriptivo del error
     * @param status  Código HTTP asociado
     * @param code    Código único que identifica la excepción
     */
    public EmpleadoException(String message, int status, String code){
        super(message, status, code);
    }

    /**
     * Excepción cuando un empleado no es encontrada.
     *
     * @return Instancia de {@link EtiquetaException}
     */
    public static EmpleadoException notFound() {
        return new EmpleadoException(
                "Empleado no encontrada",
                404,
                "EMPLEADO_NOT_FOUND"
        );
    }
}
