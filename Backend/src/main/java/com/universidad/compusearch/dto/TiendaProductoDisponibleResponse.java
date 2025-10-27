package com.universidad.compusearch.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO que representa la información de disponibilidad de un producto en una tienda específica.
 * 
 * <p>Esta clase se utiliza para mostrar los datos relevantes de un producto 
 * ofrecido por una tienda, sin exponer la estructura completa de la entidad 
 * {@code ProductoTienda}.</p>
 * 
 * <ul>
 *   <li><b>nombreTienda:</b> Nombre de la tienda que ofrece el producto.</li>
 *   <li><b>precio:</b> Precio del producto en la tienda.</li>
 *   <li><b>stock:</b> Cantidad disponible en inventario.</li>
 *   <li><b>urlProducto:</b> Enlace directo al producto en la tienda.</li>
 * </ul>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TiendaProductoDisponibleResponse {

    /** Nombre de la tienda que ofrece el producto. */
    private String nombreTienda;

    /** Precio del producto en la tienda. */
    private BigDecimal precio;

    /** Cantidad de unidades disponibles. */
    private int stock;

    /** URL o enlace al producto en la tienda. */
    private String urlProducto;
}
