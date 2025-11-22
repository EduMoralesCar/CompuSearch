package com.universidad.compusearch.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.dto.PlanRequest;
import com.universidad.compusearch.dto.PlanResponse;
import com.universidad.compusearch.entity.Plan;
import com.universidad.compusearch.exception.PlanException;
import com.universidad.compusearch.repository.PlanRepository;
import com.universidad.compusearch.stripe.ProductStripeService;
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
    private final ProductStripeService productStripeService;

    // Obtener por nombre
    public Plan obtenerPorNombre(String nombre) {
        log.info("Obteniendo plan con el nombre {}", nombre);

        Plan plan = planRepository.findByNombre(nombre).orElse(null);
        if (plan == null)
            throw PlanException.notFound();

        return plan;
    }

    // Crear un plan
    @Transactional
    public PlanResponse crearPlan(PlanRequest request) {
        log.info("Iniciando creación de plan: {}", request.getNombre());

        if (planRepository.existsByNombreIgnoreCaseAndActivo(request.getNombre(), true)) {
            throw PlanException.exist(request.getNombre());
        }

        Plan plan = new Plan();
        plan.setNombre(request.getNombre());
        plan.setDuracionMeses(request.getDuracionMeses());
        plan.setPrecioMensual(request.getPrecioMensual());
        plan.setDescripcion(request.getDescripcion());
        plan.setBeneficios(request.getBeneficios());
        plan.setActivo(true);

        try {
            String productId = productStripeService.crearProductoStripe(plan);
            plan.setStripeProductId(productId);

            String priceId = productStripeService.crearPrecioStripe(plan, productId);
            plan.setStripePriceId(priceId);

        } catch (Exception e) {
            log.error("Error creando plan en Stripe: {}", e.getMessage(), e);
            throw PlanException.create();
        }

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
                planesPage = planRepository.findAll(pageable);
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

    public Plan findById(Long id) {
        log.info("Buscando plan con ID: {}", id);
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> PlanException.notFound());

        return plan;
    }

    // Actualizar el plan
    @Transactional
    public PlanResponse actualizarPlan(Long id, PlanRequest request) {
        log.info("Actualizando plan con ID: {}", id);
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> PlanException.notFound());

        if (!plan.getNombre().equalsIgnoreCase(request.getNombre()) &&
                planRepository.existsByNombreIgnoreCaseAndActivo(request.getNombre(), true)) {
            throw PlanException.exist(request.getNombre());
        }

        // boolean precioCambio = plan.getPrecioMensual().compareTo(request.getPrecioMensual()) != 0;

        // 1. Actualizar entidad local
        plan.setNombre(request.getNombre());
        plan.setDuracionMeses(request.getDuracionMeses());
        plan.setPrecioMensual(request.getPrecioMensual());
        plan.setDescripcion(request.getDescripcion());
        plan.setBeneficios(request.getBeneficios());

        try {
            productStripeService.actualizarProductoStripe(plan.getStripeProductId(), plan);
            /* 
            // Si el precio cambió, migrar suscripciones
            if (precioCambio) {

                String nuevoPriceId = productStripeService.crearNuevoPrecioStripe(plan, plan.getStripeProductId());
                plan.setStripePriceId(nuevoPriceId);

                log.warn("Precio cambiado. Migrando suscripciones activas...");

                List<Suscripcion> suscripcionesActivas =
                        suscripcionRepository.findByPlanIdPlanAndEstado(plan.getIdPlan(), EstadoSuscripcion.ACTIVA);

                for (Suscripcion suscripcion : suscripcionesActivas) {
                    try {
                        suscripcionService.actualizarSuscripcionConNuevoPrecio(
                                suscripcion.getIdSuscripcion(),
                                plan.getIdPlan()
                        );
                    } catch (Exception subEx) {
                        log.error("Error migrando suscripción {}: {}", suscripcion.getIdSuscripcion(), subEx.getMessage());
                    }
                }

                log.warn("Migración completada. Total: {}", suscripcionesActivas.size());
            }
                */

        } catch (Exception e) {
            log.error("Error al sincronizar con Stripe: {}", e.getMessage(), e);
            throw new ValidationException("Error al sincronizar cambios con Stripe");
        }

        Plan planActualizado = planRepository.save(plan);
        log.info("Plan ID {} modificado exitosamente.", id);
        return Mapper.mapToPlan(planActualizado);
    }

    // Actualizar estado activo
    @Transactional
    public void actualizarEstadoActivo(Long id, boolean activo) {
        log.warn("Cambiando estado activo del plan ID {} a {}", id, activo);

        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> PlanException.notFound());

        if (plan.isActivo() == activo) {
            log.warn("Plan ID {} ya tiene activo={}", id, activo);
            return;
        }

        plan.setActivo(activo);
        planRepository.save(plan);

        log.info("Estado del plan ID {} actualizado a activo={}", id, activo);
    }
}
