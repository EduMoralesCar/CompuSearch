package com.universidad.compusearch.config.initializer;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.universidad.compusearch.entity.EstadoSuscripcion;
import com.universidad.compusearch.entity.Plan;
import com.universidad.compusearch.entity.Suscripcion;
import com.universidad.compusearch.entity.Tienda;
import com.universidad.compusearch.repository.PlanRepository;
import com.universidad.compusearch.repository.SuscripcionRepository;
import com.universidad.compusearch.repository.TiendaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SuscripcionInitializer {

    private final PlanRepository planRepository;
    private final SuscripcionRepository suscripcionRepository;
    private final TiendaRepository tiendaRepository;

    /**
     * Inicializa los planes y asigna suscripciones a tiendas existentes.
     */
    public void init() {
        asignarSuscripcionesIniciales();
    }

    // --- Métodos de Asignación de Suscripciones ---

    /**
     * Asigna un plan de pago diferente a cada una de las 3 tiendas iniciales.
     */
    private void asignarSuscripcionesIniciales() {
        // La lista de tiendas es: tienda1@test.com, tienda2@test.com, tienda3@test.com
        List<Tienda> tiendas = tiendaRepository.findAll();
        List<Plan> planes = planRepository.findAll();

        // Aseguramos que haya suficientes tiendas y planes (excluyendo el plan Gratis)
        if (tiendas.size() < 3 || planes.size() < 4) {
            log.warn("No hay suficientes tiendas o planes disponibles para la asignación inicial.");
            return;
        }
        
        // Obtener los planes de pago
        Plan planEstandar = planes.stream().filter(p -> "Estándar".equals(p.getNombre())).findFirst().orElse(null);
        Plan planProfesional = planes.stream().filter(p -> "Profesional".equals(p.getNombre())).findFirst().orElse(null);
        Plan planEmpresarial = planes.stream().filter(p -> "Empresarial".equals(p.getNombre())).findFirst().orElse(null);

        // Asignar los planes si existen
        if (planEstandar != null) {
            Tienda tienda1 = tiendas.stream().filter(t -> "tienda1@test.com".equals(t.getEmail())).findFirst().orElse(null);
            if (tienda1 != null) {
                crearSuscripcion(tienda1, planEstandar);
            }
        }
        
        if (planProfesional != null) {
            Tienda tienda2 = tiendas.stream().filter(t -> "tienda2@test.com".equals(t.getEmail())).findFirst().orElse(null);
            if (tienda2 != null) {
                crearSuscripcion(tienda2, planProfesional);
            }
        }
        
        if (planEmpresarial != null) {
            Tienda tienda3 = tiendas.stream().filter(t -> "tienda3@test.com".equals(t.getEmail())).findFirst().orElse(null);
            if (tienda3 != null) {
                crearSuscripcion(tienda3, planEmpresarial);
            }
        }
    }

    /**
     * Crea y guarda una Suscripcion activa para una Tienda y un Plan.
     */
    private void crearSuscripcion(Tienda tienda, Plan plan) {
        // Verificar si la tienda ya tiene una suscripción activa
        if (suscripcionRepository.findByTiendaAndEstado(tienda, EstadoSuscripcion.ACTIVA).isPresent()) {
            log.warn("Tienda {} ya tiene una suscripción activa. No se asignó {}", tienda.getEmail(), plan.getNombre());
            return;
        }
        
        LocalDateTime fechaInicio = LocalDateTime.now().minusDays(5); // Inicia hace 5 días como ejemplo
        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setTienda(tienda);
        suscripcion.setPlan(plan);
        suscripcion.setFechaInicio(fechaInicio);
        
        // Calcular fecha de fin basada en la duración del plan
        if (plan.getDuracionMeses() > 0) {
            suscripcion.setFechaFin(fechaInicio.plusMonths(plan.getDuracionMeses()));
        } else {
            // Plan indefinido (como el Gratuito) - usar una fecha muy lejana
            suscripcion.setFechaFin(fechaInicio.plusYears(99)); 
        }

        suscripcion.setEstado(EstadoSuscripcion.ACTIVA);

        suscripcionRepository.save(suscripcion);
        log.info("Suscripción ACTIVA creada: Tienda {} -> Plan {}", tienda.getEmail(), plan.getNombre());
    }
}