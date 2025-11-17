package com.universidad.compusearch.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.dto.PlanRequest;
import com.universidad.compusearch.dto.PlanResponse;
import com.universidad.compusearch.entity.Plan;
import com.universidad.compusearch.exception.PlanException;
import com.universidad.compusearch.repository.PlanRepository;
import com.universidad.compusearch.util.Mapper;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;

    // Obtener por nombre
    public Plan obtenerPorNombre(String nombre) {
        log.info("Obteniendo plan con el nombre {}", nombre);

        Plan plan = planRepository.findByNombre(nombre).orElse(null);
        if (plan == null) throw PlanException.notFound();

        return plan;
    }

    // Crear un plan
    @Transactional
    public PlanResponse crearPlan(PlanRequest request) {
        log.info("Iniciando creación de plan: {}", request.getNombre());

        if (planRepository.existsByNombreIgnoreCaseAndActivo(request.getNombre(), true)) {
            throw new ValidationException("Ya existe un plan ACTIVO con el nombre: " + request.getNombre());
        }

        Plan plan = new Plan();
        plan.setNombre(request.getNombre());
        plan.setDuracionMeses(request.getDuracionMeses());
        plan.setPrecioMensual(request.getPrecioMensual());
        plan.setDescripcion(request.getDescripcion());
        plan.setBeneficios(request.getBeneficios());
        plan.setActivo(true);

        Plan nuevoPlan = planRepository.save(plan);
        log.info("Plan ID {} creado exitosamente.", nuevoPlan.getIdPlan());
        return Mapper.mapToPlan(nuevoPlan);
    }

    // Obtener todos los planes
    @Transactional
    public Page<PlanResponse> obtenerTodosLosPlanes(Pageable pageable, String nombre, boolean incluirInactivos) {
        log.info("Obteniendo página {} de planes. Filtro nombre: {}. Incluir Inactivos: {}",
                pageable.getPageNumber(), nombre, incluirInactivos);

        Page<Plan> planesPage;
        boolean activoFilter = !incluirInactivos;

        if (nombre != null && !nombre.trim().isEmpty()) {
            if (incluirInactivos) {
                planesPage = planRepository.findByNombreContainingIgnoreCase(nombre, pageable);
            } else {
                planesPage = planRepository.findByNombreContainingIgnoreCaseAndActivo(nombre, activoFilter, pageable);
            }
        } else {
            if (incluirInactivos) {
                planesPage = planRepository.findAll(pageable); // Traer todos si se solicitan inactivos
            } else {
                planesPage = planRepository.findByActivo(activoFilter, pageable);
            }
        }

        return planesPage.map(Mapper::mapToPlan);
    }

    // Obtener plan por id
    @Transactional
    public PlanResponse obtenerPlanPorId(Long id) {
        log.info("Buscando plan con ID: {}", id);
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> PlanException.notFound());

        return Mapper.mapToPlan(plan);
    }

    // Actualizar el plan
    @Transactional
    public PlanResponse actualizarPlan(Long id, PlanRequest request) {
        log.info("Actualizando plan con ID: {}", id);
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> PlanException.notFound());

        if (!plan.getNombre().equalsIgnoreCase(request.getNombre()) &&
                planRepository.existsByNombreIgnoreCaseAndActivo(request.getNombre(), true)) {
            throw PlanException.exist();
        }

        plan.setNombre(request.getNombre());
        plan.setDuracionMeses(request.getDuracionMeses());
        plan.setPrecioMensual(request.getPrecioMensual());
        plan.setDescripcion(request.getDescripcion());
        plan.setBeneficios(request.getBeneficios());

        Plan planActualizado = planRepository.save(plan);
        log.info("Plan ID {} modificado exitosamente.", id);
        return Mapper.mapToPlan(planActualizado);
    }

    // Actualizar el estado del plan
    @Transactional
    public void actualizarEstadoActivo(Long id, boolean activo) {
        log.warn("Cambiando estado activo del plan ID {} a: {}", id, activo);
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> PlanException.notFound());

        if (plan.isActivo() == activo) {
            log.warn("El plan ID {} ya tiene el estado activo={}. No se requiere actualización.", id, activo);
            return;
        }

        plan.setActivo(activo);
        planRepository.save(plan);
        log.info("Estado del plan ID {} cambiado a activo={}.", id, activo);
    }
}
