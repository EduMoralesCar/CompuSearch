package com.universidad.compusearch.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.universidad.compusearch.dto.ProductoRequest;
import com.universidad.compusearch.entity.Categoria;
import com.universidad.compusearch.entity.Metrica;
import com.universidad.compusearch.entity.Producto;
import com.universidad.compusearch.entity.ProductoAtributo;
import com.universidad.compusearch.entity.ProductoTienda;
import com.universidad.compusearch.entity.Tienda;
import com.universidad.compusearch.repository.ProductoAtributoRepository;
import com.universidad.compusearch.repository.ProductoRepository;
import com.universidad.compusearch.repository.ProductoTiendaRepository;
import com.universidad.compusearch.util.ValidationText;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;
    private final ProductoAtributoService productoAtributoService;
    private final ProductoTiendaRepository productoTiendaRepository;
    private final ProductoAtributoRepository productoAtributoRepository;
    private final MetricaService metricaService;

    // Obtener productos por categorias
    public List<Producto> obtenerPorCategoria(String nombreCategoria) {
        log.info("Buscando productos para la categoría: {}", nombreCategoria);
        Categoria categoria = categoriaService.obtenerPorNombre(nombreCategoria);
        log.error("Categoría no encontrada: {}", nombreCategoria);

        List<Producto> productos = productoRepository.findByCategoria_IdCategoria(categoria.getIdCategoria());
        log.info("Se encontraron {} productos para la categoría '{}'", productos.size(), nombreCategoria);
        return productos;
    }

    // Obtener producto por id
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

    // Obtener producto por nombre
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

    // Verificar si existen productos en categorias por su id
    public boolean existeProductoEnCategoria(Long idCategoria) {
        log.info("Verificando si existen productos en la categoría con ID: {}", idCategoria);
        boolean existe = productoRepository.existsByCategoria_IdCategoria(idCategoria);
        if (existe) {
            log.warn("Existen productos asociados a la categoría con ID: {}", idCategoria);
        } else {
            log.info("No hay productos asociados a la categoría con ID: {}", idCategoria);
        }
        return existe;
    }

    @Transactional
    public void crearProductosDeApi(List<ProductoRequest> productos, Tienda tienda) {
        log.info("Creando productos a partir de la API de la tienda {}", tienda.getNombre());

        List<Producto> productosExistentes = productoRepository.findAll();

        for (ProductoRequest req : productos) {

            Producto productoEncontrado = productosExistentes.stream()
                    .filter(p -> p.getNombre().equalsIgnoreCase(req.getNombre())
                            || ValidationText.esNombreParecido(p.getNombre(), req.getNombre()))
                    .findFirst()
                    .orElse(null);

            Producto productoFinal;

            if (productoEncontrado != null) {
                log.info("Producto '{}' es igual o parecido a uno existente. NO se crea.", req.getNombre());
                productoFinal = productoEncontrado;

            } else {
                Producto nuevo = new Producto();
                nuevo.setNombre(req.getNombre());
                nuevo.setMarca(req.getMarca());
                nuevo.setModelo(req.getModelo());
                nuevo.setDescripcion(req.getDescripcion());

                Categoria cat = categoriaService.buscarPorNombre(req.getCategoria());
                if (cat == null)
                    cat = categoriaService.obtenerCategoriaNula();
                nuevo.setCategoria(cat);

                productoRepository.save(nuevo);

                List<ProductoAtributo> atributos = productoAtributoService.convertirEnPA(req.getAtributos(), nuevo);

                productoAtributoRepository.saveAll(atributos);

                nuevo.setAtributos(atributos);

                log.info("Producto '{}' creado correctamente.", req.getNombre());

                productosExistentes.add(nuevo);
                productoFinal = nuevo;
            }

            agregarProductoATienda(req, productoFinal, tienda);
        }
    }

    public void agregarProductoATienda(ProductoRequest pro, Producto producto, Tienda tienda) {

        ProductoTienda pt = productoTiendaRepository
                .findByIdProductoApiAndTienda_Nombre(pro.getId(), tienda.getNombre()).orElse(null);

        if (pt == null) {
            ProductoTienda ptNuevo = new ProductoTienda();
            ptNuevo.setPrecio(pro.getPrecio());
            ptNuevo.setStock(pro.getStock());
            ptNuevo.setUrlProducto(pro.getUrlProducto());
            ptNuevo.setUrlImagen(pro.getUrlImagen());
            ptNuevo.setIdProductoApi(pro.getId());
            ptNuevo.setProducto(producto);
            ptNuevo.setTienda(tienda);
            productoTiendaRepository.save(ptNuevo);
            return;
        }

        List<Metrica> metricas = pt.getMetricas();
        metricas.add(metricaService.crearNuevaMetrica(pt, LocalDateTime.now()));
        Categoria cat = categoriaService.buscarPorNombre(pro.getCategoria());

        Producto productoExistente = pt.getProducto();

        productoExistente.setCategoria(cat);
        productoExistente.setModelo(pro.getModelo());
        productoExistente.setMarca(pro.getMarca());
        productoExistente.setDescripcion(pro.getDescripcion());

        productoRepository.save(productoExistente);

        pt.setPrecio(pro.getPrecio());
        pt.setStock(pro.getStock());
        pt.setUrlProducto(pro.getUrlProducto());
        pt.setUrlImagen(pro.getUrlImagen());
        pt.setIdProductoApi(pro.getId());
        pt.setProducto(productoExistente);
        pt.setTienda(tienda);
        pt.setMetricas(metricas);
        productoTiendaRepository.save(pt);
    }

    public void eliminarProductosTiendaNoPresentesEnApi(List<ProductoRequest> productosApi, Tienda tienda) {
        log.info("Verificando productos que deben eliminarse para la tienda {}", tienda.getNombre());

        List<ProductoTienda> productosTiendaBD = productoTiendaRepository.findByTienda(tienda);

        Set<Long> idsApi = productosApi.stream()
                .map(ProductoRequest::getId)
                .collect(Collectors.toSet());

        List<ProductoTienda> paraEliminar = productosTiendaBD.stream()
                .filter(pt -> !idsApi.contains(pt.getIdProductoApi()))
                .collect(Collectors.toList());

        if (paraEliminar.isEmpty()) {
            log.info("No hay productos para eliminar en la tienda {}", tienda.getNombre());
            return;
        }

        paraEliminar.forEach(pt -> {
            log.info("Eliminando productoTienda id={} (API id={}) de la tienda {}",
                    pt.getIdProductoTienda(),
                    pt.getIdProductoApi(),
                    tienda.getNombre());
            productoTiendaRepository.delete(pt);
        });

        log.info("Eliminación completada. {} productos eliminados.", paraEliminar.size());
    }

}
