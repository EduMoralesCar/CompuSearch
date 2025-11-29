package com.universidad.compusearch.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.dto.SuscripcionActualResponse;
import com.universidad.compusearch.dto.SuscripcionResponse;
import com.universidad.compusearch.entity.EstadoSuscripcion;
import com.universidad.compusearch.entity.Plan;
import com.universidad.compusearch.entity.Suscripcion;
import com.universidad.compusearch.entity.Tienda;
import com.universidad.compusearch.exception.PlanException;
import com.universidad.compusearch.exception.SuscripcionException;
import com.universidad.compusearch.exception.TiendaException;
import com.universidad.compusearch.repository.PlanRepository;
import com.universidad.compusearch.repository.SuscripcionRepository;
import com.universidad.compusearch.repository.TiendaRepository;
import com.universidad.compusearch.util.Mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SuscripcionService {

    private final SuscripcionRepository suscripcionRepository;
    private final PlanRepository planRepository;
    private final TiendaRepository tiendaRepository;
    private final PagoService pagoService;

    public void asociarSuscripcion(Long idPlan, Long idTienda, boolean accion) {

        Plan plan = planRepository.findById(idPlan)
                .orElseThrow(PlanException::notFound);

        Tienda tienda = tiendaRepository.findById(idTienda)
                .orElseThrow(TiendaException::notFound);

        if (!plan.isActivo()) {
            throw PlanException.inactive(plan.getNombre());
        }

        // Verificar suscripción activa que bloquee la creación de una nueva
        Suscripcion susActiva = suscripcionRepository
                .findByTiendaIdUsuarioAndEstadoIn(idTienda,
                        List.of(EstadoSuscripcion.ACTIVA, EstadoSuscripcion.CAMBIO_PROGRAMADO))
                .orElse(null);

        if (susActiva != null && susActiva.getEstado() != EstadoSuscripcion.CANCELADA_PROGRAMADA) {
            // Hay una suscripción bloqueante que no está programada para cancelarse
            throw SuscripcionException.active();
        }

        // No permitir otra PENDIENTE
        if (suscripcionRepository.existsByTiendaIdUsuarioAndEstado(idTienda, EstadoSuscripcion.PENDIENTE)) {
            throw SuscripcionException.existsPendiente();
        }

        // Crear nueva suscripción
        Suscripcion nueva = new Suscripcion();
        nueva.setFechaInicio(LocalDateTime.now());
        nueva.setFechaFin(LocalDateTime.now().plusMonths(plan.getDuracionMeses()));
        nueva.setPlan(plan);
        nueva.setTienda(tienda);

        // Si la suscripción actual está cancelada programada o no hay activa, se pone
        // PENDIENTE
        nueva.setEstado(EstadoSuscripcion.PENDIENTE);
        suscripcionRepository.save(nueva);

        // Intentar cobrar
        boolean pagado = pagoService.cobrarPago(nueva, accion);

        if (pagado) {
            // Solo activar inmediatamente si no hay suscripción bloqueante
            if (susActiva == null || susActiva.getEstado() == EstadoSuscripcion.CANCELADA_PROGRAMADA) {
                nueva.setEstado(EstadoSuscripcion.ACTIVA);
            } else {
                // Mantener pendiente si la suscripción bloqueante sigue vigente
                nueva.setEstado(EstadoSuscripcion.PENDIENTE);
            }
        } else {
            nueva.setEstado(EstadoSuscripcion.RECHAZADA);
        }

        suscripcionRepository.save(nueva);

        log.info("Nueva suscripción asociada a la tienda {}: estado {}", tienda.getNombre(), nueva.getEstado());
    }

    public void cancelarSuscripcion(Long idTienda) {
        Tienda tienda = tiendaRepository.findById(idTienda)
                .orElseThrow(TiendaException::notFound);

        // Buscar suscripción actual (ACTIVA o CAMBIO_PROGRAMADO)
        Suscripcion actual = suscripcionRepository
                .findByTiendaIdUsuarioAndEstadoIn(idTienda,
                        List.of(EstadoSuscripcion.ACTIVA, EstadoSuscripcion.CAMBIO_PROGRAMADO))
                .orElse(null);

        if (actual != null) {
            actual.setEstado(EstadoSuscripcion.CANCELADA_PROGRAMADA);
            suscripcionRepository.save(actual);
        }

        // Buscar suscripción pendiente
        Suscripcion pendiente = suscripcionRepository
                .findByTiendaIdUsuarioAndEstado(idTienda, EstadoSuscripcion.PENDIENTE)
                .orElse(null);

        if (pendiente != null) {
            // Opcional: cancelar también el pendiente si la política es así
            pendiente.setEstado(EstadoSuscripcion.CANCELADA);
            suscripcionRepository.save(pendiente);
        }

        log.info("Cancelación de suscripción procesada para la tienda {}", tienda.getNombre());
    }

    public void cambiarSuscripcion(Long idPlanNuevo, Long idTienda, boolean accion) {
        Plan nuevoPlan = planRepository.findById(idPlanNuevo)
                .orElseThrow(PlanException::notFound);
        Tienda tienda = tiendaRepository.findById(idTienda)
                .orElseThrow(TiendaException::notFound);

        if (!nuevoPlan.isActivo()) {
            throw PlanException.inactive(nuevoPlan.getNombre());
        }

        // Buscar suscripción bloqueante: ACTIVA, CAMBIO_PROGRAMADO o
        // CANCELADA_PROGRAMADA
        Suscripcion bloqueante = suscripcionRepository
                .findByTiendaIdUsuarioAndEstadoIn(idTienda,
                        List.of(EstadoSuscripcion.ACTIVA, EstadoSuscripcion.CAMBIO_PROGRAMADO,
                                EstadoSuscripcion.CANCELADA_PROGRAMADA))
                .orElse(null);

        // Verificar que no exista pendiente
        if (suscripcionRepository.existsByTiendaIdUsuarioAndEstado(idTienda, EstadoSuscripcion.PENDIENTE)) {
            throw SuscripcionException.existsPendiente();
        }

        Suscripcion nueva = new Suscripcion();
        nueva.setPlan(nuevoPlan);
        nueva.setTienda(tienda);
        nueva.setFechaInicio(LocalDateTime.now());
        nueva.setFechaFin(LocalDateTime.now().plusMonths(nuevoPlan.getDuracionMeses()));

        if (bloqueante == null) {
            // No hay suscripción bloqueante: cobrar y activar
            nueva.setEstado(EstadoSuscripcion.PENDIENTE);
            suscripcionRepository.save(nueva);

            boolean correcto = pagoService.cobrarPago(nueva, accion);

            if (correcto) {
                nueva.setEstado(EstadoSuscripcion.ACTIVA);
            } else {
                nueva.setEstado(EstadoSuscripcion.RECHAZADA);
            }

            suscripcionRepository.save(nueva);
            log.info("Nueva suscripción activada porque no había bloqueante.");
            return;
        }

        // Si el bloqueante está en CANCELADA_PROGRAMADA: crear pendiente, no activar
        if (bloqueante.getEstado() == EstadoSuscripcion.CANCELADA_PROGRAMADA) {
            nueva.setEstado(EstadoSuscripcion.PENDIENTE);
            suscripcionRepository.save(nueva);
            log.info("Nueva suscripción creada como PENDIENTE porque la suscripción actual está cancelada programada.");
            return;
        }

        // Si el bloqueante está ACTIVA o CAMBIO_PROGRAMADO: programar cambio
        bloqueante.setEstado(EstadoSuscripcion.CAMBIO_PROGRAMADO);
        suscripcionRepository.save(bloqueante);

        nueva.setEstado(EstadoSuscripcion.PENDIENTE);
        suscripcionRepository.save(nueva);

        log.info("Cambio de plan programado para cuando termine la suscripción actual.");
    }

    public SuscripcionActualResponse obtenerSuscripcionActual(Long idTienda) {
        log.info("Obteniendo suscripción actual para tienda {}", idTienda);

        tiendaRepository.findById(idTienda)
                .orElseThrow(TiendaException::notFound);

        Suscripcion actual = suscripcionRepository
                .findTopByTiendaIdUsuarioAndEstadoInOrderByFechaInicioDesc(idTienda,
                        List.of(EstadoSuscripcion.ACTIVA, EstadoSuscripcion.CAMBIO_PROGRAMADO,
                                EstadoSuscripcion.CANCELADA_PROGRAMADA))
                .orElse(null);

        Suscripcion pendiente = suscripcionRepository
                .findTopByTiendaIdUsuarioAndEstado(idTienda, EstadoSuscripcion.PENDIENTE)
                .orElse(null);

        SuscripcionActualResponse susActual = new SuscripcionActualResponse();
        susActual.setActual(Mapper.mapToSuscripcion(actual));
        susActual.setPendiente(Mapper.mapToSuscripcion(pendiente));

        return susActual;
    }

    public Page<SuscripcionResponse> obtenerSuscripcionesPorUsuario(Long idTienda, int page, int size) {
        log.info("Obteniendo suscripciones para tienda {} (page: {}, size: {})", idTienda, page, size);

        tiendaRepository.findById(idTienda)
                .orElseThrow(() -> {
                    log.warn("Tienda con id {} no encontrada", idTienda);
                    return TiendaException.notFound();
                });

        Pageable pageable = PageRequest.of(page, size);

        Page<Suscripcion> suscripciones = suscripcionRepository.findByTiendaIdUsuario(idTienda, pageable);

        log.info("Se encontraron {} suscripciones para la tienda {}", suscripciones.getTotalElements(), idTienda);

        return suscripciones.map(Mapper::mapToSuscripcion);
    }
}