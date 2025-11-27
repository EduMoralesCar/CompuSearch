package com.universidad.compusearch.exception;

public class TiendaException extends CustomException{
    
    public TiendaException(String message){
        super(message);
    }

    public static TiendaException notFound() {
        return new TiendaException(
                "Tienda no encontrada");
    }

    public static TiendaException duplicatedData() {
        return new TiendaException(
                "La solicitud tiene datos duplicados de otra tienda");
    }

    public static TiendaException errorInsertDirect() {
        return new TiendaException(
                "Error al insertar la tienda directamente");
    }

    public static TiendaException dataInUse(String data) {
        return new TiendaException(
                "El valor ingresado ya esta en otra tienda: " + data);
    }
}
