package com.universidad.compusearch.exception;

public class ProductoException extends CustomException {
    
    public ProductoException(String message) {
        super(message);
    }

    public static ProductoException notFound() {
        return new ProductoException(
                "Producto no encontrado");
    }
}
