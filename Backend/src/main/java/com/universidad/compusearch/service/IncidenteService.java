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

/**
 * Servicio encargado de la gestión de incidentes.
 * 
 * Permite la creación de incidentes y la obtención de los incidentes de un
 * usuario.
 * Controla los intentos de creación mediante {@link IncidenciaAttempsService}.
 * 
 * 
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class IncidenteService {

    private final IncidenteRepository incidenteRepository;
    private final UsuarioRepository usuarioRepository;
    private final IncidenciaAttempsService incidenciaAttempsService;

    /**
     * Obtiene una página de incidentes asociados a un usuario.
     *
     * @param idUsuario el ID del usuario.
     * @param page      número de página (0-indexed).
     * @param size      cantidad de incidentes por página.
     * @return una {@link Page} de {@link Incidente} ordenada por fecha de creación
     *         descendente.
     */
    public Page<Incidente> obtenerIncidentesPorUsuario(Long idUsuario, int page, int size) {
        log.info("Buscando incidentes de usuario con ID: {} | Página: {} | Tamaño: {}", idUsuario, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaCreacion").descending());
        return incidenteRepository.findAllByUsuario_IdUsuario(idUsuario, pageable);
    }

    /**
     * Crea un nuevo incidente para un usuario determinado.
     * 
     * Controla la cantidad de intentos para evitar abuso mediante
     * {@link IncidenciaAttempsService}.
     * 
     *
     * @param idUsuario   el ID del usuario que reporta el incidente.
     * @param titulo      el título del incidente.
     * @param descripcion la descripción detallada del incidente.
     * @return el {@link Incidente} recién creado.
     * @throws TooManyAttemptsException si se exceden los intentos permitidos.
     * @throws UserException            si el usuario no existe.
     */
    public Incidente crearIncidente(Long idUsuario, String titulo, String descripcion) {
        log.info("Intentando crear incidente para usuario con ID: {}", idUsuario);

        String key = "incidente_key_" + idUsuario;

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

    /**
     * Obtiene todos los incidentes registrados en el sistema de manera paginada.
     *
     * <p>
     * Convierte los resultados de la entidad {@link Incidente} al DTO
     * {@link IncidenteResponse}
     * antes de devolverlos, para mantener una capa de presentación limpia y segura.
     * </p>
     *
     * @param page número de página solicitada (por defecto 0)
     * @param size cantidad de registros por página (por defecto 10)
     * @return una página de objetos {@link IncidenteResponse} con información
     *         resumida de cada incidente
     */
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

    /**
     * Elimina un incidente por su ID.
     *
     * @param id ID del incidente a eliminar
     * @throws IncidenteException si el incidente no existe
     */
    public void eliminarIncidentePorId(Long id) {
        log.info("Intentando eliminar incidente con id: {}", id);

        if (!incidenteRepository.existsById(id)) {
            log.warn("No se encontró incidente con id: {}", id);
            throw IncidenteException.notFound();
        }

        incidenteRepository.deleteById(id);
        log.info("Incidente con id {} eliminado exitosamente", id);
    }

    /**
     * Actualiza el estado "revisado" de un incidente.
     *
     * @param idIncidente ID del incidente.
     * @param revisado    Nuevo valor del estado revisado.
     * @throws IncidenteException si el incidente no existe.
     */
    public void actualizarEstadoRevisado(Long idIncidente, boolean revisado) {
        log.info("Actualizando campo 'revisado' del incidente con ID: {} a {}", idIncidente, revisado);

        var incidente = incidenteRepository.findById(idIncidente)
                .orElseThrow(() -> IncidenteException.notFound());

        incidente.setRevisado(revisado);
        incidenteRepository.save(incidente);

        log.info("Incidente con ID {} actualizado (revisado = {})", idIncidente, revisado);
    }

}
