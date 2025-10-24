package com.universidad.compusearch.exception;

// Excepciones de producto tienda
public class ProductoTiendaException extends CustomException {

    public ProductoTiendaException(String message, int status, String code) {
        super(message, status, code);
    }
    
    public static ProductoTiendaException notFoundProductoOrShop() {
        return new ProductoTiendaException("Producto de tienda no encontrada", 404, "NOT_FOUND_PRODUCT_OR_SHOP");
    }
}