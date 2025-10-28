package com.universidad.compusearch.service;

import java.time.LocalDateTime;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con las
 * solicitudes
 * de creación o verificación de tiendas por parte de los usuarios.
 * 
 * Implementa lógica para:
 * <ul>
 * <li>Listar las solicitudes enviadas por un usuario.</li>
 * <li>Crear nuevas solicitudes de tienda con validación de intentos.</li>
 * </ul>
 * 
 *
 * 
 * Este servicio utiliza {@link SolicitudTiendaRepository} para acceder a los
 * datos,
 * {@link UsuarioService} para validar la existencia del usuario solicitante y
 * {@link SolicitudTiendaAttempService} para limitar los intentos de envío de
 * solicitudes.
 * 
 *
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SolicitudTiendaService {

    private final SolicitudTiendaRepository solicitudTiendaRepository;
    private final UsuarioService usuarioService;
    private final SolicitudTiendaAttempService solicitudTiendaAttempService;
    private final EmpleadoService empleadoService;

    /**
     * Obtiene una página con las solicitudes de tienda realizadas por un usuario
     * específico.
     *
     * @param idUsuario el identificador del usuario.
     * @param page      número de página (base 0).
     * @param size      cantidad de elementos por página.
     * @return una {@link Page} de objetos {@link SolicitudTienda} ordenadas por
     *         fecha de solicitud descendente.
     */
    public Page<SolicitudTienda> obtenerSolicitudesPorUsuario(Long idUsuario, int page, int size) {
        log.info("Buscando solicitudes de usuario con ID: {} | Página: {} | Tamaño: {}", idUsuario, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaSolicitud").descending());
        return solicitudTiendaRepository.findByUsuario_IdUsuario(idUsuario, pageable);
    }

    /**
     * Crea una nueva solicitud de tienda asociada a un usuario.
     * 
     * Se valida que el usuario no haya excedido el límite de intentos recientes de
     * envío
     * utilizando {@link SolicitudTiendaAttempService}.
     * Si el usuario está bloqueado, se lanza una excepción de tipo
     * {@link TooManyAttemptsException}.
     * 
     *
     * @param idUsuario el identificador del usuario que realiza la solicitud.
     * @param datosForm datos en formato JSON o texto plano del formulario de
     *                  solicitud.
     * @throws TooManyAttemptsException si el usuario ha excedido el límite de
     *                                  intentos permitidos.
     */
    public void crearSolicitud(Long idUsuario, String datosForm) {
        log.info("Intentando crear solicitud para usuario con ID: {}", idUsuario);

        String key = "solicitud_key_" + idUsuario;

        // Validar intentos de solicitud recientes
        if (solicitudTiendaAttempService.isBlocked(key)) {
            throw TooManyAttemptsException.solicitud();
        }

        Usuario usuario = usuarioService.buscarPorId(idUsuario);

        // Crear y configurar la nueva solicitud
        SolicitudTienda solicitud = new SolicitudTienda();
        solicitud.setFechaSolicitud(LocalDateTime.now());
        solicitud.setEstado(EstadoSolicitud.OBSERVACION);
        solicitud.setDatosFormulario(datosForm);
        solicitud.setUsuario(usuario);

        // Registrar intento de solicitud
        solicitudTiendaAttempService.fail(key);

        log.info("Solicitud de tienda creada para el usuario: {}", usuario.getUsername());
        solicitudTiendaRepository.save(solicitud);
    }

    /**
     * Actualiza el estado de una solicitud de tienda existente.
     *
     * <p>
     * El nuevo estado se recibe como una cadena (String) y se convierte
     * internamente
     * a un valor del enum {@link EstadoSolicitud}.
     * </p>
     *
     * @param idSolicitud    el identificador de la solicitud que se desea
     *                       actualizar.
     * @param nuevoEstadoStr el nuevo estado en formato texto (por ejemplo:
     *                       "APROBADA", "RECHAZADA").
     * @return la solicitud actualizada.
     * @throws SolicitudTiendaException si la solicitud no existe o el estado
     *                                  proporcionado es inválido.
     */
    /**
     * Actualiza el estado de una solicitud de tienda y registra qué empleado
     * realizó el cambio.
     *
     * @param idSolicitud    ID de la solicitud a actualizar.
     * @param nuevoEstadoStr Nuevo estado de la solicitud en formato texto (por
     *                       ejemplo, "APROBADA", "RECHAZADA").
     * @param idEmpleado     ID del empleado que realiza la actualización.
     * @return La entidad {@link SolicitudTienda} actualizada.
     * @throws SolicitudTiendaException si la solicitud no existe o el estado es
     *                                  inválido.
     */
    public SolicitudTienda actualizarEstadoSolicitud(Long idSolicitud, String nuevoEstadoStr, Long idEmpleado) {
        log.info("Actualizando estado de la solicitud con ID: {} a '{}' por el empleado con ID: {}",
                idSolicitud, nuevoEstadoStr, idEmpleado);

        // Buscar la solicitud
        SolicitudTienda solicitud = solicitudTiendaRepository.findById(idSolicitud)
                .orElseThrow(SolicitudTiendaException::notFound);

        // Buscar el empleado (asegúrate de tener un repositorio para ello)
        Empleado empleado = empleadoService.obtenerPorId(idEmpleado);

        EstadoSolicitud nuevoEstado = EstadoSolicitud.valueOf(nuevoEstadoStr.toUpperCase());
        solicitud.setEstado(nuevoEstado);

        // Registrar qué empleado hizo el cambio
        solicitud.setEmpleado(empleado);

        // Guardar cambios
        SolicitudTienda actualizada = solicitudTiendaRepository.save(solicitud);

        log.info("Estado de la solicitud con ID {} actualizado correctamente a '{}' por el empleado {}",
                idSolicitud, nuevoEstadoStr, empleado.getNombre());

        return actualizada;
    }

    /**
     * Obtiene todas las solicitudes de tiendas en el sistema, paginadas y ordenadas
     * por fecha descendente, y las transforma a objetos
     * {@link SolicitudTiendaResponse}.
     *
     * @param page número de página (base 0).
     * @param size cantidad de elementos por página.
     * @return una {@link Page} de {@link SolicitudTiendaResponse}.
     */
    public Page<SolicitudTiendaResponse> obtenerTodasLasSolicitudes(int page, int size) {
        log.info("Consultando todas las solicitudes | Página: {} | Tamaño: {}", page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaSolicitud").descending());
        Page<SolicitudTienda> solicitudesPage = solicitudTiendaRepository.findAll(pageable);

        // Convertir cada entidad en un DTO (SolicitudTiendaResponse)
        Page<SolicitudTiendaResponse> respuestaPage = solicitudesPage.map(solicitud -> new SolicitudTiendaResponse(
                solicitud.getIdSolicitud(),
                solicitud.getUsuario().getIdUsuario(),
                solicitud.getUsuario().getUsername(),
                solicitud.getDatosFormulario(),
                solicitud.getFechaSolicitud(),
                solicitud.getEstado().name(),
                solicitud.getEmpleado() != null ? solicitud.getEmpleado().getIdUsuario() : null,
                solicitud.getEmpleado() != null ? solicitud.getEmpleado().getNombre() : null));

        log.info("Se encontraron {} solicitudes en la página {}", respuestaPage.getNumberOfElements(), page);
        return respuestaPage;
    }
}
