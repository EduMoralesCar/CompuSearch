package com.universidad.compusearch.exception;

/**
 * Excepción para manejar errores relacionados con etiquetas.
 *
 * <p>
 * Esta clase extiende {@link CustomException} y proporciona métodos estáticos
 * para generar excepciones predefinidas cuando una etiqueta no se encuentra
 * o está en uso.
 * </p>
 */
public class EtiquetaException extends CustomException {

    /**
     * Constructor principal de la excepción.
     *
     * @param message Mensaje descriptivo del error
     * @param status  Código HTTP asociado
     * @param code    Código único que identifica la excepción
     */
    public EtiquetaException(String message, int status, String code) {
        super(message, status, code);
    }

    /**
     * Excepción cuando una etiqueta no es encontrada.
     *
     * @return Instancia de {@link EtiquetaException}
     */
    public static EtiquetaException notFound() {
        return new EtiquetaException(
                "Etiqueta no encontrada",
                404,
                "ETIQUETA_NOT_FOUND"
        );
    }

    /**
     * Excepción cuando una etiqueta está en uso y no se puede eliminar.
     *
     * @return Instancia de {@link EtiquetaException} indicando conflicto
     */
    public static EtiquetaException inUse() {
        return new EtiquetaException(
                "Etiqueta en uso por tiendas",
                409,
                "ETIQUETA_IN_USE"
        );
    }

    /**
     * Excepción cuando una etiqueta con el mismo nombre existe
     *
     * @return Instancia de {@link EtiquetaException} indicando conflicto
     */
    public static EtiquetaException exist() {
        return new EtiquetaException(
                "Ya existe una etiqueta con el nombre proporcionado",
                400,
                "ETIQUETA_ALREADY_EXISTS");
    }
}
