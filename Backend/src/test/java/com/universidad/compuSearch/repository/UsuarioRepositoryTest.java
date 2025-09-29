package com.universidad.compuSearch.repository;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.universidad.compuSearch.entity.TipoUsuario;
import static org.assertj.core.api.Assertions.assertThat;
import com.universidad.compuSearch.entity.Usuario;

@DataJpaTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void findByEmail_UsuarioExistente_RetornaUsuario(){
        Usuario testUsuario = new Usuario();
        testUsuario.setEmail("usuariotest@test.com");
        testUsuario.setContrasena("123");
        testUsuario.setTipoUsuario(TipoUsuario.USUARIO);

        usuarioRepository.save(testUsuario);

        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail(testUsuario.getEmail());

        assertThat(usuarioEncontrado).isPresent();
        assertThat(usuarioEncontrado.get().getTipoUsuario()).isEqualTo(TipoUsuario.USUARIO);
        assertThat(usuarioEncontrado.get().isActivo()).isTrue();
    }

    @Test
    void findByEmail_UsuarioInexistente_RetornarNull(){
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail("usuariotest@test.com");
        
        assertThat(usuarioEncontrado).isNotPresent();
    }
}
