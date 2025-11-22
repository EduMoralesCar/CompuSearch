package com.universidad.compusearch.exception;

public class SolicitudTiendaException extends CustomException {

    public SolicitudTiendaException(String message) {
        super(message);
    }

    public static SolicitudTiendaException notFound() {
        return new SolicitudTiendaException(
                "Solicitud no encontrada");
    }

    public static SolicitudTiendaException incorrectStatus() {
        return new SolicitudTiendaException(
                "Estado incorrecto para la solicitud");
    }
}
