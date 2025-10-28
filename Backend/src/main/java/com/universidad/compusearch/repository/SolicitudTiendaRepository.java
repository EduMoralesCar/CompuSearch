package com.universidad.compusearch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.SolicitudTienda;

/**
 * Repositorio JPA para la entidad {@link SolicitudTienda}.
 * Permite operaciones CRUD y consultas específicas por usuario.
 */
public interface SolicitudTiendaRepository extends JpaRepository<SolicitudTienda, Long> {

    /**
     * Obtiene una página de solicitudes de tienda realizadas por un usuario específico.
     *
     * @param idUsuario el ID del usuario
     * @param pageable  la configuración de paginación
     * @return página de solicitudes del usuario
     */
    Page<SolicitudTienda> findByUsuario_IdUsuario(Long idUsuario, Pageable pageable);
}
