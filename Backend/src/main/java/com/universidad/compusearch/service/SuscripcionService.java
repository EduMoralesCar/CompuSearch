package com.universidad.compusearch.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.EstadoSuscripcion;
import com.universidad.compusearch.entity.Suscripcion;
import com.universidad.compusearch.repository.SuscripcionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
@RequiredArgsConstructor
public class SuscripcionService {

    private final SuscripcionRepository suscripcionRepository;

    // Guardar suscripcion
    public void guardarSuscripcion(Suscripcion suscripcion) {
        log.info("Guardando suscripcion de la tienda {}", suscripcion.getTienda().getNombre());
        suscripcionRepository.save(suscripcion);
    }

    public List<Suscripcion> buscarSuscripcionesActivasPorPlanId(Long idPlan) {
        return suscripcionRepository.findByPlanIdPlanAndEstado(idPlan, EstadoSuscripcion.ACTIVA);
    }
}