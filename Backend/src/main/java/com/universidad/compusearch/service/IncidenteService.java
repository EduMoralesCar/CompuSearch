package com.universidad.compusearch.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.Incidente;
import com.universidad.compusearch.entity.Usuario;
import com.universidad.compusearch.exception.TooManyAttemptsException;
import com.universidad.compusearch.exception.UserException;
import com.universidad.compusearch.repository.IncidenteRepository;
import com.universidad.compusearch.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidenteService {
    private final IncidenteRepository incidenteRepository;
    private final UsuarioRepository usuarioRepository;
    private final IncidenciaAttempsService incidenciaAttempsService;

    public Page<Incidente> obtenerIncidentesPorUsuario(Long idUsuario, int page, int size) {
        log.info("Buscando incidentes de usuario con ID: {} | Página: {} | Tamaño: {}", idUsuario, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaCreacion").descending());
        return incidenteRepository.findAllByUsuario_IdUsuario(idUsuario, pageable);
    }

    public Incidente crearIncidente(Long idUsuario, String titulo, String descripcion) {
        log.info("Intentando crear incidente para usuario con ID: {}", idUsuario);

        String key = "incidente_key_" + idUsuario;

        if (incidenciaAttempsService.isBlocked(key)) {
            throw TooManyAttemptsException.incident();
        }

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> UserException.notFound());

        // Crear la incidencia
        Incidente incidente = new Incidente();
        incidente.setUsuario(usuario);
        incidente.setTitulo(titulo);
        incidente.setDescripcion(descripcion);
        incidente.setFechaCreacion(LocalDateTime.now());

        Incidente saved = incidenteRepository.save(incidente);

        incidenciaAttempsService.fail(key);

        log.info("Incidente creado con ID: {}", saved.getIdIncidente());
        return saved;
    }
}
