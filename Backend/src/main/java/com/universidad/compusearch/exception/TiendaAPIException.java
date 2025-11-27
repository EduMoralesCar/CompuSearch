package com.universidad.compusearch.exception;

public class TiendaAPIException extends CustomException {

    public TiendaAPIException(String message) {
        super(message);
    }

    public static TiendaAPIException notRegistered(Long idTienda) {
        return new TiendaAPIException(
                "La tienda con id {} aun no tiene registrada una api");
    }
}
