package com.universidad.compusearch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.universidad.compusearch.entity.Empleado;
import com.universidad.compusearch.entity.Rol;
import com.universidad.compusearch.entity.Tienda;
import com.universidad.compusearch.entity.TipoUsuario;
import com.universidad.compusearch.entity.Usuario;
import com.universidad.compusearch.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initData(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            try {
                // Crear usuario EMPLEADO ADMIN
                if (usuarioRepository.findByEmail("admin@test.com").isEmpty()) {
                    Empleado admin = new Empleado();
                    admin.setEmail("admin@test.com");
                    admin.setUsername("admin");
                    admin.setContrasena(passwordEncoder.encode("123!123Aa"));
                    admin.setTipoUsuario(TipoUsuario.EMPLEADO);
                    admin.setActivo(true);

                    // Datos de empleado
                    admin.setNombre("Administrador");
                    admin.setApellido("Principal");
                    admin.setRol(Rol.ADMIN);

                    usuarioRepository.save(admin);
                    logger.info("Usuario empleado creado con rol ADMIN: admin@test.com / 123!123Aa");
                } else {
                    logger.debug("Usuario admin@test.com ya existe en la BD, no se creó uno nuevo.");
                }

                // Crear usuario TIENDA
                if (usuarioRepository.findByEmail("tienda@test.com").isEmpty()) {
                    Tienda tienda = new Tienda();
                    tienda.setEmail("tienda@test.com");
                    tienda.setUsername("tienda");
                    tienda.setContrasena(passwordEncoder.encode("123!123Aa"));
                    tienda.setTipoUsuario(TipoUsuario.TIENDA);
                    tienda.setActivo(true);

                    // Datos de tienda
                    tienda.setNombre("Game Center");
                    tienda.setTelefono("999999999");
                    tienda.setDireccion("Av. Mi Casa");
                    tienda.setRuc("11111111111");
                    tienda.setLogo(null);
                    tienda.setUrlPagina(null);

                    usuarioRepository.save(tienda);
                    logger.info("Tienda creada: tienda@test.com / 123!123Aa");
                } else {
                    logger.debug("Tienda tienda@test.com ya existe en la BD.");
                }

                // Crear usuario normal
                if (usuarioRepository.findByEmail("usuario@test.com").isEmpty()) {
                    Usuario user = new Usuario();
                    user.setEmail("usuario@test.com");
                    user.setUsername("usuario");
                    user.setContrasena(passwordEncoder.encode("123!123Aa"));
                    user.setTipoUsuario(TipoUsuario.USUARIO);
                    user.setActivo(true);

                    usuarioRepository.save(user);
                    logger.info("Usuario creado: usuario@test.com / 123!123Aa");
                } else {
                    logger.debug("Usuario usuario@test.com ya existe en la BD.");
                }

            } catch (Exception e) {
                logger.error("Error inicializando datos: {}", e.getMessage(), e);
            }
        };
    }
}
