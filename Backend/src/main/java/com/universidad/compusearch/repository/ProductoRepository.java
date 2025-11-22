package com.universidad.compusearch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.Producto;

/**
 * Repositorio para la entidad {@link Producto}.
 * Permite realizar operaciones CRUD y búsquedas específicas por nombre o
 * categoría.
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Obtiene un producto por su nombre exacto.
     *
     * @param nombre Nombre del producto.
     * @return Optional con el {@link Producto} encontrado, vacío si no existe.
     */
    Optional<Producto> findByNombre(String nombre);

    /**
     * Obtiene todos los productos asociados a una categoría específica.
     *
     * @param idCategoria ID de la categoría.
     * @return Lista de {@link Producto} pertenecientes a la categoría indicada.
     */
    List<Producto> findByCategoria_IdCategoria(long idCategoria);

    /**
     * Verifica si existe al menos un producto asociado a una categoría.
     *
     * @param idCategoria ID de la categoría a verificar
     * @return true si existe al menos un producto con la categoría indicada, false
     *         en caso contrario
     */
    boolean existsByCategoria_IdCategoria(Long idCategoria);

}
