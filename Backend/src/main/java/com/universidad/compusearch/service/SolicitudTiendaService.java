package com.universidad.compusearch.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.EstadoSolicitud;
import com.universidad.compusearch.entity.SolicitudTienda;
import com.universidad.compusearch.entity.Usuario;
import com.universidad.compusearch.exception.TooManyAttemptsException;
import com.universidad.compusearch.repository.SolicitudTiendaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SolicitudTiendaService {
    private final SolicitudTiendaRepository solicitudTiendaRepository;
    private final UsuarioService usuarioService;
    private final SolicitudTiendaAttempService solicitudTiendaAttempService;

    public Page<SolicitudTienda> obtenerSolicitudesPorUsuario(Long idUsuario, int page, int size) {
        log.info("Buscando solicitudes de usuario con ID: {} | Página: {} | Tamaño: {}", idUsuario, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaSolicitud").descending());
        return solicitudTiendaRepository.findByUsuario_IdUsuario(idUsuario, pageable);
    }

    public void crearSolicitud(Long idUsuario, String datosForm) {
        log.info("Intentando crear solicitud para usuario con ID: {}", idUsuario);

        String key = "solicitud_key_" + idUsuario;

        if (solicitudTiendaAttempService.isBlocked(key)) {
            throw TooManyAttemptsException.solicitud();
        }

        Usuario usuario = usuarioService.buscarPorId(idUsuario);

        SolicitudTienda solicitud = new SolicitudTienda();
        solicitud.setFechaSolicitud(LocalDateTime.now());
        solicitud.setEstado(EstadoSolicitud.OBSERVACION);
        solicitud.setDatosFormulario(datosForm);
        solicitud.setUsuario(usuario);

        solicitudTiendaAttempService.fail(key);;

        log.info("Solicitud de tienda creada para el usuario: {}", usuario.getUsername());
        solicitudTiendaRepository.save(solicitud);
    }
}
