package com.universidad.compusearch.specification;

import org.springframework.data.jpa.domain.Specification;

import com.universidad.compusearch.entity.ProductoTienda;

import jakarta.persistence.criteria.JoinType;

public class RefrigeracionSpecification {
    public static Specification<ProductoTienda> porTipo(String tipo) {
        return (root, query, cb) -> {
            if (tipo == null)
                return null;

            query.distinct(true);

            var joinProducto = root.join("producto", JoinType.INNER);
            var joinProductoAtributos = joinProducto.join("atributos", JoinType.INNER);
            var joinAtributo = joinProductoAtributos.join("atributo", JoinType.INNER);

            return cb.and(
                    cb.equal(joinAtributo.get("nombre"), "Tipo de Enfriamiento"),
                    cb.equal(joinProductoAtributos.get("valor"), tipo));
        };
    }

    public static Specification<ProductoTienda> porCompatibilidad(String compatibilidad) {
        return (root, query, cb) -> {
            if (compatibilidad == null)
                return null;

            query.distinct(true);

            var joinProducto = root.join("producto", JoinType.INNER);
            var joinProductoAtributos = joinProducto.join("atributos", JoinType.INNER);
            var joinAtributo = joinProductoAtributos.join("atributo", JoinType.INNER);

            return cb.and(
                    cb.equal(joinAtributo.get("nombre"), "Compatibilidad Socket"),
                    cb.equal(joinProductoAtributos.get("valor"), compatibilidad));
        };
    }
}
