package com.universidad.compusearch.dto;

import java.util.List;

import com.universidad.compusearch.entity.Etiqueta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO que representa la información pública de una tienda.
 * 
 * <p>Esta clase se utiliza para devolver datos de la tienda a los clientes
 * sin exponer toda la entidad {@code Tienda}.</p>
 * 
 * <ul>
 *   <li><b>nombre:</b> Nombre comercial de la tienda.</li>
 *   <li><b>descripcion:</b> Descripción general de la tienda.</li>
 *   <li><b>telefono:</b> Número de contacto.</li>
 *   <li><b>direccion:</b> Dirección física.</li>
 *   <li><b>logo:</b> Logo en formato Base64 o URL pública.</li>
 *   <li><b>urlPagina:</b> Página web de la tienda (si existe).</li>
 *   <li><b>etiquetas:</b> Lista de nombres de etiquetas asociadas.</li>
 * </ul>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TiendaResponse {
    private String nombre;
    private String descripcion;
    private String telefono;
    private String direccion;
    private String logo;
    private String urlPagina;
    private List<Etiqueta> etiquetas;
}
