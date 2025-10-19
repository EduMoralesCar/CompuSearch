package com.universidad.compusearch.config.initializer;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.universidad.compusearch.entity.*;
import com.universidad.compusearch.repository.EtiquetaRepository;
import com.universidad.compusearch.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsuarioInitializer {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioInitializer.class);

    private final UsuarioRepository usuarioRepository;
    private final EtiquetaRepository etiquetaRepository;
    private final PasswordEncoder passwordEncoder;

    public void init() throws IOException {
        crearAdmin();
        crearTiendas();
        crearUsuarioNormal();
    }

    private void crearAdmin() {
        if (usuarioRepository.findByEmail("admin@test.com").isEmpty()) {
            Empleado empleado = new Empleado();
            empleado.setEmail("admin@test.com");
            empleado.setUsername("admin");
            empleado.setContrasena(passwordEncoder.encode("123!123Aa"));
            empleado.setTipoUsuario(TipoUsuario.EMPLEADO);
            empleado.setActivo(true);
            empleado.setNombre("Administrador");
            empleado.setApellido("Principal");
            empleado.setRol(Rol.ADMIN);
            usuarioRepository.save(empleado);
            logger.info("Empleado ADMIN creado");
        }
    }

    private void crearTiendas() throws IOException {
        List<Etiqueta> etiquetas = etiquetaRepository.findAll();

        crearTienda(
                "tienda1@test.com", "computershop", "Computer Shop",
                "999999991", "Av. Mi Casa",
                "Especialistas en memorias RAM y almacenamiento.",
                "static/images/computershop.png", "https://computershopperu.com/",
                List.of(etiquetas.get(0), etiquetas.get(3), etiquetas.get(5)));
        crearTienda(
                "tienda2@test.com", "impacto", "Impacto",
                "987654321", "Av. Tecnología 123",
                "Componentes de alta gama y equipos gaming.",
                "static/images/impacto.jpeg", "https://www.impacto.com.pe/",
                List.of(etiquetas.get(1), etiquetas.get(4), etiquetas.get(5)));
        crearTienda(
                "tienda3@test.com", "sercoplus", "Serco Plus",
                "911223233", "Calle Gamer 456",
                "Tu tienda de confianza para componentes de computadora.",
                "static/images/sercoplus.jpg", "https://sercoplus.com/",
                List.of(etiquetas.get(0), etiquetas.get(1), etiquetas.get(2)));
    }

    private void crearTienda(String email, String username, String nombre,
            String telefono, String direccion, String descripcion,
            String logoPath, String url, List<Etiqueta> etiquetas) throws IOException {

        if (usuarioRepository.findByEmail(email).isEmpty()) {
            Tienda tienda = new Tienda();
            tienda.setEmail(email);
            tienda.setUsername(username);
            tienda.setContrasena(passwordEncoder.encode("123!123Aa"));
            tienda.setTipoUsuario(TipoUsuario.TIENDA);
            tienda.setActivo(true);
            tienda.setNombre(nombre);
            tienda.setTelefono(telefono);
            tienda.setDireccion(direccion);
            tienda.setDescripcion(descripcion);
            tienda.setLogo(cargarImagen(logoPath));
            tienda.setUrlPagina(url);
            tienda.setEtiquetas(etiquetas);
            tienda.setVerificado(true);
            usuarioRepository.save(tienda);
            logger.info("Tienda creada: {}", email);
        }
    }

    private void crearUsuarioNormal() {
        if (usuarioRepository.findByEmail("usuario@test.com").isEmpty()) {
            Usuario user = new Usuario();
            user.setEmail("usuario@test.com");
            user.setUsername("usuario");
            user.setContrasena(passwordEncoder.encode("123!123Aa"));
            user.setTipoUsuario(TipoUsuario.USUARIO);
            user.setActivo(true);
            usuarioRepository.save(user);
            logger.info("Usuario normal creado");
        }
    }

    private byte[] cargarImagen(String rutaRelativa) throws IOException {
        ClassPathResource imgFile = new ClassPathResource(rutaRelativa);
        if (!imgFile.exists())
            return null;
        return StreamUtils.copyToByteArray(imgFile.getInputStream());
    }
}
