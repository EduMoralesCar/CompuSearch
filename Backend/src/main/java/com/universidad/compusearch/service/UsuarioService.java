package com.universidad.compusearch.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.dto.PasswordResetRequest;
import com.universidad.compusearch.dto.UsuarioInfoResponse;
import com.universidad.compusearch.dto.UsuarioResponse;
import com.universidad.compusearch.entity.TipoUsuario;
import com.universidad.compusearch.entity.Usuario;
import com.universidad.compusearch.exception.PasswordException;
import com.universidad.compusearch.exception.TooManyAttemptsException;
import com.universidad.compusearch.exception.UserException;
import com.universidad.compusearch.repository.UsuarioRepository;
import com.universidad.compusearch.util.Mapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final ChangeEmailService changeEmailService;
    private final PasswordEncoder passwordEncoder;

    // Cargar usuario por identificador (email o username)
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

    // Buscar usuario por id
    public Usuario buscarPorId(Long idUsuario) {
        log.debug("Buscando al usuario con id: {}", idUsuario);
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> UserException.notFoundId(idUsuario));
    }
    
    // Actualizar informacion del usuario
    public void actualizarInfoPersonal(Long id, Map<String, String> cambios) {
        String key = "changeInfo_" + id;

        if (changeEmailService.isBlocked(key)) {
            throw TooManyAttemptsException.info();
        }

        Usuario usuario = buscarPorId(id);

        if (cambios.containsKey("email")) {
            String nuevoEmail = cambios.get("email");

            if (usuarioRepository.existsByEmail(nuevoEmail)) {
                throw UserException.emailAlredyRegistered(nuevoEmail);
            }

            usuario.setEmail(nuevoEmail);
            changeEmailService.fail(key);
        }

        usuarioRepository.save(usuario);
    }

    // Actualizar contraseña
    public void actualizarPassword(Long id, PasswordResetRequest request) {
        Usuario usuario = buscarPorId(id);

        String currentPassword = request.getActualConstrasena();
        String newPassword = request.getContrasenaNueva();
        String confirmPassword = request.getContrasenaConfirmada();

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

    // Actualizar contraseña por administrador
    public void actualizarPasswordByAdmin(Long id, String newPassword){
        Usuario usuario = buscarPorId(id);
        usuario.setContrasena(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);
    }

    // Guardar usuario
    public Usuario guardarUsuario(Usuario usuario) {
        log.info("Guardando usuario con username {}", usuario.getUsername());
        return usuarioRepository.save(usuario);
    }

    // Obtener usuario paginados
    public Page<UsuarioResponse> obtenerUsuariosPaginados(int page, int size, String username) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaRegistro").descending());

        Page<Usuario> usuariosPage;

        if (username != null && !username.trim().isEmpty()) {
            log.info("Buscando usuarios con username similar a: {}", username);
            usuariosPage = usuarioRepository.findByUsernameContainingIgnoreCaseAndTipoUsuario(
                    username, TipoUsuario.USUARIO, pageable);
        } else {
            log.info("Obteniendo todos los usuarios tipo USUARIO paginados");
            usuariosPage = usuarioRepository.findByTipoUsuario(TipoUsuario.USUARIO, pageable);
        }

        return usuariosPage.map(Mapper::mapToUsuario);
    }

    // Actualizar estado del usuario
    @Transactional
    public void actualizarActivo(Long id, boolean activo) {
        log.debug("Verificando existencia del usuario con ID: {}", id);

        if (!usuarioRepository.existsById(id)) {
            log.warn("Intento de actualizar 'activo' para usuario inexistente con ID: {}", id);
            throw UserException.notFound();
        }

        usuarioRepository.actualizarActivo(id, activo);
        log.info("Estado 'activo' del usuario con ID {} actualizado a {}", id, activo);
    }

    public UsuarioInfoResponse buscarInfoUsuario(long idUsuario) {
        log.debug("Buscando información del usuario con id: {}", idUsuario);
        Usuario usuario = buscarPorId(idUsuario);

        return Mapper.mapToUsuarioInfo(usuario);
    } 
}
