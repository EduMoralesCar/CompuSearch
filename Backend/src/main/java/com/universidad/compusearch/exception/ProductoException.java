package com.universidad.compusearch.exception;

// Excepciones de producto
public class ProductoException extends CustomException{
    
    public ProductoException(String message, int status, String code) {
        super(message, status, code);
    }

    public static ProductoException notFound() {
        return new ProductoException("Producto no encontrado", 404, "PRODUCT_NOT_FOUND");
    }
}
