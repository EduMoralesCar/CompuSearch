package com.universidad.compusearch.config.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.universidad.compusearch.entity.Categoria;
import com.universidad.compusearch.repository.CategoriaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoriaInitializer {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaInitializer.class);
    private final CategoriaRepository categoriaRepository;

    public void init() {
        String[] categorias = {
            "Procesador", "Refrigeración CPU", "Almacenamiento",
            "Memoria RAM", "Placa Madre", "Tarjeta de Video", "Fuente de Poder"
        };

        int nuevas = 0, existentes = 0;
        for (String nombre : categorias) {
            if (categoriaRepository.findByNombre(nombre).isEmpty()) {
                Categoria categoria = new Categoria();
                categoria.setNombre(nombre);
                categoriaRepository.save(categoria);
                nuevas++;
            } else {
                existentes++;
            }
        }
        logger.info("Categorías: {} nuevas, {} existentes, total {}", nuevas, existentes, categorias.length);
    }
}
