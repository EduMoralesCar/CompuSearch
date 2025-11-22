package com.universidad.compusearch.exception;

/**
 * Excepción para manejar errores relacionados con productos en tiendas.
 *
 * <p>
 * Esta clase extiende {@link CustomException} y proporciona métodos estáticos
 * para generar excepciones predefinidas en escenarios donde un producto de tienda
 * no se encuentra.
 * </p>
 */
public class ProductoTiendaException extends CustomException {

    /**
     * Constructor principal de la excepción.
     *
     * @param message Mensaje descriptivo del error
     * @param status  Código HTTP asociado
     * @param code    Código único que identifica la excepción
     */
    public ProductoTiendaException(String message, int status, String code) {
        super(message, status, code);
    }

    /**
     * Excepción cuando un producto de tienda o la tienda asociada no se encuentra.
     *
     * @return Instancia de ProductoTiendaException
     */
    public static ProductoTiendaException notFoundProductoOrShop() {
        return new ProductoTiendaException(
                "Producto de tienda no encontrada",
                404,
                "NOT_FOUND_PRODUCT_OR_SHOP");
    }
}
