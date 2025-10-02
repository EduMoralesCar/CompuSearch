package com.universidad.compuSearch.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.universidad.compuSearch.entity.Empleado;
import com.universidad.compuSearch.entity.Rol;
import com.universidad.compuSearch.entity.Tienda;
import com.universidad.compuSearch.entity.TipoUsuario;
import com.universidad.compuSearch.entity.Usuario;
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

                System.out.println("Usuario empleado creado con rol ADMIN: admin@test.com / 123!123Aa");
            }

            if (usuarioRepository.findByEmail("tienda@test.com").isEmpty()) {
                Tienda tienda = new Tienda();
                tienda.setEmail("tienda@test.com");
                tienda.setContrasena(passwordEncoder.encode("123!123Aa"));
                tienda.setTipoUsuario(TipoUsuario.TIENDA);
                tienda.setActivo(true);

                // datos de empleado
                tienda.setNombre("Game Center");
                tienda.setTelefono("999999999");
                tienda.setDireccion("Av. Mi Casa");
                tienda.setRuc("11111111111");
                
                usuarioRepository.save(tienda);

                System.out.println("Tienda creado: tienda@test.com / 123!123Aa");
            }

            if (usuarioRepository.findByEmail("usuario@test.com").isEmpty()) {
                Usuario user = new Usuario();
                user.setEmail("usuario@test.com");
                user.setContrasena(passwordEncoder.encode("123!123Aa"));
                user.setTipoUsuario(TipoUsuario.USUARIO);
                user.setActivo(true);
                
                usuarioRepository.save(user);

                System.out.println("Usuario creado: usuario@test.com / 123!123Aa");
            }
        };
    }
}
