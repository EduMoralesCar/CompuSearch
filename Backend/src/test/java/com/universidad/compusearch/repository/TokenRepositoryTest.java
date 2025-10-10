package com.universidad.compusearch.repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.universidad.compusearch.entity.EstadoToken;
import com.universidad.compusearch.entity.TipoToken;
import com.universidad.compusearch.entity.TipoUsuario;
import com.universidad.compusearch.entity.Token;
import com.universidad.compusearch.entity.Usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;;

@DataJpaTest
@ActiveProfiles("test")
class TokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void testFindByTokenAndTipo() {
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setEmail("test@correo.com");
        usuario.setContrasena("123456");
        usuario.setTipoUsuario(TipoUsuario.USUARIO);
        usuarioRepository.save(usuario);

        Token token = new Token();
        token.setToken("abc123");
        token.setTipo(TipoToken.REFRESH);
        token.setEstado(EstadoToken.ACTIVO);
        token.setFechaExpiracion(LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.UTC));
        token.setIpDispositivo("192.168.0.1");
        token.setUsuario(usuario);
        tokenRepository.save(token);

        Optional<Token> result = tokenRepository.findByTokenAndTipo("abc123", TipoToken.REFRESH);
        assertTrue(result.isPresent());
        assertEquals("abc123", result.get().getToken());
        assertEquals(TipoToken.REFRESH, result.get().getTipo());
    }

    @Test
    void testFindByUsuarioAndIpDispositivoAndTipo() {
        Usuario usuario = new Usuario();
        usuario.setUsername("deviceuser");
        usuario.setEmail("device@correo.com");
        usuario.setContrasena("abcdef");
        usuario.setTipoUsuario(TipoUsuario.USUARIO);
        usuarioRepository.save(usuario);

        Token token = new Token();
        token.setToken("xyz789");
        token.setTipo(TipoToken.RESET);
        token.setEstado(EstadoToken.ACTIVO);
        token.setFechaExpiracion(LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.UTC));
        token.setIpDispositivo("192.168.1.100");
        token.setUsuario(usuario);
        tokenRepository.save(token);

        Optional<Token> result = tokenRepository.findByUsuarioAndIpDispositivoAndTipo(
                usuario, "192.168.1.100", TipoToken.RESET);

        assertTrue(result.isPresent());
        assertEquals("xyz789", result.get().getToken());
        assertEquals("192.168.1.100", result.get().getIpDispositivo());
        assertEquals(TipoToken.RESET, result.get().getTipo());
    }
}