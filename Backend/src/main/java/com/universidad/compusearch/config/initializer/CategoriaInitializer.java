package com.universidad.compusearch.config.initializer;

import org.springframework.stereotype.Component;

import com.universidad.compusearch.entity.Categoria;
import com.universidad.compusearch.repository.CategoriaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Crear categorias al inicio
@Component
@RequiredArgsConstructor
@Slf4j
public class CategoriaInitializer {

        private final CategoriaRepository categoriaRepository;

        public void init() {

                // Nombre de los categorias
                Object[][] categorias = {
                                { "Procesador",
                                                "Unidad central de procesamiento (CPU) para computadoras.",
                                                "procesador.jpg" },

                                { "Refrigeración CPU",
                                                "Sistemas de enfriamiento para mantener baja la temperatura del procesador.",
                                                "refrigeracion_cpu.jpg" },

                                { "Almacenamiento",
                                                "Discos duros y unidades SSD para guardar datos.",
                                                "almacenamiento.jpg" },

                                { "Memoria RAM",
                                                "Módulos de memoria para mejorar el rendimiento del sistema.",
                                                "memoria_ram.jpg" },

                                { "Placa Madre",
                                                "Tarjetas base que conectan todos los componentes de la computadora.",
                                                "placa_madre.jpg" },

                                { "Tarjeta de Video",
                                                "GPU dedicadas para procesamiento gráfico y gaming.",
                                                "tarjeta_video.jpg" },

                                { "Fuente de Poder",
                                                "Suministra energía eléctrica a todos los componentes.",
                                                "fuente_poder.jpg" },
                                { "Otros", 
                                        "Explora entre otros componentes disponibles.",
                                        "otros.jpg"}
                };

                // Insercion de los categorias
                int nuevas = 0, existentes = 0;

                for (Object[] data : categorias) {
                        String nombre = (String) data[0];
                        String descripcion = (String) data[1];
                        String nombreImagen = (String) data[2];

                        var categoriaOptional = categoriaRepository.findByNombre(nombre);

                        if (categoriaOptional.isPresent()) {
                                existentes++;
                        } else {
                                Categoria categoria = new Categoria();
                                categoria.setNombre(nombre);
                                categoria.setDescripcion(descripcion);
                                categoria.setNombreImagen(nombreImagen);
                                categoriaRepository.save(categoria);
                                nuevas++;
                        }
                }

                log.info("Inicialización de categorías completada: {} nuevas, {} existentes, total {}",
                                nuevas, existentes, categorias.length);

        }
}
