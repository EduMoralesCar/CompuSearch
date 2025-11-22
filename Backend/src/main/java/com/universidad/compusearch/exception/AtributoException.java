package com.universidad.compusearch.exception;

/**
 * Excepción para manejar errores relacionados con atributos.
 *
 * <p>
 * Esta clase extiende {@link CustomException} y proporciona métodos estáticos
 * para generar excepciones predefinidas cuando un atributo no se encuentra.
 * </p>
 */
public class AtributoException extends CustomException {

    /**
     * Constructor principal de la excepción.
     *
     * @param message Mensaje descriptivo del error
     * @param status  Código HTTP asociado
     * @param code    Código único que identifica la excepción
     */
    public AtributoException(String message, int status, String code) {
        super(message, status, code);
    }

    /**
     * Excepción cuando un atributo no es encontrado.
     *
     * @return Instancia de AtributoException
     */
    public static AtributoException notFound() {
        return new AtributoException(
                "Atributo no encontrado",
                404,
                "ATRIBUTO_NOT_FOUND");
    }
}
