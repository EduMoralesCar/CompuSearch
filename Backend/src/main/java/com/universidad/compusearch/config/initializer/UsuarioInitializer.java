package com.universidad.compusearch.config.initializer;

import java.io.IOException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.universidad.compusearch.entity.*;
import com.universidad.compusearch.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UsuarioInitializer {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public void init() throws IOException {
        crearEmpleado("admin@test.com", "admin", "123!123Aa",
                "Administrador", "Principal", Rol.ADMIN);
        crearUsuario("usuario1@test.com", "usuario1", "123!123Aa", true);
        crearUsuario("usuario2@test.com", "usuario2", "123!123Aa", false);
    }

    // Método genérico para crear un Usuario normal
    private void crearUsuario(String email, String username, String contrasena, boolean activo) {
        if (usuarioRepository.findByEmail(email).isPresent())
            return;

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setUsername(username);
        usuario.setContrasena(passwordEncoder.encode(contrasena));
        usuario.setTipoUsuario(TipoUsuario.USUARIO);
        usuario.setActivo(activo);

        usuarioRepository.save(usuario);
        log.info("Usuario creado: {}", email);
    }

    // Método genérico para crear un Empleado
    private void crearEmpleado(String email, String username, String contrasena,
            String nombre, String apellido, Rol rol) {
        if (usuarioRepository.findByEmail(email).isPresent())
            return;

        Empleado empleado = new Empleado();
        empleado.setEmail(email);
        empleado.setUsername(username);
        empleado.setContrasena(passwordEncoder.encode(contrasena));
        empleado.setTipoUsuario(TipoUsuario.EMPLEADO);
        empleado.setActivo(true);
        empleado.setNombre(nombre);
        empleado.setApellido(apellido);
        empleado.setRol(rol);

        usuarioRepository.save(empleado);
        log.info("Empleado creado: {} ({})", email, rol);
    }
}
