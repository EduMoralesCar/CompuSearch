package com.universidad.compusearch.config.initializer;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.universidad.compusearch.entity.Producto;
import com.universidad.compusearch.entity.ProductoTienda;
import com.universidad.compusearch.entity.Tienda;
import com.universidad.compusearch.exception.ProductoException;
import com.universidad.compusearch.exception.UserException;
import com.universidad.compusearch.repository.ProductoRepository;
import com.universidad.compusearch.repository.ProductoTiendaRepository;
import com.universidad.compusearch.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Crear los prouductos de tiendas al inicio
@Component
@RequiredArgsConstructor
@Slf4j
public class ProductoTiendaInitializer {

        private final ProductoTiendaRepository productoTiendaRepository;
        private final UsuarioRepository usuarioRepository;
        private final ProductoRepository productoRepository;

        public void init() {
                crearProductoTienda("Tarjeta Gráfica ASUS TUF Gaming GeForce RTX 4070 12GB GDDR6X", "tienda1@test.com",
                                "1299.90", 10, "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp", "GPU-ASUS-4070");

                crearProductoTienda("Tarjeta Gráfica ASUS TUF Gaming GeForce RTX 4070 12GB GDDR6X", "tienda2@test.com",
                                "1499.90", 5, "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp", "GPU-ASUS-4070");

                crearProductoTienda("Placa Madre ASRock B450M-HDV R4.0", "tienda2@test.com", "89.90", 15,
                                "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp",
                                "MB-ASROCK-B450");

                crearProductoTienda("Tarjeta Gráfica MSI GeForce RTX 4080 GAMING X TRIO 16GB", "tienda3@test.com",
                                "1599.90", 8, "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp", "GPU-MSI-4080");
                crearProductoTienda("Tarjeta Gráfica GIGABYTE GeForce RTX 3060 GAMING OC 12G", "tienda1@test.com",
                                "499.90", 12, "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp",
                                "GPU-GIGABYTE-3060");

                crearProductoTienda("Tarjeta Gráfica ASUS Dual GeForce GTX 1650 OC 4GB", "tienda2@test.com", "189.90",
                                20, "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp", "GPU-ASUS-1650");

                crearProductoTienda("Placa Madre ASUS PRIME B550M-A WIFI II", "tienda3@test.com", "129.90", 10,
                                "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp", "MB-ASUS-B550");

                crearProductoTienda("Placa Madre MSI B450 TOMAHAWK MAX II", "tienda1@test.com", "149.90", 8,
                                "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp", "MB-MSI-B450");

                crearProductoTienda("Procesador AMD Ryzen 7 5800X 8 Núcleos 16 Hilos", "tienda2@test.com", "399.90", 12,
                                "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp",
                                "CPU-RYZEN-5800X");

                crearProductoTienda("Procesador Intel Core i7-12700K", "tienda3@test.com", "419.90", 10,
                                "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp", "CPU-I7-12700K");

                crearProductoTienda("Procesador AMD Ryzen 5 5600X", "tienda1@test.com", "249.90", 15,
                                "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp",
                                "CPU-RYZEN-5600X");

                crearProductoTienda("Procesador Intel Core i5-10400F", "tienda2@test.com", "189.90", 20,
                                "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp", "CPU-I5-10400F");

                crearProductoTienda("SSD Kingston NV2 1TB NVMe PCIe 4.0", "tienda3@test.com", "249.90", 35,
                                "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp", "SSD-NV2-001");

                crearProductoTienda("SSD WD Blue SN570 250GB NVMe PCIe 3.0", "tienda1@test.com", "49.90", 50,
                                "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp", "SSD-WD-SN570");

                crearProductoTienda("SSD Samsung 980 PRO 2TB NVMe PCIe 4.0", "tienda2@test.com", "499.90", 20,
                                "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp",
                                "SSD-SAMSUNG-980PRO");

                crearProductoTienda("SSD Crucial P3 500GB NVMe PCIe 3.0", "tienda3@test.com", "79.90", 30,
                                "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp",
                                "SSD-CRUCIAL-P3");

                crearProductoTienda("Fuente de Poder Corsair RM750x 750W 80+ Gold Full Modular", "tienda1@test.com",
                                "129.90", 15, "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp",
                                "PSU-CORSAIR-750X");

                crearProductoTienda("Fuente de Poder EVGA SuperNOVA 1000 G5 1000W 80+ Gold Full Modular",
                                "tienda2@test.com", "179.90", 10, "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp", "PSU-EVGA-1000");

                crearProductoTienda("Fuente de Poder Cooler Master MWE 650 Bronze V2 650W", "tienda3@test.com", "99.90",
                                20, "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp", "PSU-CM-650");

                crearProductoTienda("Fuente de Poder EVGA 500 W1 500W 80+ White", "tienda1@test.com", "59.90", 25,
                                "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp", "PSU-EVGA-500");

                crearProductoTienda("Cooler CPU Cooler Master Hyper 212 Black Edition", "tienda2@test.com", "49.90", 30,
                                "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp", "COOLER-H212");

                crearProductoTienda("Cooler CPU Cooler Master Hyper T20", "tienda3@test.com", "29.90", 40,
                                "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp", "COOLER-T20");

                crearProductoTienda("Enfriamiento Líquido NZXT Kraken X63", "tienda1@test.com", "149.90", 12,
                                "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp",
                                "COOLER-KRAKENX63");

                crearProductoTienda("Cooler CPU DeepCool GAMMAXX 400 V2", "tienda2@test.com", "39.90", 35,
                                "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp",
                                "COOLER-GAMMAXX400");

                crearProductoTienda("Memoria RAM Corsair Vengeance RGB Pro 16GB (2x8GB) 3600MHz DDR4",
                                "tienda3@test.com", "79.90", 20, "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp",
                                "RAM-CORSAIR-16GB");

                crearProductoTienda("Memoria RAM G.Skill Ripjaws V 32GB (2x16GB) 3600MHz DDR4", "tienda1@test.com",
                                "149.90", 15, "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp",
                                "RAM-GSKILL-32GB");

                crearProductoTienda("Memoria RAM ADATA XPG GAMMIX D30 16GB (2x8GB) 3200MHz DDR4", "tienda2@test.com",
                                "69.90", 25, "https://www.google.com/",
                                "https://www.impacto.com.pe/storage/products/md/173834431364422.webp",
                                "RAM-ADATA-16GB");
        }

        // Método genérico para crear una relación Producto - Tienda si no existe.
        private void crearProductoTienda(
                        String nombreProducto,
                        String emailTienda,
                        String precio,
                        int stock,
                        String urlProducto,
                        String urlImagen,
                        String idProductoApi) {
                Producto producto = productoRepository.findByNombre(nombreProducto)
                                .orElseThrow(() -> ProductoException.notFound());

                Tienda tienda = (Tienda) usuarioRepository.findByEmail(emailTienda)
                                .orElseThrow(() -> UserException.notFoundEmail());

                boolean existe = productoTiendaRepository
                                .findByProducto_IdProductoAndTienda_IdUsuario(producto.getIdProducto(),
                                                tienda.getIdUsuario())
                                .isPresent();

                if (!existe) {
                        ProductoTienda productoTienda = new ProductoTienda();
                        productoTienda.setProducto(producto);
                        productoTienda.setTienda(tienda);
                        productoTienda.setPrecio(new BigDecimal(precio));
                        productoTienda.setStock(stock);
                        productoTienda.setUrlProducto(urlProducto);
                        productoTienda.setUrlImagen(urlImagen);
                        productoTienda.setHabilitado(true);
                        productoTienda.setIdProductoApi(idProductoApi);

                        productoTiendaRepository.save(productoTienda);

                        log.info("ProductoTienda creado para '{}' en '{}'", producto.getNombre(),
                                        tienda.getNombre());
                } else {
                        log.debug("El producto '{}' ya está asociado a la tienda '{}'", producto.getNombre(),
                                        tienda.getNombre());
                }
        }
}
