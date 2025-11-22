package com.universidad.compusearch.service;

import com.universidad.compusearch.exception.EmpleadoException;
import com.universidad.compusearch.exception.UserException;
import com.universidad.compusearch.dto.EmpleadoRequest;
import com.universidad.compusearch.dto.EmpleadoResponse;
import com.universidad.compusearch.entity.Empleado;
import com.universidad.compusearch.entity.TipoUsuario;
import com.universidad.compusearch.repository.EmpleadoRepository;
import com.universidad.compusearch.repository.UsuarioRepository;
import com.universidad.compusearch.util.Mapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmpleadoService {

    /** Repositorio para acceder a los datos de los empleados. */
    private final EmpleadoRepository empleadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioService usuarioService;

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
}
