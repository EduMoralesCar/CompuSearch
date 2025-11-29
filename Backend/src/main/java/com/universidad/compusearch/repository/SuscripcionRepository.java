package com.universidad.compusearch.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.EstadoSuscripcion;
import com.universidad.compusearch.entity.Suscripcion;
import com.universidad.compusearch.entity.Tienda;

public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long> {

        // Obtener suscripcion por tienda y estado
        Optional<Suscripcion> findByTiendaAndEstado(Tienda tienda, EstadoSuscripcion estadoSuscripcion);

        Optional<Suscripcion> findTopByTiendaIdUsuarioAndEstado(Long idUsuario, EstadoSuscripcion estadoSuscripcion);

        List<Suscripcion> findByPlanIdPlanAndEstado(Long idPlan, EstadoSuscripcion estadoSuscripcion);

        boolean existsByTiendaIdUsuarioAndEstado(Long idTienda, EstadoSuscripcion estado);

        Optional<Suscripcion> findByTiendaIdUsuarioAndEstado(Long idTienda, EstadoSuscripcion estado);

        List<Suscripcion> findByFechaFinBeforeAndEstadoIn(LocalDateTime fecha, List<EstadoSuscripcion> estados);

        Optional<Suscripcion> findByTiendaIdUsuarioAndEstadoIn(Long idTienda, List<EstadoSuscripcion> estados);

        Optional<Suscripcion> findFirstByTiendaIdUsuarioAndEstadoOrderByFechaInicioAsc(Long idTienda,
                        EstadoSuscripcion estado);

        Optional<Suscripcion> findTopByTiendaIdUsuarioAndEstadoNotInOrderByFechaInicioDesc(
                        Long idTienda,
                        List<EstadoSuscripcion> estadosIgnorados);

        Optional<Suscripcion> findTopByTiendaIdUsuarioAndEstadoInOrderByFechaInicioDesc(
                        Long idTienda,
                        List<EstadoSuscripcion> estadosIgnorados);

        Page<Suscripcion> findByTiendaIdUsuario(Long idUsuario, Pageable pageable);
}
