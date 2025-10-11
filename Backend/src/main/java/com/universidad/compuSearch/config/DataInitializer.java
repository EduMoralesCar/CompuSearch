package com.universidad.compusearch.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StreamUtils;

import com.universidad.compusearch.entity.Empleado;
import com.universidad.compusearch.entity.Etiqueta;
import com.universidad.compusearch.entity.Rol;
import com.universidad.compusearch.entity.Tienda;
import com.universidad.compusearch.entity.TipoUsuario;
import com.universidad.compusearch.entity.Usuario;
import com.universidad.compusearch.repository.EtiquetaRepository;
import com.universidad.compusearch.repository.UsuarioRepository;

import io.jsonwebtoken.io.IOException;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initData(UsuarioRepository usuarioRepository, EtiquetaRepository etiquetaRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            List<Etiqueta> etiquetas = new ArrayList<>();

            String[] nombresEtiquetas = {
                    "Envío gratis", "Descuento", "Nuevo", "Armado PC", "Soporte técnico", "Gaming", "Oficina"
            };

            for (int i = 0; i < nombresEtiquetas.length; i++) {
                String nombre = nombresEtiquetas[i];
                if (etiquetaRepository.findByNombre(nombre).isEmpty()) {
                    Etiqueta etiqueta = new Etiqueta(i + 1, nombre);
                    etiquetaRepository.save(etiqueta);
                    etiquetas.add(etiqueta);
                    logger.info("Etiqueta creada: {}", nombre);
                } else {
                    etiquetas.add(etiquetaRepository.findByNombre(nombre).get(i));
                    logger.debug("Etiqueta ya existe: {}", nombre);
                }
            }

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
                // Tienda 1
                if (usuarioRepository.findByEmail("tienda1@test.com").isEmpty()) {
                    Tienda tienda1 = new Tienda();
                    tienda1.setEmail("tienda1@test.com");
                    tienda1.setUsername("computershop");
                    tienda1.setContrasena(passwordEncoder.encode("123!123Aa"));
                    tienda1.setTipoUsuario(TipoUsuario.TIENDA);
                    tienda1.setActivo(true);
                    tienda1.setNombre("Computer Shop");
                    tienda1.setTelefono("999999991");
                    tienda1.setDireccion("Av. Mi Casa");
                    tienda1.setDescripcion("Especialistas en tecnología de primer nivel");
                    tienda1.setLogo(cargarImagen("static/images/computershop.png"));
                    tienda1.setUrlPagina("https://computershopperu.com/");
                    tienda1.setEtiquetas(List.of(etiquetas.get(0), etiquetas.get(3), etiquetas.get(5)));
                    tienda1.setVerificado(true);
                    usuarioRepository.save(tienda1);
                    logger.info("Tienda creada: tienda@test.com / 123!123Aa");
                }

                // Tienda 2
                if (usuarioRepository.findByEmail("tienda2@test.com").isEmpty()) {
                    Tienda tienda2 = new Tienda();
                    tienda2.setEmail("tienda2@test.com");
                    tienda2.setUsername("impacto");
                    tienda2.setContrasena(passwordEncoder.encode("123!123Aa"));
                    tienda2.setTipoUsuario(TipoUsuario.TIENDA);
                    tienda2.setActivo(true);
                    tienda2.setNombre("Impacto");
                    tienda2.setTelefono("987654321");
                    tienda2.setDireccion("Av. Tecnología 123");
                    tienda2.setDescripcion("Todo en hardware y gaming");
                    tienda2.setEtiquetas(List.of(etiquetas.get(1), etiquetas.get(4), etiquetas.get(5)));
                    tienda2.setLogo(cargarImagen("static/images/impacto.jpeg"));
                    tienda2.setUrlPagina("https://www.impacto.com.pe/");
                    tienda2.setVerificado(true);
                    usuarioRepository.save(tienda2);
                    logger.info("Tienda creada: tienda2@test.com / 123!123Aa");
                }

                // Tienda 3
                if (usuarioRepository.findByEmail("tienda3@test.com").isEmpty()) {
                    Tienda tienda3 = new Tienda();
                    tienda3.setEmail("tienda3@test.com");
                    tienda3.setUsername("pixelstore");
                    tienda3.setContrasena(passwordEncoder.encode("123!123Aa"));
                    tienda3.setTipoUsuario(TipoUsuario.TIENDA);
                    tienda3.setActivo(true);
                    tienda3.setNombre("Pixel Store");
                    tienda3.setTelefono("911223344");
                    tienda3.setDireccion("Calle Gamer 456");
                    tienda3.setEtiquetas(List.of(etiquetas.get(2), etiquetas.get(6)));
                    tienda3.setDescripcion("Accesorios y periféricos de calidad");
                    tienda3.setLogo(new byte[0]);
                    tienda3.setUrlPagina(null);
                    tienda3.setVerificado(true);
                    usuarioRepository.save(tienda3);
                    logger.info("Tienda creada: tienda3@test.com / 123!123Aa");
                }

                // Tienda 3
                if (usuarioRepository.findByEmail("tienda4@test.com").isEmpty()) {
                    Tienda tienda4 = new Tienda();
                    tienda4.setEmail("tienda4@test.com");
                    tienda4.setUsername("sercoplus");
                    tienda4.setContrasena(passwordEncoder.encode("123!123Aa"));
                    tienda4.setTipoUsuario(TipoUsuario.TIENDA);
                    tienda4.setActivo(true);
                    tienda4.setNombre("Serco Plus");
                    tienda4.setTelefono("911223233");
                    tienda4.setDireccion("Calle Gamer 456");
                    tienda4.setDescripcion("Accesorios y periféricos de calidad");
                    tienda4.setLogo(cargarImagen("static/images/sercoplus.jpg"));
                    tienda4.setEtiquetas(List.of(etiquetas.get(0), etiquetas.get(1), etiquetas.get(2)));
                    tienda4.setUrlPagina("https://sercoplus.com/");
                    tienda4.setVerificado(true);
                    usuarioRepository.save(tienda4);
                    logger.info("Tienda creada: tienda4@test.com / 123!123Aa");
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

    private byte[] cargarImagen(String rutaRelativa) throws java.io.IOException {
        ClassPathResource imgFile = new ClassPathResource(rutaRelativa);
        if (!imgFile.exists()) {
            logger.warn("Imagen no encontrada en el classpath: {}", rutaRelativa);
            return null;
        }
        try {
            return StreamUtils.copyToByteArray(imgFile.getInputStream());
        } catch (IOException e) {
            logger.error("Error leyendo imagen: {}", rutaRelativa, e);
            return null;
        }
    }

}
