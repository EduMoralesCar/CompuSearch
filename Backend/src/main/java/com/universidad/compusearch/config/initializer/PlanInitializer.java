package com.universidad.compusearch.config.initializer;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.universidad.compusearch.entity.Plan;
import com.universidad.compusearch.repository.PlanRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PlanInitializer {

    private final PlanRepository planRepository;

    public void init() {

        crearPlan(
                "Básico (Gratis)",
                0,
                BigDecimal.ZERO,
                "Plan gratuito con acceso limitado a funcionalidades básicas.",
                "✔ Acceso al catálogo público\n✔ Comparación básica de productos\n✖ Sin acceso a métricas avanzadas\n✖ Sin soporte prioritario");
        /* 
        crearPlan(
                "Estándar",
                1,
                new BigDecimal("9.99"),
                "Ideal para tiendas pequeñas que desean una mejor visibilidad.",
                "✔ Incluye métricas básicas\n✔ Soporte por correo\n✔ Acceso a integraciones limitadas");

        crearPlan(
                "Profesional",
                6,
                new BigDecimal("49.99"),
                "Pensado para tiendas que buscan crecimiento y automatización.",
                "✔ Integración completa por API\n✔ Reportes detallados\n✔ Soporte prioritario");

        crearPlan(
                "Empresarial",
                12,
                new BigDecimal("89.99"),
                "Diseñado para empresas con grandes volúmenes de datos y necesidades avanzadas.",
                "✔ Métricas avanzadas en tiempo real\n✔ Integración total por API\n✔ Asistencia personalizada 24/7");
    */}

    private void crearPlan(String nombre, int duracionMeses, BigDecimal precioMensual,
            String descripcion, String beneficios) {

        if (planRepository.findByNombre(nombre).isEmpty()) {
            Plan plan = new Plan();
            plan.setNombre(nombre);
            plan.setDuracionMeses(duracionMeses);
            plan.setPrecioMensual(precioMensual);
            plan.setDescripcion(descripcion);
            plan.setBeneficios(beneficios);

            planRepository.save(plan);
            log.info("Plan creado: {}", nombre);
        } else {
            log.info("El plan {} ya existe", nombre);
        }
    }
}
