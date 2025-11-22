package com.universidad.compusearch.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.dto.SolicitudTiendaResponse;
import com.universidad.compusearch.entity.Empleado;
import com.universidad.compusearch.entity.EstadoSolicitud;
import com.universidad.compusearch.entity.SolicitudTienda;
import com.universidad.compusearch.entity.Usuario;
import com.universidad.compusearch.exception.SolicitudTiendaException;
import com.universidad.compusearch.exception.TooManyAttemptsException;
import com.universidad.compusearch.repository.SolicitudTiendaRepository;
import com.universidad.compusearch.util.Mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SolicitudTiendaService {

    private final SolicitudTiendaRepository solicitudTiendaRepository;
    private final UsuarioService usuarioService;
    private final SolicitudTiendaAttempService solicitudTiendaAttempService;
    private final EmpleadoService empleadoService;
    private final ConversionService conversionService; 

    // Obtener todas las solicitudes por usuario
    public Page<SolicitudTienda> obtenerSolicitudesPorUsuario(Long idUsuario, int page, int size) {
        log.info("Buscando solicitudes de usuario con ID: {} | Página: {} | Tamaño: {}", idUsuario, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaSolicitud").descending());
        return solicitudTiendaRepository.findByUsuario_IdUsuario(idUsuario, pageable);
    }

    // Crear solicitud
    public void crearSolicitud(Long idUsuario, Map<String, Object> datosForm) {
        log.info("Intentando crear solicitud para usuario con ID: {}", idUsuario);
        String key = "solicitud_" + idUsuario;

        if (solicitudTiendaAttempService.isBlocked(key)) {
            throw TooManyAttemptsException.solicitud();
        }

        Usuario usuario = usuarioService.buscarPorId(idUsuario);

        SolicitudTienda solicitud = new SolicitudTienda();
        solicitud.setFechaSolicitud(LocalDateTime.now());
        solicitud.setEstado(EstadoSolicitud.OBSERVACION);
        solicitud.setDatosFormulario(datosForm);
        solicitud.setUsuario(usuario);

        solicitudTiendaAttempService.fail(key);

        log.info("Solicitud de tienda creada para el usuario: {}", usuario.getUsername());
        solicitudTiendaRepository.save(solicitud);
    }

    // Actualizar estado de solicitud
    public SolicitudTienda actualizarEstadoSolicitud(Long idSolicitud, String nuevoEstadoStr, Long idEmpleado) {
        log.info("Actualizando estado de la solicitud con ID: {} a '{}' por el empleado con ID: {}",
                idSolicitud, nuevoEstadoStr, idEmpleado);

        SolicitudTienda solicitud = solicitudTiendaRepository.findById(idSolicitud)
                .orElseThrow(SolicitudTiendaException::notFound);

        Empleado empleado = empleadoService.obtenerPorId(idEmpleado);
        EstadoSolicitud nuevoEstado = EstadoSolicitud.valueOf(nuevoEstadoStr.toUpperCase());
        solicitud.setEstado(nuevoEstado);
        solicitud.setEmpleado(empleado);

        if (nuevoEstado == EstadoSolicitud.APROBADA) {
            conversionService.convertirUsuarioEnTienda(solicitud);
        }

        SolicitudTienda actualizada = solicitudTiendaRepository.save(solicitud);
        log.info("Estado de la solicitud con ID {} actualizado correctamente a '{}' por el empleado {}",
                idSolicitud, nuevoEstadoStr, empleado.getNombre());
        return actualizada;
    }

    // Obtener todas las solicitudes
    public Page<SolicitudTiendaResponse> obtenerTodasLasSolicitudes(int page, int size) {
        log.info("Consultando todas las solicitudes | Página: {} | Tamaño: {}", page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaSolicitud").descending());
        Page<SolicitudTienda> solicitudesPage = solicitudTiendaRepository.findAll(pageable);

        Page<SolicitudTiendaResponse> respuestaPage = solicitudesPage.map(Mapper::mapToSolicitudTienda);
        
        log.info("Se encontraron {} solicitudes en la página {}", respuestaPage.getNumberOfElements(), page);
        return respuestaPage;
    }
}