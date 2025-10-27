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

/**
 * Servicio encargado de gestionar las operaciones relacionadas con las solicitudes
 * de creación o verificación de tiendas por parte de los usuarios.
 * 
 * Implementa lógica para:
 * <ul>
 *     <li>Listar las solicitudes enviadas por un usuario.</li>
 *     <li>Crear nuevas solicitudes de tienda con validación de intentos.</li>
 * </ul>
 * 
 *
 * 
 * Este servicio utiliza {@link SolicitudTiendaRepository} para acceder a los datos,
 * {@link UsuarioService} para validar la existencia del usuario solicitante y
 * {@link SolicitudTiendaAttempService} para limitar los intentos de envío de solicitudes.
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

    /**
     * Obtiene una página con las solicitudes de tienda realizadas por un usuario específico.
     *
     * @param idUsuario el identificador del usuario.
     * @param page      número de página (base 0).
     * @param size      cantidad de elementos por página.
     * @return una {@link Page} de objetos {@link SolicitudTienda} ordenadas por fecha de solicitud descendente.
     */
    public Page<SolicitudTienda> obtenerSolicitudesPorUsuario(Long idUsuario, int page, int size) {
        log.info("Buscando solicitudes de usuario con ID: {} | Página: {} | Tamaño: {}", idUsuario, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaSolicitud").descending());
        return solicitudTiendaRepository.findByUsuario_IdUsuario(idUsuario, pageable);
    }

    /**
     * Crea una nueva solicitud de tienda asociada a un usuario.
     * 
     * Se valida que el usuario no haya excedido el límite de intentos recientes de envío
     * utilizando {@link SolicitudTiendaAttempService}.
     * Si el usuario está bloqueado, se lanza una excepción de tipo {@link TooManyAttemptsException}.
     * 
     *
     * @param idUsuario el identificador del usuario que realiza la solicitud.
     * @param datosForm datos en formato JSON o texto plano del formulario de solicitud.
     * @throws TooManyAttemptsException si el usuario ha excedido el límite de intentos permitidos.
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
}
