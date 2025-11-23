package com.universidad.compusearch.exception;

public class TiendaException extends CustomException{
    
    public TiendaException(String message){
        super(message);
    }

    public static TiendaException notFound() {
        return new TiendaException(
                "Tienda no encontrada");
    }

    public static TiendaException anyInfoIsUsed() {
        return new TiendaException(
                "La solicitud tiene datos importantes de otra tienda");
    }
}
