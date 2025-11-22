package com.universidad.compusearch.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO que representa el detalle de un producto dentro de una build.
 * 
 * <p>Incluye información sobre el producto, la tienda, el precio, la cantidad
 * seleccionada y los atributos específicos del producto.</p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DetalleBuildResponse {

    /** Identificador del producto asociado a la tienda. */
    private long idProductoTienda;

    /** Nombre del producto incluido en la build. */
    private String nombreProducto;

    /** Nombre de la tienda que ofrece el producto. */
    private String nombreTienda;

    /** Stock disponible del producto en la tienda. */
    private int stock;

    /** Subtotal calculado para este producto (precio * cantidad). */
    private BigDecimal subTotal;

    /** Precio unitario del producto. */
    private BigDecimal precio;

    /** URL de la página del producto. */
    private String urlProducto;

    /** Categoría a la que pertenece el producto. */
    private String categoria;

    /** Cantidad del producto incluida en la build. */
    private int cantidad;

    /** Lista de detalles o atributos específicos del producto (ej. marca, modelo, potencia, etc.). */
    private List<DetalleAtributoResponse> detalles;
}
