package com.universidad.compusearch.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DetalleBuildRequest {

    @NotNull(message = "El id del producto de la tienda es obligatorio")
    private Long idProductoTienda;

    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private int cantidad;

    @NotNull(message = "El precio del producto de la tienda es obligatorio")
    @Positive(message = "El precio unitario debe ser positivo")
    private BigDecimal precioUnitario;

    @NotNull(message = "El subtotal del producto de la tienda es obligatorio")
    @Positive(message = "El subtotal debe ser positivo")
    private BigDecimal subTotal;
}
