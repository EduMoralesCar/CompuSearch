package com.universidad.compusearch.exception;

public class TiendaException extends CustomException{
    
    public TiendaException(String message){
        super(message);
    }

    public static TiendaException notFound() {
        return new TiendaException(
                "Tienda no encontrada");
    }

    public static TiendaException InfoIsUsed() {
        return new TiendaException(
                "La solicitud tiene datos duplicados de otra tienda");
    }

    public static TiendaException errorInsertDirect() {
        return new TiendaException(
                "Error al insertar la tienda directamente");
    }
}
