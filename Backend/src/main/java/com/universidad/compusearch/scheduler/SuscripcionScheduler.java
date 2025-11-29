package com.universidad.compusearch.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.EstadoSuscripcion;
import com.universidad.compusearch.entity.Plan;
import com.universidad.compusearch.entity.Suscripcion;
import com.universidad.compusearch.entity.Tienda;
import com.universidad.compusearch.repository.SuscripcionRepository;
import com.universidad.compusearch.service.PagoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SuscripcionScheduler {

    private final SuscripcionRepository suscripcionRepository;
    private final PagoService pagoService;

    @Scheduled(fixedRate = 60000)
    public void procesarSuscripciones() {

        LocalDateTime ahora = LocalDateTime.now();

        List<Suscripcion> finalizadas = suscripcionRepository.findByFechaFinBeforeAndEstadoIn(
                ahora,
                List.of(
                        EstadoSuscripcion.ACTIVA,
                        EstadoSuscripcion.CANCELADA_PROGRAMADA,
                        EstadoSuscripcion.CAMBIO_PROGRAMADO));

        for (Suscripcion sus : finalizadas) {

            switch (sus.getEstado()) {

                case CANCELADA_PROGRAMADA:
                    log.info("Finalizando suscripción cancelada programada {}", sus.getIdSuscripcion());
                    sus.setEstado(EstadoSuscripcion.CANCELADA);
                    suscripcionRepository.save(sus);
                    break;

                case CAMBIO_PROGRAMADO:
                    log.info("Aplicando cambio programado para {}", sus.getIdSuscripcion());
                    procesarCambioProgramado(sus);
                    break;

                case ACTIVA:
                    log.info("Renovando automáticamente {}", sus.getIdSuscripcion());
                    procesarRenovacionAutomatica(sus);
                    break;

                default:
                    break;
            }
        }
    }

    private void procesarCambioProgramado(Suscripcion activa) {

        activa.setEstado(EstadoSuscripcion.TERMINADA);
        suscripcionRepository.save(activa);

        Suscripcion nueva = suscripcionRepository
                .findFirstByTiendaIdUsuarioAndEstadoOrderByFechaInicioAsc(
                        activa.getTienda().getIdUsuario(),
                        EstadoSuscripcion.PENDIENTE)
                .orElse(null);

        if (nueva == null) {
            log.warn("No se encontró la nueva suscripción pendiente para activar.");
            return;
        }

        boolean pagado = pagoService.cobrarPago(nueva, true);

        nueva.setEstado(pagado ? EstadoSuscripcion.ACTIVA : EstadoSuscripcion.PENDIENTE);
        suscripcionRepository.save(nueva);
    }

    private void procesarRenovacionAutomatica(Suscripcion actual) {

        Plan plan = actual.getPlan();

        if (!plan.isActivo()) {
            log.warn("El plan {} está desactivado, no se renovará la suscripción {}",
                    plan.getNombre(), actual.getIdSuscripcion());

            actual.setEstado(EstadoSuscripcion.TERMINADA);
            suscripcionRepository.save(actual);
            return;
        }
        
        Tienda tienda = actual.getTienda();

        actual.setEstado(EstadoSuscripcion.TERMINADA);
        suscripcionRepository.save(actual);

        Suscripcion nueva = new Suscripcion();
        nueva.setPlan(plan);
        nueva.setTienda(tienda);
        nueva.setFechaInicio(LocalDateTime.now());
        nueva.setFechaFin(LocalDateTime.now().plusMonths(plan.getDuracionMeses()));
        nueva.setEstado(EstadoSuscripcion.PENDIENTE);
        suscripcionRepository.save(nueva);

        boolean pagado = pagoService.cobrarPago(nueva, true);

        nueva.setEstado(pagado ? EstadoSuscripcion.ACTIVA : EstadoSuscripcion.PENDIENTE);
        suscripcionRepository.save(nueva);
    }
}
