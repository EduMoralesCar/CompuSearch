package com.universidad.compusearch.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.dto.CategoriaRequest;
import com.universidad.compusearch.entity.Categoria;
import com.universidad.compusearch.exception.CategoriaException;
import com.universidad.compusearch.repository.CategoriaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoService productoService;

    // Obtener todos los nombres de las categorias
    public List<String> obtenerTodasLosNombres() {
        log.debug("Buscando todas las categorías en la base de datos...");
        List<String> categorias = categoriaRepository.findAllNombres();
        log.info("Se encontraron {} categorías", categorias.size());
        return categorias;
    }

    // Obtener todas las categorias
    public List<Categoria> obtenerTodos() {
        log.debug("Buscando todas las categorías en la base de datos...");
        List<Categoria> categorias = categoriaRepository.findAll();
        log.info("Se encontraron {} categorías", categorias.size());
        return categorias;
    }

    // Buscar categoria por nombre
    public Categoria buscarPorNombre(String nombre) {
        log.debug("Buscando categoría con nombre exacto: {}", nombre);
        Optional<Categoria> categoria = categoriaRepository.findByNombre(nombre);

        if (categoria.isEmpty()) {
            log.warn("No se encontró ninguna categoría con el nombre: {}", nombre);
            return null;
        } else {
            log.info("Categoría encontrada: {}", categoria.get().getNombre());
            return categoria.get();
        }
    }

    // Actualizar categoria
    public Categoria actualizar(Long id, CategoriaRequest categoria) {
        log.debug("Intentando actualizar categoría con id={}", id);

        Categoria existente = categoriaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontró categoría con id={}", id);
                    return CategoriaException.notFound();
                });

        existente.setNombre(categoria.getNombre());
        existente.setDescripcion(categoria.getDescripcion());
        existente.setNombreImagen(categoria.getNombreImagen());

        Categoria actualizada = categoriaRepository.save(existente);
        log.info("Categoría con id={} actualizada correctamente", id);
        return actualizada;
    }

    // Eliminar categoria
    public void eliminar(Long idCategoria) {
        log.debug("Intentando eliminar categoría con id={}", idCategoria);

        if (!categoriaRepository.existsById(idCategoria)) {
            log.warn("No se encontró categoría con id={}", idCategoria);
            throw CategoriaException.notFound();
        }

        if (productoService.existeProductoEnCategoria(idCategoria)) {
            throw CategoriaException.inUse();
        }

        categoriaRepository.deleteById(idCategoria);
        log.info("Categoría con id={} eliminada correctamente", idCategoria);
    }

    // Crear categoria
    public Categoria crear(CategoriaRequest datosCategoria) {
        log.debug("Intentando crear una nueva categoría con nombre={}", datosCategoria.getNombre());

        Categoria existente = categoriaRepository.findByNombre(datosCategoria.getNombre())
                .orElse(null);

        if (existente != null) {
            log.warn("Ya existe una categoría con nombre={}", datosCategoria.getNombre());
            throw CategoriaException.exist();
        }

        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre(datosCategoria.getNombre());
        nuevaCategoria.setDescripcion(datosCategoria.getDescripcion());
        nuevaCategoria.setNombreImagen(datosCategoria.getNombreImagen());

        categoriaRepository.save(nuevaCategoria);
        log.info("Categoría creada correctamente con id={}", nuevaCategoria.getIdCategoria());
        return nuevaCategoria;
    }

    // Obtener las categorias paginadas
    public Page<Categoria> obtenerTodosPaginados(Pageable pageable) {
        return categoriaRepository.findAll(pageable); 
    }
}
