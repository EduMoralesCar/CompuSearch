package com.universidad.compusearch.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.Categoria;
import com.universidad.compusearch.repository.CategoriaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaService.class);
    private final CategoriaRepository categoriaRepository;

    public List<String> obtenerTodas() {
        logger.debug("Buscando todas las etiquetas en la base de datos...");
        List<String> categorias = categoriaRepository.findAllNombres();
        logger.info("Se encontraron {} etiquetas", categorias.size());
        return categorias;
    }

    public Categoria buscarPorNombre(String nombre) {
        logger.debug("Buscando etiqueta con nombre exacto: {}", nombre);
        Optional<Categoria> categoria = categoriaRepository.findByNombre(nombre);

        if (categoria.isEmpty()) {
            logger.warn("No se encontró ninguna etiqueta con el nombre: {}", nombre);
            return null;
        } else {
            logger.info("Etiqueta encontrada: {}", categoria.get().getNombre());
            return categoria.get();
        }
    }
}
