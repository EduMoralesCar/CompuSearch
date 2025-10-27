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


public class ProductoTiendaMapper {

    // Conversión de entidad a DTO de respuesta
    public static ProductoTiendaResponse mapToResponse(ProductoTienda productoTienda) {
        return new ProductoTiendaResponse(
                productoTienda.getIdProductoTienda(),
                productoTienda.getProducto().getNombre(),
                productoTienda.getPrecio(),
                productoTienda.getStock(),
                productoTienda.getUrlImagen(),
                productoTienda.getTienda().getNombre());
    }

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
