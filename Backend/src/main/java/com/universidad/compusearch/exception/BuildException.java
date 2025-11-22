package com.universidad.compusearch.exception;

/**
 * Excepción para manejar errores relacionados con builds.
 *
 * <p>
 * Esta clase extiende {@link CustomException} y proporciona métodos estáticos
 * para generar excepciones predefinidas cuando una build no se encuentra.
 * </p>
 */
public class BuildException extends CustomException {

    /**
     * Constructor principal de la excepción.
     *
     * @param message Mensaje descriptivo del error
     * @param status  Código HTTP asociado
     * @param code    Código único que identifica la excepción
     */
    public BuildException(String message, int status, String code) {
        super(message, status, code);
    }

    /**
     * Excepción cuando una build no es encontrada.
     *
     * @return Instancia de BuildException
     */
    public static BuildException notFound() {
        return new BuildException(
                "Build no encontrada",
                404,
                "BUILD_NOT_FOUND");
    }
}
