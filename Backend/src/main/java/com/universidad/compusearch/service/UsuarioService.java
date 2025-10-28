package com.universidad.compusearch.service;

import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.universidad.compusearch.dto.UsuarioInfoResponse;
import com.universidad.compusearch.entity.Usuario;
import com.universidad.compusearch.exception.AlreadyRegisteredException;
import com.universidad.compusearch.exception.PasswordException;
import com.universidad.compusearch.exception.TooManyAttemptsException;
import com.universidad.compusearch.exception.UserException;
import com.universidad.compusearch.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio responsable de la gestión de usuarios dentro del sistema.
 * <p>
 * Implementa {@link UserDetailsService} para permitir la autenticación
 * basada en credenciales de Spring Security.
 * </p>
 *
 * <p>Provee métodos para:</p>
 * <ul>
 *   <li>Autenticar usuarios por email o username.</li>
 *   <li>Buscar usuarios por su identificador.</li>
 *   <li>Actualizar información personal y contraseñas.</li>
 *   <li>Obtener información general de perfil del usuario.</li>
 * </ul>
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final ChangeEmailService changeEmailService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Carga un usuario por su identificador (email o username) para autenticación.
     *
     * @param identificador email o nombre de usuario.
     * @return instancia de {@link UserDetails} correspondiente al usuario autenticado.
     * @throws UsernameNotFoundException si no se encuentra un usuario con el identificador.
     */
    @Override
    public UserDetails loadUserByUsername(String identificador) throws UsernameNotFoundException {
        log.debug("Intentando autenticar usuario con identificador: {}", identificador);

        Usuario usuario = usuarioRepository.findByEmail(identificador)
                .or(() -> usuarioRepository.findByUsername(identificador))
                .orElseThrow(UserException::notFound);

        log.info("Usuario autenticado: {} (username: {}) con rol {}",
                usuario.getEmail(), usuario.getUsername(), usuario.getTipoUsuario());

        return usuario;
    }

    /**
     * Busca un usuario por su identificador único.
     *
     * @param idUsuario identificador del usuario.
     * @return el usuario encontrado.
     * @throws UserException si no existe un usuario con el id dado.
     */
    public Usuario buscarPorId(Long idUsuario) {
        log.debug("Buscando al usuario con id: {}", idUsuario);
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(UserException::notFound);
    }

    /**
     * Devuelve información resumida del usuario para vistas de perfil o dashboard.
     *
     * @param idUsuario identificador del usuario.
     * @return un {@link UsuarioInfoResponse} con datos resumidos del usuario.
     */
    public UsuarioInfoResponse buscarInfoUsuario(long idUsuario) {
        log.debug("Buscando información del usuario con id: {}", idUsuario);
        Usuario usuario = buscarPorId(idUsuario);
        return new UsuarioInfoResponse(
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getFechaRegistro(),
                usuario.getBuilds().size(),
                usuario.getIncidentes().size(),
                usuario.getSolicitudes().size());
    }

    /**
     * Actualiza la información personal del usuario (actualmente, solo el email).
     *
     * @param id identificador del usuario.
     * @param cambios mapa con los cambios a aplicar, por ejemplo {"email": "nuevo@email.com"}.
     * @throws TooManyAttemptsException si el cambio de email ha sido bloqueado por demasiados intentos.
     * @throws AlreadyRegisteredException si el nuevo email ya está registrado.
     */
    public void actualizarInfoPersonal(Long id, Map<String, String> cambios) {
        String key = "changeEmail:" + id;

        if (changeEmailService.isBlocked(key)) {
            throw TooManyAttemptsException.info();
        }

        Usuario usuario = buscarPorId(id);

        if (cambios.containsKey("email")) {
            String nuevoEmail = cambios.get("email");

            if (usuarioRepository.existsByEmail(nuevoEmail)) {
                throw AlreadyRegisteredException.email();
            }

            usuario.setEmail(nuevoEmail);
            changeEmailService.fail(key);
        }

        usuarioRepository.save(usuario);
    }

    /**
     * Actualiza la contraseña del usuario tras validar la actual y las nuevas credenciales.
     *
     * @param id identificador del usuario.
     * @param cambios mapa con las claves:
     *                <ul>
     *                  <li>"currentPassword": contraseña actual</li>
     *                  <li>"newPassword": nueva contraseña</li>
     *                  <li>"confirmPassword": confirmación de la nueva contraseña</li>
     *                </ul>
     * @throws PasswordException si alguna validación de contraseña falla.
     */
    public void actualizarPassword(Long id, Map<String, String> cambios) {
        Usuario usuario = buscarPorId(id);

        if (!cambios.containsKey("currentPassword") ||
            !cambios.containsKey("newPassword") ||
            !cambios.containsKey("confirmPassword")) {
            throw PasswordException.notFoundData();
        }

        String currentPassword = cambios.get("currentPassword");
        String newPassword = cambios.get("newPassword");
        String confirmPassword = cambios.get("confirmPassword");

        if (!passwordEncoder.matches(currentPassword, usuario.getPassword())) {
            throw PasswordException.isInvalid();
        }

        if (newPassword.equals(currentPassword)) {
            throw PasswordException.equalOldAndNew();
        }

        if (!newPassword.equals(confirmPassword)) {
            throw PasswordException.notEquals();
        }

        usuario.setContrasena(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);
    }
}
