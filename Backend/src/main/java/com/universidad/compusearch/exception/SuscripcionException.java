package com.universidad.compusearch.exception;

public class SuscripcionException extends CustomException {
    public SuscripcionException(String message) {
        super(message);
    }

    public static SuscripcionException active() {
        return new SuscripcionException(
                "La tienda ya tiene una suscripción activa.");
    }

    public static SuscripcionException noActive() {
        return new SuscripcionException(
                "La tienda no tiene una suscripción activa.");
    }

    public static SuscripcionException existsPendiente() {
        return new SuscripcionException(
                "La tienda tiene una suscripcion pendiente.");
    }
    
}
