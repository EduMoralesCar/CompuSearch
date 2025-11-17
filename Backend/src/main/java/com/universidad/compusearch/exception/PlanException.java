package com.universidad.compusearch.exception;

public class PlanException extends CustomException {

    public PlanException(String message) {
        super(message);
    }

    public static PlanException notFound() {
        return new PlanException(
                "Plan no encontrado");
    }

    public static PlanException inUse() {
        return new PlanException(
                "Plan en uso por tiendas"
        );
    }

    public static PlanException exist() {
        return new PlanException(
                "Ya existe un plan con el nombre proporcionado");
    }
}
