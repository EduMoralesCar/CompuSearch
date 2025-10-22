package com.universidad.compusearch.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.Categoria;
import com.universidad.compusearch.entity.Producto;
import com.universidad.compusearch.repository.CategoriaRepository;
import com.universidad.compusearch.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Servicio de productos
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    // Obtiene una lista de productos por categoria
    public List<Producto> obtenerPorCategoria(String nombreCategoria) {
        log.info("Buscando productos para la categoría: {}", nombreCategoria);
        Categoria categoria = categoriaRepository.findByNombre(nombreCategoria)
                .orElseThrow(() -> {
                    log.error("Categoría no encontrada: {}", nombreCategoria);
                    return new RuntimeException("Categoría no encontrada: " + nombreCategoria);
                });

        List<Producto> productos = productoRepository.findByCategoria_IdCategoria(categoria.getIdCategoria());
        log.info("Se encontraron {} productos para la categoría '{}'", productos.size(), nombreCategoria);
        return productos;
    }

    // Obtiene un producto por id
    public Optional<Producto> obtenerPorId(Long idProducto) {
        log.info("Buscando producto con ID: {}", idProducto);
        Optional<Producto> productoOpt = productoRepository.findById(idProducto);
        if (productoOpt.isPresent()) {
            log.info("Producto encontrado: {}", productoOpt.get().getNombre());
        } else {
            log.warn("No se encontró producto con ID: {}", idProducto);
        }
        return productoOpt;
    }

    public Optional<Producto> obtenerPorNombre(String nombreProducto) {
        log.info("Buscando producto con nombre: {}", nombreProducto);
        Optional<Producto> productoOpt = productoRepository.findByNombre(nombreProducto);
        if (productoOpt.isPresent()) {
            log.info("Producto encontrado: {}", productoOpt.get().getNombre());
        } else {
            log.warn("No se encontró producto con nombre: {}", nombreProducto);
        }
        return productoOpt;
    }
}
