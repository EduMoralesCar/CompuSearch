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

// Servicio de usuario
@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final ChangeEmailService changeEmailService;
    private final PasswordEncoder passwordEncoder;

    // Sobreescritura de UserDetails para cargar el usuario
    @Override
    public UserDetails loadUserByUsername(String identificador) throws UsernameNotFoundException {
        log.debug("Intentando autenticar usuario con identificador: {}", identificador);

        // Buscar primero por email, si no, buscar por username
        Usuario usuario = usuarioRepository.findByEmail(identificador)
                .or(() -> usuarioRepository.findByUsername(identificador))
                .orElseThrow(() -> {
                    log.warn("No se encontró usuario con email/username: {}", identificador);
                    return UserException.notFound();
                });

        log.info("Usuario autenticado: {} (username: {}) con rol {}",
                usuario.getEmail(), usuario.getUsername(), usuario.getTipoUsuario());

        return usuario;
    }

    // Buscar el usuario por id
    public Usuario buscarPorId(Long idUsuario) {
        log.debug("Buscando al usuario con id: {}", idUsuario);
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> {
                    log.warn("Usuario con el id {} no encontrado", idUsuario);
                    return UserException.notFound();
                });

        log.info("Usuario con id {} encontrado", idUsuario);
        return usuario;
    }

    public UsuarioInfoResponse buscarInfoUsuario(long idUsuario) {
        log.debug("Buscando informacion del usuario con id: {}", idUsuario);
        Usuario usuario = buscarPorId(idUsuario);
        return new UsuarioInfoResponse(
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getFechaRegistro(),
                usuario.getBuilds().size(),
                usuario.getIncidentes().size(),
                usuario.getSolicitudes().size());
    }

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

        // Validar contraseña actual
        if (!passwordEncoder.matches(currentPassword, usuario.getPassword())) {
            throw PasswordException.isInvalid();
        }

        if (newPassword.equals(currentPassword)) {
            throw PasswordException.equalOldAndNew();
        }

        // Validar que la nueva contraseña y confirmación coincidan
        if (!newPassword.equals(confirmPassword)) {
            throw PasswordException.notEquals();
        }

        // Guardar la nueva contraseña
        usuario.setContrasena(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);
    }

}