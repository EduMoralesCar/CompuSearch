package com.universidad.compusearch.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.universidad.compusearch.dto.CategoriaRequest;
import com.universidad.compusearch.entity.Categoria;
import com.universidad.compusearch.exception.CategoriaException;
import com.universidad.compusearch.repository.CategoriaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio encargado de la gestión de {@link Categoria} en el sistema.
 * <p>
 * Permite consultar todas las categorías, buscar por nombre, actualizar y
 * eliminar categorías,
 * y obtener listas de nombres de categorías.
 * </p>
 * 
 * <p>
 * Cada categoría representa una clasificación de productos o builds.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoService productoService;

    /**
     * Obtiene una lista con los nombres de todas las categorías.
     *
     * @return Lista de nombres de categorías
     */
    public List<String> obtenerTodasLosNombres() {
        log.debug("Buscando todas las categorías en la base de datos...");
        List<String> categorias = categoriaRepository.findAllNombres();
        log.info("Se encontraron {} categorías", categorias.size());
        return categorias;
    }

    /**
     * Obtiene todas las categorías existentes en la base de datos.
     *
     * @return Lista de objetos {@link Categoria}
     */
    public List<Categoria> obtenerTodos() {
        log.debug("Buscando todas las categorías en la base de datos...");
        List<Categoria> categorias = categoriaRepository.findAll();
        log.info("Se encontraron {} categorías", categorias.size());
        return categorias;
    }

    /**
     * Busca una categoría por su nombre exacto.
     *
     * @param nombre Nombre de la categoría a buscar
     * @return La categoría encontrada, o {@code null} si no existe
     */
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

    /**
     * Actualiza una categoría existente según su ID.
     *
     * @param id        Identificador de la categoría a actualizar
     * @param categoria Datos de la categoría para actualizar
     * @return La categoría actualizada
     * @throws CategoriaException si la categoría con el ID dado no existe
     */
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

    /**
     * Elimina una categoría existente según su ID.
     *
     * <p>
     * Antes de eliminar, verifica si existen productos asociados a la categoría.
     * Si existen, se lanza una {@link CategoriaException} indicando que la
     * categoría está en uso.
     * </p>
     *
     * @param idCategoria Identificador de la categoría a eliminar
     * @throws CategoriaException si la categoría no existe o está en uso
     */
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

    /**
     * Crea una nueva categoría en el sistema.
     *
     * <p>
     * Antes de crear, verifica que no exista otra categoría con el mismo nombre.
     * En caso de existir, lanza una {@link CategoriaException}.
     * </p>
     *
     * @param datosCategoria Objeto {@link CategoriaRequest} con los datos de la
     *                       nueva categoría
     * @return La categoría creada y persistida en la base de datos
     * @throws CategoriaException si ya existe una categoría con el mismo nombre
     */
    public Categoria crear(CategoriaRequest datosCategoria) {
        log.debug("Intentando crear una nueva categoría con nombre={}", datosCategoria.getNombre());

        // Verificar si ya existe alguna categoría con el mismo nombre (o similar)
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

}
