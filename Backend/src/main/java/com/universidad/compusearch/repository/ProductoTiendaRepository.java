package com.universidad.compusearch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.universidad.compusearch.entity.ProductoTienda;

/**
 * Repositorio para la entidad {@link ProductoTienda}.
 * Permite realizar operaciones CRUD, búsquedas específicas y consultas con
 * filtros dinámicos.
 */
public interface ProductoTiendaRepository extends JpaRepository<ProductoTienda, Long>,
                JpaSpecificationExecutor<ProductoTienda> {

        /**
         * Obtiene un ProductoTienda específico dado el ID del producto y el ID del
         * usuario (tienda).
         *
         * @param idProducto ID del producto.
         * @param idTienda   ID del usuario que representa la tienda.
         * @return Optional con el ProductoTienda encontrado, vacío si no existe.
         */
        Optional<ProductoTienda> findByProducto_IdProductoAndTienda_IdUsuario(Long idProducto, Long idTienda);

        /**
         * Obtiene el rango de precios (mínimo y máximo) de productos habilitados en una
         * categoría específica.
         *
         * @param nombreCategoria Nombre de la categoría; si es null, considera todas
         *                        las categorías.
         * @return Object[] donde [0] = precio mínimo y [1] = precio máximo; null si no
         *         hay productos.
         */
        @Query("""
                        SELECT MIN(pt.precio), MAX(pt.precio)
                        FROM ProductoTienda pt
                        JOIN pt.producto p
                        JOIN p.categoria c
                        WHERE pt.habilitado = true
                        AND (:nombreCategoria IS NULL OR c.nombre = :nombreCategoria)
                        """)
        Object obtenerRangoPrecioPorCategoria(@Param("nombreCategoria") String nombreCategoria);

        /**
         * Obtiene la lista de marcas distintas de productos habilitados en una
         * categoría específica.
         *
         * @param nombreCategoria Nombre de la categoría; si es null, considera todas
         *                        las categorías.
         * @return Lista de nombres de marcas distintas.
         */
        @Query("""
                        SELECT DISTINCT p.marca
                        FROM ProductoTienda pt
                        JOIN pt.producto p
                        JOIN p.categoria c
                        WHERE pt.habilitado = true
                        AND (:nombreCategoria IS NULL OR c.nombre = :nombreCategoria)
                        """)
        List<String> findDistinctMarcasByCategoria(@Param("nombreCategoria") String nombreCategoria);

        /**
         * Obtiene la lista de nombres de tiendas que tienen productos habilitados en
         * una categoría específica.
         *
         * @param nombreCategoria Nombre de la categoría; si es null, considera todas
         *                        las categorías.
         * @return Lista de nombres de tiendas distintas.
         */
        @Query("""
                        SELECT DISTINCT t.nombre
                        FROM ProductoTienda pt
                        JOIN pt.tienda t
                        JOIN pt.producto p
                        JOIN p.categoria c
                        WHERE pt.habilitado = true
                        AND (:nombreCategoria IS NULL OR c.nombre = :nombreCategoria)
                        """)
        List<String> findDistinctTiendasWithHabilitadosByCategoria(@Param("nombreCategoria") String nombreCategoria);

        /**
         * Obtiene un ProductoTienda específico dado el nombre del producto y el nombre
         * de la tienda.
         *
         * @param nombreProducto Nombre del producto.
         * @param nombreTienda   Nombre de la tienda.
         * @return Optional con el ProductoTienda encontrado, vacío si no existe.
         */
        @Query("""
                        SELECT pt
                        FROM ProductoTienda pt
                        JOIN pt.producto p
                        JOIN pt.tienda t
                        WHERE pt.habilitado = true
                        AND p.nombre = :nombreProducto
                        AND t.nombre = :nombreTienda
                        """)
        Optional<ProductoTienda> findByNombreProductoAndNombreTienda(
                        @Param("nombreProducto") String nombreProducto,
                        @Param("nombreTienda") String nombreTienda);

        /**
         * Obtiene todos los ProductosTienda habilitados con un nombre de producto
         * específico.
         *
         * @param nombreProducto Nombre del producto.
         * @return Lista de ProductosTienda que coinciden con el nombre.
         */
        @Query("""
                        SELECT pt
                        FROM ProductoTienda pt
                        JOIN pt.producto p
                        JOIN pt.tienda t
                        WHERE pt.habilitado = true
                        AND p.nombre = :nombreProducto
                        """)
        List<ProductoTienda> findByNombreProducto(@Param("nombreProducto") String nombreProducto);
}
