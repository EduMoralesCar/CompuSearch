package com.universidad.compuSearch.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.universidad.compuSearch.entity.TipoUsuario;
import com.universidad.compuSearch.entity.Usuario;
import com.universidad.compuSearch.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.findByEmail("admin@test.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setEmail("admin@test.com");
                admin.setContrasena(passwordEncoder.encode("12345"));
                admin.setTipoUsuario(TipoUsuario.USUARIO);
                admin.setActivo(true);

                usuarioRepository.save(admin);
                System.out.println("✅ Usuario admin creado: admin@test.com / 12345");
            }
        };
    }
}
