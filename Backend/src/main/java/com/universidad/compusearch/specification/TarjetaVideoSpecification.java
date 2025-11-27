package com.universidad.compusearch.specification;

import org.springframework.data.jpa.domain.Specification;

import com.universidad.compusearch.entity.ProductoTienda;

import jakarta.persistence.criteria.JoinType;

public class TarjetaVideoSpecification {

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
