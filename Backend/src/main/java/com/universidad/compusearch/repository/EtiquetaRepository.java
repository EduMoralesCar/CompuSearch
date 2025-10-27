package com.universidad.compusearch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.Etiqueta;

/**
 * Repositorio para la entidad {@link Etiqueta}.
 * Proporciona operaciones CRUD y consultas específicas sobre etiquetas.
 */
public interface EtiquetaRepository extends JpaRepository<Etiqueta, Long> {

    /**
     * Busca una etiqueta por su nombre exacto.
     *
     * @param nombre Nombre de la etiqueta a buscar.
     * @return {@link Optional} que contiene la etiqueta si existe, o vacío si no.
     */
    Optional<Etiqueta> findByNombre(String nombre);
}
