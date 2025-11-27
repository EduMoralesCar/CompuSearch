package com.universidad.compusearch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.universidad.compusearch.entity.Atributo;
import com.universidad.compusearch.entity.Categoria;
import com.universidad.compusearch.entity.Empleado;
import com.universidad.compusearch.entity.Rol;
import com.universidad.compusearch.entity.TipoUsuario;
import com.universidad.compusearch.repository.AtributoRepository;
import com.universidad.compusearch.repository.CategoriaRepository;
import com.universidad.compusearch.repository.EmpleadoRepository;
import com.universidad.compusearch.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final AtributoRepository atributoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmpleadoRepository empleadoRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initData() {
        return args -> {
            crearAtributos();
            crearCategorias();
            crearAdmin();
        };
    }

    // ===============================
    // ATRIBUTOS
    // ===============================
    private void crearAtributos() {
        List<String> atributos = List.of(
                "Socket CPU", "Núcleos CPU", "Hilos CPU", "Frecuencia Base CPU",
                "Frecuencia Turbo CPU", "Caché L3 CPU", "Consumo CPU",
                "Socket Motherboard", "Chipset Motherboard",
                "Factor de Forma Motherboard", "Slots RAM", "RAM Máxima",
                "Tipo RAM Compatible", "Puertos M.2", "Puertos SATA",
                "Consumo Motherboard",
                "Tipo RAM", "Capacidad RAM", "Frecuencia RAM", "Latencia CAS RAM",
                "Perfil XMP RAM", "Número de Módulos RAM", "Consumo RAM",
                "Tipo de Almacenamiento", "Capacidad Almacenamiento",
                "Interfaz Almacenamiento", "Velocidad Lectura",
                "Velocidad Escritura", "Consumo Almacenamiento",
                "Interfaz PCIe GPU", "Conectores Energía GPU",
                "Factor de Forma GPU", "Memoria VRAM", "Tipo de VRAM",
                "Frecuencia Base GPU", "Frecuencia Boost GPU",
                "Salidas de Video GPU", "Consumo GPU", "Fabricante GPU",
                "Tipo de Enfriamiento", "Compatibilidad Socket Cooler",
                "Tamaño Ventilador", "Nivel de Ruido Cooler",
                "ARGB Cooler", "Consumo Cooler",
                "Potencia PSU", "Certificación PSU", "Formato PSU",
                "Modularidad PSU", "Eficiencia PSU", "Consumo PSU");

        atributos.forEach(nombre -> atributoRepository.findByNombre(nombre).orElseGet(() -> {
            log.info("Insertando atributo: {}", nombre);

            Atributo atributo = new Atributo();
            atributo.setNombre(nombre);

            return atributoRepository.save(atributo);
        }));
    }

    // ===============================
    // CATEGORÍAS
    // ===============================
    private void crearCategorias() {
        List<Categoria> categorias = List.of(
                crearCategoria("Procesador", "Unidad central de procesamiento (CPU) para computadoras.",
                        "procesador.jpg"),
                crearCategoria("Refrigeración CPU",
                        "Sistemas de enfriamiento para mantener baja la temperatura del procesador.",
                        "refrigeracion_cpu.jpg"),
                crearCategoria("Almacenamiento", "Discos duros y unidades SSD para guardar datos.",
                        "almacenamiento.jpg"),
                crearCategoria("Memoria RAM", "Módulos de memoria para mejorar el rendimiento del sistema.",
                        "memoria_ram.jpg"),
                crearCategoria("Placa Madre", "Tarjetas base que conectan todos los componentes de la computadora.",
                        "placa_madre.jpg"),
                crearCategoria("Tarjeta de Video", "GPU dedicadas para procesamiento gráfico y gaming.",
                        "tarjeta_video.jpg"),
                crearCategoria("Fuente de Poder", "Suministra energía a todos los componentes.", "fuente_poder.jpg"),
                crearCategoria("Otros", "Otros componentes disponibles.", "otros.jpg"));

        categorias.forEach(cat -> categoriaRepository.findByNombre(cat.getNombre()).orElseGet(() -> {
            log.info("Insertando categoría: {}", cat.getNombre());
            return categoriaRepository.save(cat);
        }));
    }

    private Categoria crearCategoria(String nombre, String descripcion, String imagen) {
        Categoria c = new Categoria();
        c.setNombre(nombre);
        c.setDescripcion(descripcion);
        c.setNombreImagen(imagen);
        return c;
    }

    // ===============================
    // ADMINISTRADOR
    // ===============================
    private void crearAdmin() {
        if (usuarioRepository.findByUsername("admin").isPresent()) {
            log.info("El usuario administrador ya existe");
            return;
        }

        log.info("Creando usuario administrador...");

        Empleado admin = new Empleado();
        
        admin.setUsername("admin");
        admin.setEmail("admin@test.com");
        admin.setContrasena(passwordEncoder.encode("Admin123!"));
        admin.setActivo(true);
        admin.setTipoUsuario(TipoUsuario.EMPLEADO);
        admin.setFechaRegistro(LocalDateTime.now());
        admin.setNombre("Administrador");
        admin.setApellido("Principal");
        admin.setRol(Rol.ADMIN);
        admin.setFechaAsignacion(LocalDateTime.now());

        empleadoRepository.save(admin);

        log.info("Administrador creado con éxito");
    }
}
