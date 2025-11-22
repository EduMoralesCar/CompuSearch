package com.universidad.compusearch.specification;

import org.springframework.data.jpa.domain.Specification;

import com.universidad.compusearch.entity.ProductoTienda;

import jakarta.persistence.criteria.JoinType;

public class MemoriaSpecification {

    public static Specification<ProductoTienda> porFrecuencia(String frecuencia) {
        return (root, query, cb) -> {
            if (frecuencia == null) return null;

            query.distinct(true);

            var joinProducto = root.join("producto", JoinType.INNER);
            var joinProductoAtributos = joinProducto.join("atributos", JoinType.INNER);
            var joinAtributo = joinProductoAtributos.join("atributo", JoinType.INNER);

            return cb.and(
                cb.equal(joinAtributo.get("nombre"), "Frecuencia RAM"),
                cb.equal(joinProductoAtributos.get("valor"), frecuencia)
            );
        };
    }

    public static Specification<ProductoTienda> porTipo(String tipo) {
        return (root, query, cb) -> {
            if (tipo == null) return null;

            query.distinct(true);

            var joinProducto = root.join("producto", JoinType.INNER);
            var joinProductoAtributos = joinProducto.join("atributos", JoinType.INNER);
            var joinAtributo = joinProductoAtributos.join("atributo", JoinType.INNER);

            return cb.and(
                cb.equal(joinAtributo.get("nombre"), "Tipo RAM"),
                cb.equal(joinProductoAtributos.get("valor"), tipo)
            );
        };
    }

    public static Specification<ProductoTienda> porCapacidad(String capacidad) {
        return (root, query, cb) -> {
            if (capacidad == null) return null;

            query.distinct(true);

            var joinProducto = root.join("producto", JoinType.INNER);
            var joinProductoAtributos = joinProducto.join("atributos", JoinType.INNER);
            var joinAtributo = joinProductoAtributos.join("atributo", JoinType.INNER);

            return cb.and(
                cb.equal(joinAtributo.get("nombre"), "Capacidad RAM"),
                cb.equal(joinProductoAtributos.get("valor"), capacidad)
            );
        };
    }
}
