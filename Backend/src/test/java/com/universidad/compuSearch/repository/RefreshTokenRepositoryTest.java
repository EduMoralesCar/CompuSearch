package com.universidad.compuSearch.repository;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

import com.universidad.compuSearch.entity.RefreshToken;
import com.universidad.compuSearch.entity.Usuario;
import com.universidad.compuSearch.entity.TipoUsuario;
import com.universidad.compuSearch.entity.EstadoToken;

@DataJpaTest
@ActiveProfiles("test")
public class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Guardar y recuperar refresh token por usuario y dispositivo")
    void testFindByUsuarioAndDispositivoAndEstado() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setContrasena("1234");
        usuario.setTipoUsuario(TipoUsuario.USUARIO);
        usuario.setActivo(true);
        usuarioRepository.save(usuario);

        RefreshToken token = new RefreshToken();
        token.setUsuario(usuario);
        token.setToken("refresh-token-123");
        token.setEstado(EstadoToken.ACTIVO);
        token.setFechaCreacion(Instant.now());
        token.setFechaExpiracion(Instant.now().plusSeconds(3600));
        token.setDispositivo("mobile");
        refreshTokenRepository.save(token);

        Optional<RefreshToken> found = refreshTokenRepository.findByUsuarioAndDispositivoAndEstado(
                usuario, "mobile", EstadoToken.ACTIVO);

        assertThat(found).isPresent();
        assertThat(found.get().getToken()).isEqualTo("refresh-token-123");
        assertThat(found.get().getDispositivo()).isEqualTo("mobile");
        assertThat(found.get().getEstado()).isEqualTo(EstadoToken.ACTIVO);
    }

    @Test
    @DisplayName("Buscar token inexistente devuelve empty")
    void testFindByUsuarioAndDispositivoAndEstado_NotFound() {
        Usuario usuario = new Usuario();
        usuario.setEmail("otro@example.com");
        usuario.setContrasena("1234");
        usuario.setTipoUsuario(TipoUsuario.USUARIO);
        usuario.setActivo(true);
        usuarioRepository.save(usuario);

        Optional<RefreshToken> found = refreshTokenRepository.findByUsuarioAndDispositivoAndEstado(
                usuario, "tablet", EstadoToken.ACTIVO);

        assertThat(found).isEmpty();
    }
}
