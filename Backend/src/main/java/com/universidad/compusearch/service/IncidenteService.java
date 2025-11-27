package com.universidad.compusearch.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.dto.IncidenteResponse;
import com.universidad.compusearch.entity.Incidente;
import com.universidad.compusearch.entity.Usuario;
import com.universidad.compusearch.exception.IncidenteException;
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

    // Obtener incidentes por usuario
    public Page<Incidente> obtenerIncidentesPorUsuario(Long idUsuario, int page, int size) {
        log.info("Buscando incidentes de usuario con ID: {} | Página: {} | Tamaño: {}", idUsuario, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaCreacion").descending());
        return incidenteRepository.findAllByUsuario_IdUsuario(idUsuario, pageable);
    }

    // Crear un incidente
    public Incidente crearIncidente(Long idUsuario, String titulo, String descripcion) {
        log.info("Intentando crear incidente para usuario con ID: {}", idUsuario);

        String key = "incidentekey_" + idUsuario;

        if (incidenciaAttempsService.isBlocked(key)) {
            throw TooManyAttemptsException.incident();
        }

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(UserException::notFound);

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

    // Obtener todos los incidentes
    public Page<IncidenteResponse> obtenerTodosLosIncidentes(int page, int size) {
        log.info("Consultando todas las solicitudes | página: {} | tamaño: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<Incidente> incidentes = incidenteRepository.findAll(pageable);

        log.info("Se encontraron {} incidentes en la página {}", incidentes.getNumberOfElements(), page);

        Page<IncidenteResponse> responsePage = incidentes.map(incidente -> new IncidenteResponse(
                incidente.getIdIncidente(),
                incidente.getUsuario() != null ? incidente.getUsuario().getUsername() : "Desconocido",
                incidente.getTitulo(),
                incidente.getDescripcion(),
                incidente.getFechaCreacion(),
                incidente.isRevisado()));

        return responsePage;
    }

    // Eliminar incidente por id
    public void eliminarIncidentePorId(Long id) {
        log.info("Intentando eliminar incidente con id: {}", id);

        if (!incidenteRepository.existsById(id)) {
            log.warn("No se encontró incidente con id: {}", id);
            throw IncidenteException.notFound();
        }

        incidenteRepository.deleteById(id);
        log.info("Incidente con id {} eliminado exitosamente", id);
    }

    // Actualizar estado de incidente
    public void actualizarEstadoRevisado(Long idIncidente, boolean revisado) {
        log.info("Actualizando campo 'revisado' del incidente con ID: {} a {}", idIncidente, revisado);

        var incidente = incidenteRepository.findById(idIncidente)
                .orElseThrow(() -> IncidenteException.notFound());

        incidente.setRevisado(revisado);
        incidenteRepository.save(incidente);

        log.info("Incidente con ID {} actualizado (revisado = {})", idIncidente, revisado);
    }

}
