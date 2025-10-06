package com.universidad.compusearch.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.Usuario;
import com.universidad.compusearch.exception.UserException;
import com.universidad.compusearch.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Override
    public UserDetails loadUserByUsername(String identificador) throws UsernameNotFoundException {
        logger.debug("Intentando autenticar usuario con identificador: {}", identificador);

        // Buscar primero por email, si no, buscar por username
        Usuario usuario = usuarioRepository.findByEmail(identificador)
                .or(() -> usuarioRepository.findByUsername(identificador))
                .orElseThrow(() -> {
                    logger.warn("No se encontró usuario con email/username: {}", identificador);
                    return UserException.notFound();
                });

        logger.info("Usuario autenticado: {} (username: {}) con rol {}",
                usuario.getEmail(), usuario.getUsername(), usuario.getTipoUsuario());

        return usuario;
    }
}