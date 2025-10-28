package com.universidad.compusearch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.ProductoAtributo;

/**
 * Repositorio para la entidad {@link ProductoAtributo}.
 * Permite realizar operaciones CRUD y búsquedas de atributos asociados a productos.
 */
public interface ProductoAtributoRepository extends JpaRepository<ProductoAtributo, Long> {

    /**
     * Obtiene todos los atributos asociados a un producto específico.
     *
     * @param idProducto ID del producto.
     * @return Lista de {@link ProductoAtributo} asociados al producto indicado.
     */
    List<ProductoAtributo> findByProducto_idProducto(long idProducto);
}
