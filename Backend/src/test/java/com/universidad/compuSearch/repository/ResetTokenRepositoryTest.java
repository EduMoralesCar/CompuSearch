package com.universidad.compuSearch.repository;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.universidad.compuSearch.entity.EstadoToken;
import com.universidad.compuSearch.entity.ResetToken;
import com.universidad.compuSearch.entity.TipoUsuario;
import com.universidad.compuSearch.entity.Usuario;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ResetTokenRepositoryTest {

    @Autowired
    private ResetTokenRepository resetTokenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Guardar y recuperar ResetToken por token")
    void testFindByToken() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setContrasena("1234");
        usuario.setTipoUsuario(TipoUsuario.USUARIO);
        usuario.setActivo(true);
        usuarioRepository.save(usuario);

        ResetToken token = new ResetToken();
        token.setUsuario(usuario);
        token.setToken("reset-token-abc");
        token.setEstado(EstadoToken.ACTIVO);
        token.setFechaCreacion(Instant.now());
        token.setFechaExpiracion(Instant.now().plusSeconds(3600));
        resetTokenRepository.save(token);

        Optional<ResetToken> found = resetTokenRepository.findByToken("reset-token-abc");

        assertThat(found).isPresent();
        assertThat(found.get().getToken()).isEqualTo("reset-token-abc");
        assertThat(found.get().getUsuario().getEmail()).isEqualTo("test@example.com");
        assertThat(found.get().getEstado()).isEqualTo(EstadoToken.ACTIVO);
    }

    @Test
    @DisplayName("Buscar token inexistente devuelve empty")
    void testFindByToken_NotFound() {
        Optional<ResetToken> found = resetTokenRepository.findByToken("no-existe");
        assertThat(found).isEmpty();
    }
}
