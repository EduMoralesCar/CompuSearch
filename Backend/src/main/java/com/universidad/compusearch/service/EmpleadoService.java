package com.universidad.compusearch.service;

import com.universidad.compusearch.exception.EmpleadoException;
import com.universidad.compusearch.exception.UserException;
import com.universidad.compusearch.dto.AdminDashboardResponse;
import com.universidad.compusearch.dto.EmpleadoRequest;
import com.universidad.compusearch.dto.EmpleadoResponse;
import com.universidad.compusearch.entity.Empleado;
import com.universidad.compusearch.entity.EstadoSolicitud;
import com.universidad.compusearch.entity.EstadoSuscripcion;
import com.universidad.compusearch.entity.Pago;
import com.universidad.compusearch.entity.ProductoTienda;
import com.universidad.compusearch.entity.SolicitudTienda;
import com.universidad.compusearch.entity.Suscripcion;
import com.universidad.compusearch.entity.Tienda;
import com.universidad.compusearch.entity.TipoUsuario;
import com.universidad.compusearch.repository.EmpleadoRepository;
import com.universidad.compusearch.repository.IncidenteRepository;
import com.universidad.compusearch.repository.PagoRepository;
import com.universidad.compusearch.repository.ProductoTiendaRepository;
import com.universidad.compusearch.repository.SolicitudTiendaRepository;
import com.universidad.compusearch.repository.SuscripcionRepository;
import com.universidad.compusearch.repository.TiendaRepository;
import com.universidad.compusearch.repository.UsuarioRepository;
import com.universidad.compusearch.util.Mapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioService usuarioService;
    private final TiendaRepository tiendaRepository;
    private final PagoRepository pagoRepository;
    private final SuscripcionRepository suscripcionRepository;
    private final SolicitudTiendaRepository solicitudTiendaRepository;
    private final IncidenteRepository incidenteRepository;
    private final ProductoTiendaRepository productoTiendaRepository;

    // Obtener empleado por id
    public Empleado obtenerPorId(Long idEmpleado) {
        log.info("Buscando empleado con ID: {}", idEmpleado);

        Empleado empleado = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> {
                    log.warn("Empleado con ID {} no encontrado", idEmpleado);
                    return EmpleadoException.notFound();
                });

        log.info("Empleado encontrado: {}", empleado.getNombre());
        return empleado;
    }

    // Optener todos los empleados
    public Page<EmpleadoResponse> obtenerTodosEmpleados(Pageable pageable, String username) {
        log.info("Obteniendo página {} de empleados. Filtro username: {}", pageable.getPageNumber(), username);

        Page<Empleado> empleadosPage;

        if (username != null && !username.trim().isEmpty()) {
            empleadosPage = empleadoRepository.findByUsernameContainingIgnoreCase(username, pageable);
        } else {
            empleadosPage = empleadoRepository.findAll(pageable);
        }
        return empleadosPage.map(Mapper::mapToEmpleado);
    }

    // Obtener empleado por id
    @Transactional
    public EmpleadoResponse obtenerEmpleadoPorId(Long id) {
        log.info("Buscando empleado con ID: {}", id);
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> EmpleadoException.notFound());

        return Mapper.mapToEmpleado(empleado);
    }

    // Crear empleado
    @Transactional
    public EmpleadoResponse crearEmpleado(EmpleadoRequest request) {
        log.info("Iniciando creación de nuevo empleado: {}", request.getUsername());

        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw UserException.usernameAlredyRegistered(request.getUsername());
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw UserException.emailAlredyRegistered(request.getEmail());
        }

        Empleado empleado = new Empleado();
        empleado.setUsername(request.getUsername());
        empleado.setEmail(request.getEmail());
        empleado.setContrasena(passwordEncoder.encode(request.getPassword()));
        empleado.setActivo(true);
        empleado.setTipoUsuario(TipoUsuario.EMPLEADO);
        empleado.setFechaRegistro(LocalDateTime.now());

        empleado.setNombre(request.getNombre());
        empleado.setApellido(request.getApellido());
        empleado.setRol(request.getRol());
        empleado.setFechaAsignacion(LocalDateTime.now());

        Empleado nuevoEmpleado = empleadoRepository.save(empleado);
        log.info("Empleado creado con ID: {}", nuevoEmpleado.getIdUsuario());
        return Mapper.mapToEmpleado(nuevoEmpleado);
    }

    // Modificar al empleado
    @Transactional
    public EmpleadoResponse modificarEmpleado(Long id, EmpleadoRequest request) {
        log.info("Actualizando empleado con ID: {}", id);
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> EmpleadoException.notFound());

        empleado.setEmail(request.getEmail());
        usuarioService.actualizarPasswordByAdmin(id, request.getPassword());
        empleado.setNombre(request.getNombre());
        empleado.setApellido(request.getApellido());
        empleado.setRol(request.getRol());

        Empleado empleadoActualizado = empleadoRepository.save(empleado);
        log.info("Empleado ID {} actualizado.", id);
        return Mapper.mapToEmpleado(empleadoActualizado);
    }

    // Actualizar estado de empleado
    @Transactional
    public boolean actualizarEstadoActivo(Long id, boolean activo) {
        log.info("Cambiando estado activo del empleado ID {} a: {}", id, activo);
        return empleadoRepository.findById(id).map(empleado -> {
            empleado.setActivo(activo);
            empleadoRepository.save(empleado);
            return true;
        }).orElseGet(() -> {
            log.warn("Empleado ID {} no encontrado para actualizar estado activo.", id);
            return false;
        });
    }

    public AdminDashboardResponse obtenerDashboard() {

        AdminDashboardResponse dashboard = new AdminDashboardResponse();

        List<Tienda> todasTiendas = tiendaRepository.findAll();
        dashboard.setTotalTiendas(todasTiendas.size());
        dashboard.setTiendasVerificadas((int) todasTiendas.stream().filter(Tienda::isVerificado).count());
        dashboard.setTiendasNoVerificadas(dashboard.getTotalTiendas() - dashboard.getTiendasVerificadas());

        dashboard.setUltimasTiendas(
                todasTiendas.stream()
                        .sorted(Comparator.comparing(Tienda::getFechaAfiliacion).reversed())
                        .limit(5)
                        .map(Mapper::mapToUltimaTiendaResponse)
                        .collect(Collectors.toList()));

        dashboard.setTotalEmpleados((int) empleadoRepository.count());
        dashboard.setTotalUsuarios((int) usuarioRepository.count());

        // Últimos 5 empleados
        dashboard.setUltimosEmpleados(
                empleadoRepository.findTop5ByOrderByFechaRegistroDesc()
                        .stream()
                        .map(Mapper::mapToUltimoEmpleadoResponse)
                        .collect(Collectors.toList()));

        // --- PRODUCTOS ---
        List<ProductoTienda> todosProductos = productoTiendaRepository.findAll();
        dashboard.setTotalProductos(todosProductos.size());
        dashboard.setProductosActivos((int) todosProductos.stream().filter(ProductoTienda::getHabilitado).count());
        dashboard.setProductosInactivos(dashboard.getTotalProductos() - dashboard.getProductosActivos());

        // --- SOLICITUDES ---
        List<SolicitudTienda> todasSolicitudes = solicitudTiendaRepository.findAll();
        dashboard.setTotalSolicitudes(todasSolicitudes.size());
        dashboard.setSolicitudesPendientes(
                (int) todasSolicitudes.stream().filter(s -> s.getEstado() == EstadoSolicitud.OBSERVACION).count());
        dashboard.setSolicitudesAceptadas(
                (int) todasSolicitudes.stream().filter(s -> s.getEstado() == EstadoSolicitud.APROBADA).count());
        dashboard.setSolicitudesRechazadas(
                (int) todasSolicitudes.stream().filter(s -> s.getEstado() == EstadoSolicitud.RECHAZADA).count());

        // --- INCIDENTES ---
        dashboard.setTotalIncidentes((int) incidenteRepository.count());

        // --- SUSCRIPCIONES ---
        List<Suscripcion> todasSuscripciones = suscripcionRepository.findAll();
        dashboard.setTotalSuscripciones(todasSuscripciones.size());
        dashboard.setSuscripcionesActivas(
                (int) todasSuscripciones.stream().filter(s -> s.getEstado() == EstadoSuscripcion.ACTIVA).count());
        dashboard.setSuscripcionesExpiradas(
                (int) todasSuscripciones.stream().filter(s -> s.getEstado() == EstadoSuscripcion.CANCELADA).count());

        // --- PAGOS ---
        List<Pago> todosPagos = pagoRepository.findAll();
        dashboard.setTotalPagos(todosPagos.size());

        BigDecimal ingresosTotales = todosPagos.stream()
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        dashboard.setIngresosTotales(ingresosTotales.doubleValue());

        dashboard.setUltimosPagos(
                todosPagos.stream()
                        .sorted(Comparator.comparing(Pago::getFechaPago).reversed())
                        .limit(5)
                        .map(Mapper::mapToUltimoPagoResponse)
                        .collect(Collectors.toList()));

        return dashboard;
    }
}
