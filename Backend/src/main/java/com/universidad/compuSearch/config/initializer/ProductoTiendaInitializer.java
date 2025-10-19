package com.universidad.compusearch.config.initializer;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.universidad.compusearch.entity.Producto;
import com.universidad.compusearch.entity.ProductoTienda;
import com.universidad.compusearch.entity.Tienda;
import com.universidad.compusearch.repository.ProductoRepository;
import com.universidad.compusearch.repository.ProductoTiendaRepository;
import com.universidad.compusearch.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductoTiendaInitializer {

        private static final Logger logger = LoggerFactory.getLogger(CategoriaInitializer.class);
        private final ProductoTiendaRepository productoTiendaRepository;
        private final UsuarioRepository usuarioRepository;
        private final ProductoRepository productoRepository;

        public void init() {
                Producto producto1 = productoRepository
                                .findByNombre("Tarjeta Gráfica ASUS TUF Gaming GeForce RTX 4070 12GB GDDR6X")
                                .orElseThrow(() -> new RuntimeException("Producto RTX 4070 no encontrado"));

                Tienda tienda1 = (Tienda) usuarioRepository.findByEmail("tienda1@test.com")
                                .orElseThrow(() -> new RuntimeException("Tienda no encontrada"));

                if (productoTiendaRepository.findByProductoAndTienda(producto1, tienda1).isEmpty()) {
                        ProductoTienda productoTienda = new ProductoTienda();
                        productoTienda.setProducto(producto1);
                        productoTienda.setTienda(tienda1);
                        productoTienda.setPrecio(new BigDecimal("2899.90"));
                        productoTienda.setStock(15);
                        productoTienda.setUrlProducto(
                                        "https://computershopperu.com/producto/tarjeta-video-nvidia-geforce-rtx/35561-tarjeta-de-video-asus-geforce-rtx-5060-8gb-gddr7-128bits-tuf-gaming-oc-pn90yv0n00-m0aa00.html");
                        productoTienda.setUrlImagen(
                                        "https://computershopperu.com/6275-large_default/tarjeta-de-video-asus-geforce-rtx-5060-8gb-gddr7-128bits-tuf-gaming-oc-pn90yv0n00-m0aa00.jpg");
                        productoTienda.setHabilitado(true);
                        productoTienda.setIdProductoApi("RTX4070-TUF-001");

                        productoTiendaRepository.save(productoTienda);

                        logger.info("ProductoTienda creado para {} en {}", producto1.getNombre(), tienda1.getNombre());
                } else {
                        logger.debug("El producto {} ya está asociado a la tienda {}", producto1.getNombre(),
                                        tienda1.getNombre());
                }

                Producto producto2 = productoRepository.findByNombre("Procesador AMD Ryzen 7 5800X 8 Núcleos 16 Hilos")
                                .orElseThrow(() -> new RuntimeException("Procesador no encontrado"));

                Tienda tienda2 = (Tienda) usuarioRepository.findByEmail("tienda2@test.com")
                                .orElseThrow(() -> new RuntimeException("Tienda no encontrada"));

                if (productoTiendaRepository.findByProductoAndTienda(producto2, tienda2).isEmpty()) {
                        ProductoTienda productoTienda = new ProductoTienda();
                        productoTienda.setProducto(producto2);
                        productoTienda.setTienda(tienda2);
                        productoTienda.setPrecio(new BigDecimal("1199.90"));
                        productoTienda.setStock(20);
                        productoTienda.setUrlProducto(
                                        "https://www.impacto.com.pe/producto/procesador-amd-ryzen-threadripper-2950x-3-5ghz-hasta-4-4ghz-32mb-yd295xa8afw0f-socket-tr4-16-nucleos");
                        productoTienda.setUrlImagen(
                                        "https://www.impacto.com.pe/storage/products/md/173834431364422.webp");
                        productoTienda.setHabilitado(true);
                        productoTienda.setIdProductoApi("CPU-5800X-001");
                        productoTiendaRepository.save(productoTienda);

                        logger.info("ProductoTienda creado para {} en {}", producto2.getNombre(), tienda2.getNombre());
                }

                Producto producto3 = productoRepository.findByNombre("SSD Kingston NV2 1TB NVMe PCIe 4.0")
                                .orElseThrow(() -> new RuntimeException("SSD no encontrado"));

                Tienda tienda3 = (Tienda) usuarioRepository.findByEmail("tienda3@test.com")
                                .orElseThrow(() -> new RuntimeException("Tienda no encontrada"));

                if (productoTiendaRepository.findByProductoAndTienda(producto3, tienda3).isEmpty()) {
                        ProductoTienda productoTienda = new ProductoTienda();
                        productoTienda.setProducto(producto3);
                        productoTienda.setTienda(tienda3);
                        productoTienda.setPrecio(new BigDecimal("249.90"));
                        productoTienda.setStock(35);
                        productoTienda.setUrlProducto(
                                        "https://sercoplus.com/ssd-m-2-pcie/642781-ssd-kingston-nv3-1tb-m-2-2230-nvme-p.html");
                        productoTienda.setUrlImagen(
                                        "https://sercoplus.com/70455-side_product_default/ssd-kingston-nv3-1tb-m-2-2230-nvme-p.jpg");
                        productoTienda.setHabilitado(true);
                        productoTienda.setIdProductoApi("SSD-NV2-001");
                        productoTiendaRepository.save(productoTienda);

                        logger.info("ProductoTienda creado para {} en {}", producto3.getNombre(), tienda3.getNombre());
                }

                Producto producto4 = productoRepository
                                .findByNombre("Fuente de Poder Corsair RM750x 750W 80+ Gold Full Modular")
                                .orElseThrow(() -> new RuntimeException("Fuente de poder no encontrada"));

                if (productoTiendaRepository.findByProductoAndTienda(producto4, tienda1).isEmpty()) {
                        ProductoTienda productoTienda = new ProductoTienda();
                        productoTienda.setProducto(producto4);
                        productoTienda.setTienda(tienda1);
                        productoTienda.setPrecio(new BigDecimal("499.90"));
                        productoTienda.setStock(25);
                        productoTienda.setUrlProducto(
                                        "https://www.corsair.com/us/en/p/psu/cp-9020199-na/rmx-series-rm750x-750-watt-80-plus-gold-fully-modular-atx-psu");
                        productoTienda.setUrlImagen(
                                        "https://www.corsair.com/corsairmedia/sys_master/productcontent/CP-9020199-NA-RM750x-PSU-01.png");
                        productoTienda.setHabilitado(true);
                        productoTienda.setIdProductoApi("PSU-RM750X-001");
                        productoTiendaRepository.save(productoTienda);

                        logger.info("ProductoTienda creado para {} en {}", producto4.getNombre(),
                                        tienda1.getNombre());
                }

                Producto producto5 = productoRepository
                                .findByNombre("Cooler CPU Cooler Master Hyper 212 Black Edition")
                                .orElseThrow(() -> new RuntimeException("Refrigeración CPU no encontrada"));

                if (productoTiendaRepository.findByProductoAndTienda(producto5, tienda2).isEmpty()) {
                        ProductoTienda productoTienda = new ProductoTienda();
                        productoTienda.setProducto(producto5);
                        productoTienda.setTienda(tienda2);
                        productoTienda.setPrecio(new BigDecimal("199.90"));
                        productoTienda.setStock(18);
                        productoTienda.setUrlProducto(
                                        "https://www.coolermaster.com/catalog/coolers/cpu-air-coolers/hyper-212-black-edition/");
                        productoTienda.setUrlImagen(
                                        "https://static.coolermaster.com/media/2826/hyper-212-black-edition-1.jpg");
                        productoTienda.setHabilitado(true);
                        productoTienda.setIdProductoApi("COOLER-212BE-001");
                        productoTiendaRepository.save(productoTienda);

                        logger.info("ProductoTienda creado para {} en {}", producto5.getNombre(), tienda2.getNombre());
                }

                Producto producto6 = productoRepository
                                .findByNombre("Memoria RAM Corsair Vengeance RGB Pro 16GB (2x8GB) 3600MHz DDR4")
                                .orElseThrow(() -> new RuntimeException("Memoria RAM no encontrada"));

                if (productoTiendaRepository.findByProductoAndTienda(producto6, tienda3).isEmpty()) {
                        ProductoTienda productoTienda = new ProductoTienda();
                        productoTienda.setProducto(producto6);
                        productoTienda.setTienda(tienda3);
                        productoTienda.setPrecio(new BigDecimal("299.90"));
                        productoTienda.setStock(40);
                        productoTienda.setUrlProducto(
                                        "https://www.corsair.com/us/en/p/memory/cmk16gx4m2b3200c16/vengeance-lpx-16gb-2-x-8gb-ddr4-dram-3200mhz-c16-memory-kit-black");
                        productoTienda.setUrlImagen(
                                        "https://www.corsair.com/corsairmedia/sys_master/productcontent/Corsair-Vengeance-LPX-DDR4-Black.png");
                        productoTienda.setHabilitado(true);
                        productoTienda.setIdProductoApi("RAM-VLPX16GB-001");
                        productoTiendaRepository.save(productoTienda);

                        logger.info("ProductoTienda creado para {} en {}", producto6.getNombre(), tienda3.getNombre());
                }

                Producto producto7 = productoRepository
                                .findByNombre("Placa Madre ASUS ROG STRIX B550-F GAMING Wi-Fi II")
                                .orElseThrow(() -> new RuntimeException("Placa madre no encontrada"));

                if (productoTiendaRepository.findByProductoAndTienda(producto7, tienda1).isEmpty()) {
                        ProductoTienda productoTienda = new ProductoTienda();
                        productoTienda.setProducto(producto7);
                        productoTienda.setTienda(tienda1);
                        productoTienda.setPrecio(new BigDecimal("899.90"));
                        productoTienda.setStock(12);
                        productoTienda.setUrlProducto(
                                        "https://rog.asus.com/motherboards/rog-strix/rog-strix-b550-f-gaming-wi-fi-ii-model/");
                        productoTienda.setUrlImagen(
                                        "https://dlcdnwebimgs.asus.com/gain/7e22e6e3-46b4-4a5d-bb3a-fdb357fb9eb0/");
                        productoTienda.setHabilitado(true);
                        productoTienda.setIdProductoApi("MB-B550F-001");
                        productoTiendaRepository.save(productoTienda);

                        logger.info("ProductoTienda creado para {} en {}", producto7.getNombre(), tienda1.getNombre());
                }
        }
}
