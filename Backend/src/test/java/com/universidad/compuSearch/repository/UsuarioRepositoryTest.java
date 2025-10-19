package com.universidad.compusearch.repository;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.universidad.compusearch.entity.TipoUsuario;
import com.universidad.compusearch.entity.Usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;;

@DataJpaTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void testFindByEmail() {
        Usuario usuario = new Usuario();
        usuario.setUsername("correoUser");
        usuario.setEmail("correo@ejemplo.com");
        usuario.setContrasena("123456");
        usuario.setTipoUsuario(TipoUsuario.USUARIO);
        usuarioRepository.save(usuario);

        Optional<Usuario> result = usuarioRepository.findByEmail("correo@ejemplo.com");
        assertTrue(result.isPresent());
        assertEquals("correoUser", result.get().getUsername());
    }

    @Test
    void testFindByUsername() {
        Usuario usuario = new Usuario();
        usuario.setUsername("nombreUser");
        usuario.setEmail("nombre@ejemplo.com");
        usuario.setContrasena("abcdef");
        usuario.setTipoUsuario(TipoUsuario.USUARIO);
        usuarioRepository.save(usuario);

        Optional<Usuario> result = usuarioRepository.findByUsername("nombreUser");
        assertTrue(result.isPresent());
        assertEquals("nombre@ejemplo.com", result.get().getEmail());
    }

    @Test
    void testFindByEmailAndUsername() {
        Usuario usuario = new Usuario();
        usuario.setUsername("comboUser");
        usuario.setEmail("combo@ejemplo.com");
        usuario.setContrasena("combo123");
        usuario.setTipoUsuario(TipoUsuario.USUARIO);
        usuarioRepository.save(usuario);

        Optional<Usuario> byEmail = usuarioRepository.findByEmail("combo@ejemplo.com");
        Optional<Usuario> byUsername = usuarioRepository.findByUsername("comboUser");

        assertTrue(byEmail.isPresent());
        assertTrue(byUsername.isPresent());
        assertEquals(byEmail.get().getIdUsuario(), byUsername.get().getIdUsuario());
    }
}
