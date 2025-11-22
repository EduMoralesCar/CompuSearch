package com.universidad.compusearch.config.initializer;

import java.io.IOException;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.universidad.compusearch.entity.*;
import com.universidad.compusearch.repository.EtiquetaRepository;
import com.universidad.compusearch.repository.UsuarioRepository;
import com.universidad.compusearch.util.CargarImagen;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UsuarioInitializer {

    private final UsuarioRepository usuarioRepository;
    private final EtiquetaRepository etiquetaRepository;
    private final PasswordEncoder passwordEncoder;

    public void init() throws IOException {
        crearEmpleado("admin@test.com", "admin", "123!123Aa",
                "Administrador", "Principal", Rol.ADMIN);
        crearTiendas();
        crearUsuario("usuario1@test.com", "usuario1", "123!123Aa", true);
        crearUsuario("usuario2@test.com", "usuario2", "123!123Aa", false);
    }

    // Método genérico para crear un Usuario normal
    private void crearUsuario(String email, String username, String contrasena, boolean activo) {
        if (usuarioRepository.findByEmail(email).isPresent())
            return;

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setUsername(username);
        usuario.setContrasena(passwordEncoder.encode(contrasena));
        usuario.setTipoUsuario(TipoUsuario.USUARIO);
        usuario.setActivo(activo);

        usuarioRepository.save(usuario);
        log.info("Usuario creado: {}", email);
    }

    // Método genérico para crear un Empleado
    private void crearEmpleado(String email, String username, String contrasena,
            String nombre, String apellido, Rol rol) {
        if (usuarioRepository.findByEmail(email).isPresent())
            return;

        Empleado empleado = new Empleado();
        empleado.setEmail(email);
        empleado.setUsername(username);
        empleado.setContrasena(passwordEncoder.encode(contrasena));
        empleado.setTipoUsuario(TipoUsuario.EMPLEADO);
        empleado.setActivo(true);
        empleado.setNombre(nombre);
        empleado.setApellido(apellido);
        empleado.setRol(rol);

        usuarioRepository.save(empleado);
        log.info("Empleado creado: {} ({})", email, rol);
    }

    // Método genérico para crear una Tienda
    private void crearTienda(String email, String username, String nombre,
            String telefono, String direccion, String descripcion,
            String logoPath, String url, List<Etiqueta> etiquetas) throws IOException {

        if (usuarioRepository.findByEmail(email).isPresent())
            return;

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
        tienda.setLogo(CargarImagen.cargarImagen(logoPath));
        tienda.setUrlPagina(url);
        tienda.setEtiquetas(etiquetas);
        tienda.setVerificado(true);

        usuarioRepository.save(tienda);
        log.info("Tienda creada: {}", email);
    }

    // Inicialización de las tiendas
    private void crearTiendas() throws IOException {
        List<Etiqueta> etiquetas = etiquetaRepository.findAll();

        crearTienda("tienda1@test.com", "computershop", "Computer Shop",
                "999999991", "Av. Mi Casa",
                "Especialistas en memorias RAM y almacenamiento.",
                "static/images/tiendas/computershop.png", "https://computershopperu.com/",
                List.of(etiquetas.get(0), etiquetas.get(3), etiquetas.get(5)));

        crearTienda("tienda2@test.com", "impacto", "Impacto",
                "987654321", "Av. Tecnología 123",
                "Componentes de alta gama y equipos gaming.",
                "static/images/tiendas/impacto.jpeg", "https://www.impacto.com.pe/",
                List.of(etiquetas.get(1), etiquetas.get(4), etiquetas.get(5)));

        crearTienda("tienda3@test.com", "sercoplus", "Serco Plus",
                "911223233", "Calle Gamer 456",
                "Tu tienda de confianza para componentes de computadora.",
                "static/images/tiendas/sercoplus.jpg", "https://sercoplus.com/",
                List.of(etiquetas.get(0), etiquetas.get(1), etiquetas.get(2)));
    }
}
