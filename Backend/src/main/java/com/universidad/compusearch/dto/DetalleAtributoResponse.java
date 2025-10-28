package com.universidad.compusearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO que representa el detalle de un atributo de producto.
 * 
 * <p>Se utiliza para mostrar información específica de un atributo
 * asociado a un producto, incluyendo su nombre y el valor asignado.</p>
 * 
 * <ul>
 *   <li><b>nombreAtributo:</b> Nombre del atributo (por ejemplo, "Memoria RAM", "Frecuencia").</li>
 *   <li><b>valor:</b> Valor correspondiente al atributo (por ejemplo, "16 GB", "3.6 GHz").</li>
 * </ul>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DetalleAtributoResponse {

    /** Nombre del atributo del producto. */
    private String nombreAtributo;

    /** Valor asignado a dicho atributo. */
    private String valor;
}
