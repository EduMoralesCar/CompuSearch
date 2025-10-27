package com.universidad.compusearch.specification;

import org.springframework.data.jpa.domain.Specification;

import com.universidad.compusearch.entity.ProductoTienda;

import jakarta.persistence.criteria.JoinType;

/**
 * Clase que define especificaciones de filtrado para productos de tipo
 * <strong>Fuente de Poder (PSU)</strong>.
 * 
 * <p>Estas {@link Specification Specifications} permiten construir consultas
 * dinámicas sobre la entidad {@link ProductoTienda}, aplicando filtros por
 * atributos técnicos específicos de las fuentes de poder.</p>
 * 
 * <p>Actualmente incluye filtros por <strong>potencia</strong> y
 * <strong>certificación</strong>.</p>
 * 
 * Ejemplo de uso:
 * <pre>{@code
 * Specification<ProductoTienda> spec =
 *     Specification.where(FuentePoderSpecification.porPotencia("750W"))
 *                  .and(FuentePoderSpecification.porCertificacion("80 Plus Gold"));
 * }</pre>
 * 
 */
public class FuentePoderSpecification {

    /**
     * Crea una especificación que filtra fuentes de poder según su potencia.
     * 
     * <p>Busca dentro de los atributos del producto el campo con nombre
     * <strong>"Potencia PSU"</strong> y compara su valor con la potencia
     * proporcionada.</p>
     * 
     * @param potencia la potencia de la fuente (por ejemplo, "650W", "750W", "1000W").
     * @return una {@link Specification} que filtra por potencia, o {@code null} si el
     *         parámetro es {@code null}.
     */
    public static Specification<ProductoTienda> porPotencia(String potencia) {
        return (root, query, cb) -> {
            if (potencia == null) return null;

            query.distinct(true);

            var joinProducto = root.join("producto", JoinType.INNER);
            var joinProductoAtributos = joinProducto.join("atributos", JoinType.INNER);
            var joinAtributo = joinProductoAtributos.join("atributo", JoinType.INNER);

            return cb.and(
                cb.equal(joinAtributo.get("nombre"), "Potencia PSU"),
                cb.equal(joinProductoAtributos.get("valor"), potencia)
            );
        };
    }

    /**
     * Crea una especificación que filtra fuentes de poder según su certificación de eficiencia.
     * 
     * <p>Busca dentro de los atributos del producto el campo con nombre
     * <strong>"Certificación PSU"</strong> y compara su valor con la certificación indicada.</p>
     * 
     * @param certificacion la certificación de eficiencia (por ejemplo,
     *                      "80 Plus Bronze", "80 Plus Gold", "80 Plus Platinum").
     * @return una {@link Specification} que filtra por certificación, o {@code null} si el
     *         parámetro es {@code null}.
     */
    public static Specification<ProductoTienda> porCertificacion(String certificacion) {
        return (root, query, cb) -> {
            if (certificacion == null) return null;

            query.distinct(true);

            var joinProducto = root.join("producto", JoinType.INNER);
            var joinProductoAtributos = joinProducto.join("atributos", JoinType.INNER);
            var joinAtributo = joinProductoAtributos.join("atributo", JoinType.INNER);

            return cb.and(
                cb.equal(joinAtributo.get("nombre"), "Certificación PSU"),
                cb.equal(joinProductoAtributos.get("valor"), certificacion)
            );
        };
    }
}
