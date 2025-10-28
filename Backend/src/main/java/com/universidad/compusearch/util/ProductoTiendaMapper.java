package com.universidad.compusearch.util;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.universidad.compusearch.dto.DetalleAtributoResponse;
import com.universidad.compusearch.dto.ProductoBuildResponse;
import com.universidad.compusearch.dto.ProductoInfoResponse;
import com.universidad.compusearch.dto.ProductoTiendaResponse;
import com.universidad.compusearch.entity.ProductoAtributo;
import com.universidad.compusearch.entity.ProductoTienda;

/**
 * Clase utilitaria para realizar conversiones entre entidades {@link ProductoTienda}
 * y sus respectivos objetos de transferencia de datos (DTOs).
 * 
 * Esta clase contiene únicamente métodos estáticos y no debe ser instanciada.
 * Su propósito es centralizar la lógica de mapeo entre entidades y respuestas
 * utilizadas en el sistema CompuSearch.
 * 
 */
public class ProductoTiendaMapper {

    /**
     * Convierte una entidad {@link ProductoTienda} en un objeto {@link ProductoTiendaResponse}.
     *
     * @param productoTienda entidad {@code ProductoTienda} que se desea convertir.
     * @return un objeto {@code ProductoTiendaResponse} con los datos básicos del producto en tienda.
     */
    public static ProductoTiendaResponse mapToResponse(ProductoTienda productoTienda) {
        return new ProductoTiendaResponse(
                productoTienda.getIdProductoTienda(),
                productoTienda.getProducto().getNombre(),
                productoTienda.getPrecio(),
                productoTienda.getStock(),
                productoTienda.getUrlImagen(),
                productoTienda.getTienda().getNombre());
    }

    /**
     * Convierte una entidad {@link ProductoTienda} en un objeto {@link ProductoInfoResponse}
     * que contiene información más detallada del producto.
     *
     * @param productoTienda entidad {@code ProductoTienda} que se desea convertir.
     * @return un objeto {@code ProductoInfoResponse} con los detalles del producto, incluyendo sus atributos.
     */
    public static ProductoInfoResponse mapToInfoProducto(ProductoTienda productoTienda) {
        ProductoInfoResponse dto = new ProductoInfoResponse();
        dto.setNombreProducto(productoTienda.getProducto().getNombre());
        dto.setMarca(productoTienda.getProducto().getMarca());
        dto.setModelo(productoTienda.getProducto().getModelo());
        dto.setDescripcion(productoTienda.getProducto().getDescripcion());
        dto.setUrlImagen(productoTienda.getUrlImagen());
        dto.setNombreTienda(productoTienda.getTienda().getNombre());
        dto.setStock(productoTienda.getStock());
        dto.setPrecio(productoTienda.getPrecio());
        dto.setUrlProducto(productoTienda.getUrlProducto());

        if (productoTienda.getProducto().getAtributos() != null) {
            dto.setAtributos(mapToDetalleAtributo(productoTienda.getProducto().getAtributos()));
        } else {
            dto.setAtributos(Collections.emptyList());
        }

        return dto;
    }

    /**
     * Convierte una entidad {@link ProductoTienda} en un objeto {@link ProductoBuildResponse},
     * utilizado principalmente para la construcción de listas o configuraciones de productos.
     *
     * @param productoTienda entidad {@code ProductoTienda} que se desea convertir.
     * @return un objeto {@code ProductoBuildResponse} con información del producto y sus atributos.
     */
    public static ProductoBuildResponse mapToProductoBuildResponse(ProductoTienda productoTienda) {
        ProductoBuildResponse response = new ProductoBuildResponse();
        response.setIdProductoTienda(productoTienda.getIdProductoTienda());
        response.setNombreProducto(productoTienda.getProducto().getNombre());
        response.setPrecio(productoTienda.getPrecio());
        response.setStock(productoTienda.getStock());
        response.setNombreTienda(productoTienda.getTienda().getNombre());
        response.setUrlProducto(productoTienda.getUrlProducto());

        if (productoTienda.getProducto().getAtributos() != null) {
            List<DetalleAtributoResponse> atributos = productoTienda.getProducto()
                    .getAtributos()
                    .stream()
                    .map(attr -> new DetalleAtributoResponse(attr.getAtributo().getNombre(), attr.getValor()))
                    .collect(Collectors.toList());
            response.setDetalles(atributos);
        } else {
            response.setDetalles(Collections.emptyList());
        }

        return response;
    }

    /**
     * Convierte una lista de entidades {@link ProductoAtributo} en una lista de objetos
     * {@link DetalleAtributoResponse}.
     *
     * @param atributos lista de entidades {@code ProductoAtributo}.
     * @return una lista de {@code DetalleAtributoResponse} con el nombre del atributo y su valor.
     */
    public static List<DetalleAtributoResponse> mapToDetalleAtributo(List<ProductoAtributo> atributos) {
        return atributos.stream()
                .map(atributo -> {
                    DetalleAtributoResponse dto = new DetalleAtributoResponse();
                    dto.setNombreAtributo(atributo.getAtributo().getNombre());
                    dto.setValor(atributo.getValor());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
