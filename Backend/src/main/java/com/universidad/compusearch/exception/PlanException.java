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

    public static PlanException exist(String nombre) {
        return new PlanException(
                "Ya existe un plan con el nombre proporcionado: " + nombre);
    }

    public static PlanException create() {
        return new PlanException(
                "Error al crear el plan");
    }

    public static PlanException inactive(String nombre){
        return new PlanException("El plan " + nombre + " esta inactivo");
    }
}
