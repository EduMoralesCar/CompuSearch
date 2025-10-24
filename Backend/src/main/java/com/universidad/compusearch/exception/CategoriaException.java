package com.universidad.compusearch.exception;

// Excepciones de categoria
public class CategoriaException extends CustomException{
    
    public CategoriaException(String message, int status, String code) {
        super(message, status, code);
    }

    public static CategoriaException notFound() {
        return new CategoriaException("Categoria no encontrado", 404, "CATEGORY_NOT_FOUND");
    }
}