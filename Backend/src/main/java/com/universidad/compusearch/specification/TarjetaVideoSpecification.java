package com.universidad.compusearch.specification;

import org.springframework.data.jpa.domain.Specification;

import com.universidad.compusearch.entity.ProductoTienda;

import jakarta.persistence.criteria.JoinType;

/**
 * Clase que define especificaciones JPA para realizar filtros dinámicos
 * sobre productos del tipo <b>tarjeta de video</b> dentro del sistema CompuSearch.
 * 
 * <p>Estas especificaciones permiten construir consultas condicionales 
 * mediante la API de Criteria de JPA, integrándose con Spring Data JPA 
 * a través de la interfaz {@link Specification}.</p>
 * 
 * <p>Cada método genera una condición de búsqueda específica según los atributos
 * del producto, como el fabricante o la cantidad de memoria VRAM.</p>
 * 
 * <p>Esta clase no debe ser instanciada directamente.</p>
 * 
 * @see org.springframework.data.jpa.domain.Specification
 * @see com.universidad.compusearch.entity.ProductoTienda
 */
public class TarjetaVideoSpecification {

    /**
     * Genera una especificación JPA para filtrar productos por el fabricante de la GPU.
     * 
     * <p>Realiza un <i>join</i> entre {@code ProductoTienda}, {@code Producto},
     * {@code ProductoAtributo} y {@code Atributo} para buscar coincidencias
     * donde el nombre del atributo sea <b>"Fabricante GPU"</b> y el valor coincida
     * con el parámetro proporcionado.</p>
     *
     * @param fabricante el nombre del fabricante de la GPU (por ejemplo, {@code "NVIDIA"} o {@code "AMD"}).
     * @return una {@link Specification} que filtra por fabricante, o {@code null} si el parámetro es {@code null}.
     */
    public static Specification<ProductoTienda> porFabricante(String fabricante) {
        return (root, query, cb) -> {
            if (fabricante == null) return null;

            query.distinct(true);

            var joinProducto = root.join("producto", JoinType.INNER);
            var joinProductoAtributos = joinProducto.join("atributos", JoinType.INNER);
            var joinAtributo = joinProductoAtributos.join("atributo", JoinType.INNER);

            return cb.and(
                cb.equal(joinAtributo.get("nombre"), "Fabricante GPU"),
                cb.equal(joinProductoAtributos.get("valor"), fabricante)
            );
        };
    }

    /**
     * Genera una especificación JPA para filtrar productos por la cantidad de memoria VRAM.
     * 
     * <p>Realiza un <i>join</i> entre {@code ProductoTienda}, {@code Producto},
     * {@code ProductoAtributo} y {@code Atributo} para buscar coincidencias
     * donde el nombre del atributo sea <b>"Memoria VRAM"</b> y el valor coincida
     * con el parámetro proporcionado.</p>
     *
     * @param memoriaVRAM la cantidad de memoria VRAM (por ejemplo, {@code "8GB"} o {@code "12GB"}).
     * @return una {@link Specification} que filtra por memoria VRAM, o {@code null} si el parámetro es {@code null}.
     */
    public static Specification<ProductoTienda> porMemoriaVRAM(String memoriaVRAM) {
        return (root, query, cb) -> {
            if (memoriaVRAM == null) return null;

            query.distinct(true);

            var joinProducto = root.join("producto", JoinType.INNER);
            var joinProductoAtributos = joinProducto.join("atributos", JoinType.INNER);
            var joinAtributo = joinProductoAtributos.join("atributo", JoinType.INNER);

            return cb.and(
                cb.equal(joinAtributo.get("nombre"), "Memoria VRAM"),
                cb.equal(joinProductoAtributos.get("valor"), memoriaVRAM)
            );
        };
    }
}
