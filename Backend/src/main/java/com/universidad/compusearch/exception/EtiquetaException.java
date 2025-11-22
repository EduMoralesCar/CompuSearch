package com.universidad.compusearch.exception;

public class EtiquetaException extends CustomException {

    public EtiquetaException(String message) {
        super(message);
    }

    public static EtiquetaException notFound() {
        return new EtiquetaException(
                "Etiqueta no encontrada");
    }

    public static EtiquetaException inUse() {
        return new EtiquetaException(
                "Etiqueta en uso por tiendas");
    }

    public static EtiquetaException exist() {
        return new EtiquetaException(
                "Ya existe una etiqueta con el nombre proporcionado");
    }
}
