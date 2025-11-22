package com.universidad.compusearch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.DetalleBuild;

/**
 * Repositorio para la entidad {@link DetalleBuild}.
 * Proporciona operaciones CRUD y consultas específicas relacionadas con los detalles de builds.
 */
public interface DetalleBuildRepository extends JpaRepository<DetalleBuild, Long> {

    /**
     * Obtiene todos los detalles asociados a una build específica por su id.
     *
     * @param idBuild ID de la build.
     * @return Lista de {@link DetalleBuild} asociados a la build.
     */
    List<DetalleBuild> findByBuild_IdBuild(Long idBuild);
}
