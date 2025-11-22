package com.universidad.compusearch.exception;

public class CategoriaException extends CustomException {

    public CategoriaException(String message) {
        super(message);
    }

    public static CategoriaException notFound() {
        return new CategoriaException(
                "Categoria no encontrado");
    }

    public static CategoriaException inUse() {
        return new CategoriaException(
                "Categoria en uso por otros productos");
    }
    public static CategoriaException exist() {
        return new CategoriaException(
                "Ya existe una categoría con el nombre proporcionado");
    }

}
