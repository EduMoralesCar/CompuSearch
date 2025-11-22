package com.universidad.compusearch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.universidad.compusearch.entity.Categoria;

/**
 * Repositorio para la entidad {@link Categoria}.
 * Proporciona operaciones CRUD y consultas específicas por nombre.
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /**
     * Busca una categoría por su nombre.
     *
     * @param nombre Nombre de la categoría.
     * @return {@link Optional} que contiene la categoría si existe, o vacío si no.
     */
    Optional<Categoria> findByNombre(String nombre);

    /**
     * Obtiene los nombres de todas las categorías.
     *
     * @return Lista de nombres de categorías.
     */
    @Query("SELECT c.nombre FROM Categoria c")
    List<String> findAllNombres();
}
