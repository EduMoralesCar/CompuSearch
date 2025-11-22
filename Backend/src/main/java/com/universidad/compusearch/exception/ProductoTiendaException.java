package com.universidad.compusearch.exception;

public class ProductoTiendaException extends CustomException {

    public ProductoTiendaException(String message) {
        super(message);
    }

    public static ProductoTiendaException notFoundProductoShop() {
        return new ProductoTiendaException(
                "Producto de tienda no encontrada");
    }
}
