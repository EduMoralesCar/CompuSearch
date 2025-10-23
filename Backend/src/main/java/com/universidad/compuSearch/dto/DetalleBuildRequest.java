package com.universidad.compusearch.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DetalleBuildRequest {

    @NotBlank(message = "El id del producto de la tienda es obligaroio")
    private Long idProductoTienda;

    @NotBlank(message = "La cantidad del producto de la tienda es obligaroio")
    private int cantidad;

    @NotBlank(message = "El precio del producto de la tienda es obligaroio")
    private BigDecimal precioUnitario;

    @NotBlank(message = "El subtotal del producto de la tienda es obligaroio")
    private BigDecimal subTotal;
}
