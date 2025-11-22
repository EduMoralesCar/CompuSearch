package com.universidad.compusearch.exception;

/**
 * Excepción para manejar errores relacionados con productos.
 *
 * <p>
 * Esta clase extiende {@link CustomException} y proporciona métodos estáticos
 * para generar excepciones predefinidas en escenarios donde un producto no se encuentra.
 * </p>
 */
public class ProductoException extends CustomException {
    
    /**
     * Constructor principal de la excepción.
     *
     * @param message Mensaje descriptivo del error
     * @param status  Código HTTP asociado
     * @param code    Código único que identifica la excepción
     */
    public ProductoException(String message, int status, String code) {
        super(message, status, code);
    }

    /**
     * Excepción cuando un producto no se encuentra en la base de datos.
     *
     * @return Instancia de ProductoException
     */
    public static ProductoException notFound() {
        return new ProductoException(
                "Producto no encontrado",
                404,
                "PRODUCT_NOT_FOUND");
    }
}
