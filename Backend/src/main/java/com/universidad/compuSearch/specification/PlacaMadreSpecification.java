package com.universidad.compusearch.specification;

import org.springframework.data.jpa.domain.Specification;

import com.universidad.compusearch.entity.ProductoTienda;

import jakarta.persistence.criteria.JoinType;

public class PlacaMadreSpecification {
    public static Specification<ProductoTienda> porSocket(String socket) {
        return (root, query, cb) -> {
            if (socket == null) return null;

            query.distinct(true);

            var joinProducto = root.join("producto", JoinType.INNER);
            var joinProductoAtributos = joinProducto.join("atributos", JoinType.INNER);
            var joinAtributo = joinProductoAtributos.join("atributo", JoinType.INNER);

            return cb.and(
                cb.equal(joinAtributo.get("nombre"), "Socket"),
                cb.equal(joinProductoAtributos.get("valor"), socket)
            );
        };
    }

    public static Specification<ProductoTienda> porFactor(String factor) {
        return (root, query, cb) -> {
            if (factor == null) return null;

            query.distinct(true);

            var joinProducto = root.join("producto", JoinType.INNER);
            var joinProductoAtributos = joinProducto.join("atributos", JoinType.INNER);
            var joinAtributo = joinProductoAtributos.join("atributo", JoinType.INNER);

            return cb.and(
                cb.equal(joinAtributo.get("nombre"), "Factor de Forma"),
                cb.equal(joinProductoAtributos.get("valor"), factor)
            );
        };
    }
}
