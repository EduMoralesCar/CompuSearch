package com.universidad.compusearch.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO que representa el detalle de un producto incluido en una build.
 * 
 * <p>Se utiliza para registrar los productos seleccionados por el usuario
 * al crear una build, indicando cantidad, precio unitario y subtotal.</p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DetalleBuildRequest {

    /** Identificador del producto asociado a la tienda. */
    @NotNull(message = "El id del producto de la tienda es obligatorio")
    private Long idProductoTienda;

    /** Cantidad del producto seleccionada para la build. */
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private int cantidad;

    /** Precio unitario del producto. */
    @NotNull(message = "El precio del producto de la tienda es obligatorio")
    @Positive(message = "El precio unitario debe ser positivo")
    private BigDecimal precioUnitario;

    /** Subtotal calculado del producto (precio * cantidad). */
    @NotNull(message = "El subtotal del producto de la tienda es obligatorio")
    @Positive(message = "El subtotal debe ser positivo")
    private BigDecimal subTotal;
}
