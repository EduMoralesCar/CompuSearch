package com.universidad.compusearch.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.Usuario;
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
    public Usuario buscarPorId(Long idUsuario){
        log.debug("Buscando al usuario con id: {}", idUsuario);
        Usuario usuario = usuarioRepository.findById(idUsuario)
        .orElseThrow(() -> {
            log.warn("Usuario con el id {} no encontrado", idUsuario);
            return UserException.notFound();
        });

        log.info("Usuario con id {} encontrado", idUsuario);
        return usuario;
    }
}