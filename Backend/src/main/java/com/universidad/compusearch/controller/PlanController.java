package com.universidad.compusearch.controller;

import com.universidad.compusearch.dto.PlanRequest;
import com.universidad.compusearch.dto.PlanResponse;
import com.universidad.compusearch.service.PlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/plan")
@RequiredArgsConstructor
@Slf4j
public class PlanController {

    private final PlanService planService;

    @PostMapping
    public ResponseEntity<PlanResponse> crearPlan(@Valid @RequestBody PlanRequest request) {
        log.info("POST /planes - Solicitud de creación de plan: {}", request.getNombre());
        PlanResponse nuevoPlan = planService.crearPlan(request);
        log.info("Plan ID {} creado.", nuevoPlan.getIdPlan());
        return new ResponseEntity<>(nuevoPlan, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<PlanResponse>> obtenerTodosLosPlanes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String nombre,
            @RequestParam(defaultValue = "false") boolean incluirInactivos) {

        log.info("GET /planes - Solicitud de planes paginados. Page={}, Size={}, Nombre={}, Inactivos={}", page, size,
                nombre, incluirInactivos);

        Pageable pageable = PageRequest.of(page, size);
        Page<PlanResponse> planes = planService.obtenerTodosLosPlanes(pageable, nombre, incluirInactivos);

        log.info("Retornando página {} de planes (Total: {}).", page, planes.getTotalElements());
        return ResponseEntity.ok(planes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PlanResponse> obtenerPlanPorId(@PathVariable Long id) {
        log.info("GET /planes/{} - Solicitud para obtener plan por ID.", id);
        PlanResponse plan = planService.obtenerPlanPorId(id);
        log.info("Plan ID {} encontrado.", id);
        return ResponseEntity.ok(plan);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanResponse> actualizarPlan(@PathVariable Long id,
            @Valid @RequestBody PlanRequest request) {
        log.info("PUT /planes/{} - Solicitud para modificar plan ID {}.", id, id);
        PlanResponse planActualizado = planService.actualizarPlan(id, request);
        log.info("Plan ID {} modificado exitosamente.", id);
        return ResponseEntity.ok(planActualizado);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> actualizarEstadoActivo(@PathVariable Long id, @RequestParam boolean activo) {
        log.warn("PATCH /planes/{}/estado - Solicitud de cambio de estado a activo={}.", id, activo);
        planService.actualizarEstadoActivo(id, activo);
        log.info("Estado del plan ID {} cambiado a activo={}.", id, activo);
        return ResponseEntity.noContent().build();
    }
}