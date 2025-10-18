package com.universidad.compusearch.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.Categoria;
import com.universidad.compusearch.repository.CategoriaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Servicio de categorias
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    // Obtiene todoas los nombres de las categorias
    public List<String> obtenerTodasLosNombres() {
        log.debug("Buscando todas las etiquetas en la base de datos...");
        List<String> categorias = categoriaRepository.findAllNombres();
        log.info("Se encontraron {} etiquetas", categorias.size());
        return categorias;
    }

    // Obtiene todas las categorias
    public List<Categoria> obtenerTodos() {
        log.debug("Buscando todas las etiquetas en la base de datos...");
        List<Categoria> categorias = categoriaRepository.findAll();
        log.info("Se encontraron {} etiquetas", categorias.size());
        return categorias;
    }

    // Busca una categoria por nombre
    public Categoria buscarPorNombre(String nombre) {
        log.debug("Buscando etiqueta con nombre exacto: {}", nombre);
        Optional<Categoria> categoria = categoriaRepository.findByNombre(nombre);

        if (categoria.isEmpty()) {
            log.warn("No se encontró ninguna etiqueta con el nombre: {}", nombre);
            return null;
        } else {
            log.info("Etiqueta encontrada: {}", categoria.get().getNombre());
            return categoria.get();
        }
    }
}
