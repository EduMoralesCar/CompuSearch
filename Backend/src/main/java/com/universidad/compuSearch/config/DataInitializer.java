package com.universidad.compuSearch.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.universidad.compuSearch.entity.Empleado;
import com.universidad.compuSearch.entity.Rol;
import com.universidad.compuSearch.entity.TipoUsuario;
import com.universidad.compuSearch.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.findByEmail("admin@test.com").isEmpty()) {
                Empleado admin = new Empleado();
                admin.setEmail("admin@test.com");
                admin.setContrasena(passwordEncoder.encode("123!123Aa"));
                admin.setTipoUsuario(TipoUsuario.EMPLEADO);
                admin.setActivo(true);

                // datos de empleado
                admin.setNombre("Administrador");
                admin.setApellido("Principal");
                admin.setRol(Rol.ADMIN);

                usuarioRepository.save(admin);

                System.out.println("✅ Usuario admin creado con rol ADMIN: admin@test.com / 123!123Aa");
            }
        };
    }
}
