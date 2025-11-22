package com.universidad.compusearch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.Incidente;

/**
 * Repositorio para la entidad {@link Incidente}.
 * Proporciona operaciones CRUD y consultas específicas sobre incidentes de usuarios.
 */
public interface IncidenteRepository extends JpaRepository<Incidente, Long> {

    /**
     * Obtiene una página de incidentes asociados a un usuario específico.
     *
     * @param idUsuario ID del usuario.
     * @param pageable  Objeto de paginación que define página, tamaño y orden.
     * @return Página de {@link Incidente} asociados al usuario indicado.
     */
    Page<Incidente> findAllByUsuario_IdUsuario(Long idUsuario, Pageable pageable);
}
