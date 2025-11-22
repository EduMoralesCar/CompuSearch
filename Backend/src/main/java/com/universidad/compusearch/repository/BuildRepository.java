package com.universidad.compusearch.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.universidad.compusearch.entity.Build;

/**
 * Repositorio para la entidad {@link Build}.
 * Proporciona operaciones CRUD y consultas específicas por ID y usuario.
 */
public interface BuildRepository extends JpaRepository<Build, Long> {

    /**
     * Busca una build por su ID.
     *
     * @param idBuild ID de la build.
     * @return {@link Optional} que contiene la build si existe, o vacío si no.
     */
    Optional<Build> findByIdBuild(long idBuild);

    /**
     * Obtiene todas las builds asociadas a un usuario, paginadas.
     *
     * @param idUsuario ID del usuario.
     * @param pageable Información de paginación.
     * @return {@link Page} con las builds del usuario.
     */
    @Query("SELECT b FROM Build b WHERE b.usuario.id = :idUsuario")
    Page<Build> findAllByUsuarioId(Long idUsuario, Pageable pageable);
}
