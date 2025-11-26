package com.universidad.compusearch.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.dto.AdminDashboardResponse;
import com.universidad.compusearch.dto.EmpleadoRequest;
import com.universidad.compusearch.dto.EmpleadoResponse;
import com.universidad.compusearch.service.EmpleadoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/empleado")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    @GetMapping
    public ResponseEntity<Page<EmpleadoResponse>> obtenerTodosLosEmpleados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username) {

        log.info("GET /empleados - Solicitud para obtener empleados paginados. Page={}, Size={}, Username={}", page,
                size, username);

        Pageable pageable = PageRequest.of(page, size);

        Page<EmpleadoResponse> empleados = empleadoService.obtenerTodosEmpleados(pageable, username);

        log.info("Retornando página {} de empleados (Total: {}).", page, empleados.getTotalElements());
        return ResponseEntity.ok(empleados);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoResponse> obtenerEmpleadoPorId(@PathVariable Long id) {
        log.info("GET /empleados/{} - Solicitud para obtener empleado por ID.", id);
        EmpleadoResponse empleado = empleadoService.obtenerEmpleadoPorId(id);
        log.info("Empleado ID {} encontrado y retornando DTO.", id);
        return ResponseEntity.ok(empleado);
    }

    @PostMapping
    public ResponseEntity<EmpleadoResponse> crearEmpleado(@Valid @RequestBody EmpleadoRequest request) {
        log.info("POST /empleados - Solicitud para crear un nuevo empleado: {}", request.getUsername());
        EmpleadoResponse nuevoEmpleado = empleadoService.crearEmpleado(request);
        log.info("Empleado ID {} creado exitosamente.", nuevoEmpleado.getIdUsuario());
        return new ResponseEntity<>(nuevoEmpleado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoResponse> modificarEmpleado(@PathVariable Long id,
            @Valid @RequestBody EmpleadoRequest request) {
        log.info("PUT /empleados/{} - Solicitud para modificar empleado ID {}.", id, id);
        EmpleadoResponse empleadoActualizado = empleadoService.modificarEmpleado(id, request);
        log.info("Empleado ID {} modificado exitosamente.", id);
        return ResponseEntity.ok(empleadoActualizado);
    }

    @PatchMapping("/{id}/activo")
    public ResponseEntity<Void> actualizarEstadoActivo(@PathVariable Long id, @RequestParam boolean activo) {
        log.info("PATCH /empleados/{}/activo - Solicitud para cambiar estado a activo={}.", id, activo);
        boolean isUpdated = empleadoService.actualizarEstadoActivo(id, activo);

        if (isUpdated) {
            log.info("Estado del empleado ID {} cambiado a activo={}.", id, activo);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("Fallo al cambiar el estado del empleado ID {}. No encontrado o error en servicio.", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponse> obtenerDashboard() {
        log.info("Solicitando dashboard del administrador del sistema");

        try {
            AdminDashboardResponse dashboard = empleadoService.obtenerDashboard();
            log.info("Dashboard generado correctamente con {} tiendas, {} empleados y {} pagos",
                    dashboard.getTotalTiendas(),
                    dashboard.getTotalEmpleados(),
                    dashboard.getTotalPagos());
            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            log.error("Error al generar el dashboard del administrador", e);
            return ResponseEntity.status(500).build();
        }
    }
}