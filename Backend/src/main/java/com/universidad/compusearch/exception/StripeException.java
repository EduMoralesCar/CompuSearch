package com.universidad.compusearch.exception;

public class StripeException extends CustomException {
    public StripeException(String message) {
        super(message);
    }

    public static StripeException errorCreateCustomer() {
        return new StripeException(
                "No se pudo crear el customer de stripe");
    }

    public static StripeException errorObtainCustomer() {
        return new StripeException(
                "No se pudo obtener el customer desde stripe");
    }

    public static StripeException errorCreateProduct() {
        return new StripeException(
                "No se pudo crear el product de stripe");
    }

    public static StripeException errorCreatePrice() {
        return new StripeException(
                "No se pudo crear el price de stripe");
    }

    public static StripeException errorUpdateProduct() {
        return new StripeException(
                "No se puede actualizar el producto de stripe");
    }
}
