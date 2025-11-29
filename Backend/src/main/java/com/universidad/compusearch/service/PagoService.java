package com.universidad.compusearch.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.dto.PagoHistorialResponse;
import com.universidad.compusearch.entity.EstadoPago;
import com.universidad.compusearch.entity.Pago;
import com.universidad.compusearch.entity.Suscripcion;
import com.universidad.compusearch.repository.PagoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PagoService {

    private final PagoRepository pagoRepository;

    public boolean cobrarPago(Suscripcion sus, boolean aceptar) {
        log.info("Generando pago por la suscripcion del plan {}", sus.getPlan().getNombre());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error inesperado al interrumpir la operación.");
        }

        return procesarPago(sus, aceptar);
    }

    @Transactional
    protected boolean procesarPago(Suscripcion sus, boolean aceptar) {
        Pago pago = new Pago();
        pago.setMonto(sus.getPlan().getPrecioMensual());
        pago.setIdOperacion(UUID.randomUUID().toString());
        pago.setFechaPago(LocalDateTime.now());
        pago.setSuscripcion(sus);
        pago.setEstadoPago(EstadoPago.PENDIENTE);

        if (aceptar) {
            pago.setEstadoPago(EstadoPago.COMPLETADO);
            log.info("Pago realizado correctamente");
        } else {
            pago.setEstadoPago(EstadoPago.FALLIDO);
            log.info("Pago fallido, intente más tarde");
        }

        pagoRepository.save(pago);

        return aceptar;
    }

    public Page<PagoHistorialResponse> obtenerHistorialPagos(Long idTienda, Pageable pageable) {
        Page<Pago> pagosPage = pagoRepository.findBySuscripcionTiendaIdUsuario(idTienda, pageable);

        return pagosPage.map(pago -> {
            PagoHistorialResponse resp = new PagoHistorialResponse();
            resp.setId(pago.getIdPago());
            resp.setNombrePlan(pago.getSuscripcion().getPlan().getNombre());
            resp.setPrecio(pago.getMonto());
            resp.setFechaInicioSuscripcion(pago.getSuscripcion().getFechaInicio());
            resp.setFechaFinSuscripcion(pago.getSuscripcion().getFechaFin());
            resp.setEstadoPago(pago.getEstadoPago().name());
            resp.setFechaPago(pago.getFechaPago());
            resp.setTransactionId(pago.getIdOperacion());
            return resp;
        });
    }
}
