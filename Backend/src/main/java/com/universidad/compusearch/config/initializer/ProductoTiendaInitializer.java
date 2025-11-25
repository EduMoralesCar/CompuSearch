package com.universidad.compusearch.config.initializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.universidad.compusearch.entity.Metrica;
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
                                "1299.90", 10, "https://computershopperu.com/categoria/79-tarjeta-video-nvidia-geforce-rtx",
                                "https://www.impacto.com.pe/storage/products/md/168780945480168.jpg", 1);

                crearProductoTienda("Tarjeta Gráfica ASUS TUF Gaming GeForce RTX 4070 12GB GDDR6X", "tienda2@test.com",
                                "1499.90", 5, "https://www.impacto.com.pe/producto/tarjeta-de-video-asus-tuf-rtx-4070-12gb-gddr6x-gaming-geforce-nvidia-192-bits-grafico-para-videojuegos-3-ventiladores-90yv0iz1-m0aa00",
                                "https://www.impacto.com.pe/storage/products/md/168780945480168.jpg", 2);

                crearProductoTienda("Placa Madre ASRock B450M-HDV R4.0", "tienda2@test.com", "89.90", 15,
                                "https://www.impacto.com.pe/producto/placa-madre-asrock-b450m-hdv-r4-0-90-mxb9n0-a0uayz-socket-am4-ram-ddr4-buss-3200oc-mhz",
                                "https://www.impacto.com.pe/storage/products/md/173825526395089.webp",
                                11);
                
                crearProductoTienda("Tarjeta Gráfica MSI GeForce RTX 4080 GAMING X TRIO 16GB", "tienda3@test.com",
                                "1599.90", 8, "https://sercoplus.com/busqueda?controller=search&s=RTX+4080+",
                                "https://oechsle.vteximg.com.br/arquivos/ids/15619351-1000-1000/image-7949611554104e078f59994c522014d3.jpg?v=638279550374230000", 3);
                
                crearProductoTienda("Tarjeta Gráfica GIGABYTE GeForce RTX 3060 GAMING OC 12G", "tienda1@test.com",
                                "499.90", 12, "https://computershopperu.com/producto/tarjeta-video-nvidia-geforce-rtx/35587-tarjeta-de-video-msi-geforce-rtx-3060-12gb-gddr6-192bits-ventus-2x-oc-edition-pn912-v397-658.html",
                                "https://computershopperu.com/6389-medium_default/tarjeta-de-video-msi-geforce-rtx-3060-12gb-gddr6-192bits-ventus-2x-oc-edition-pn912-v397-658.jpg",
                                4);

                crearProductoTienda("Tarjeta Gráfica ASUS Dual GeForce GTX 1650 OC 4GB", "tienda2@test.com", "189.90",
                                20, "https://www.impacto.com.pe/producto/tarjeta-de-video-asus-gtx-1650-dual-4gb-gddr6-90y0ezd-m0aa00-geforce-nvidia-128-bits-2-ventiladores",
                                "https://www.impacto.com.pe/storage/products/md/173842755660307.jpg", 6);

                crearProductoTienda("Placa Madre ASUS PRIME B550M-A WIFI II", "tienda3@test.com", "370.00", 10,
                                "https://sercoplus.com/socket-amd-am4/12106-mainboard-asus-prime-b550-a-ac-am4-amd.html",
                                "https://sercoplus.com/68541-large_default/mainboard-asus-prime-b550-a-ac-am4-amd.jpg", 7);

                crearProductoTienda("Placa Madre MSI B450 TOMAHAWK MAX II", "tienda1@test.com", "149.90", 8,
                                "https://computershopperu.com/categoria/32-placas-madre?page=5",
                                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRtFgQSlk23fSsLPhRXD1vlP5_x_Ahe3oPxig&s", 9);

                crearProductoTienda("Procesador AMD Ryzen 7 5800X 8 Núcleos 16 Hilos", "tienda2@test.com", "399.90", 12,
                                "https://www.impacto.com.pe/producto/procesador-amd-ryzen-7-5800x-3-8ghz-hasta-4-7ghz-36mb-100-100000063wof-am4-8-nucleos-scooler",
                                "https://www.impacto.com.pe/storage/products/16076152142.jpg",
                                12);

                crearProductoTienda("Procesador Intel Core i7-12700K", "tienda3@test.com", "419.90", 10,
                                "https://sercoplus.com/cpu-1700-12va-generacion/13189-procesador-intel-core-i7-12700k.html",
                                "https://sercoplus.com/56450-large_default/procesador-intel-core-i7-12700k.jpg", 13);

                crearProductoTienda("Procesador AMD Ryzen 5 5600X", "tienda1@test.com", "249.90", 15,
                                "https://computershopperu.com/producto/amd-ryzen-series-5000/6313-procesador-amd-ryzen-5-5600x-370ghz-hasta-460ghz-35mb-6-core-am4-pn100-100000065box.html",
                                "https://computershopperu.com/4010-medium_default/procesador-amd-ryzen-5-5600x-370ghz-hasta-460ghz-35mb-6-core-am4-pn100-100000065box.jpg",
                                14);

                crearProductoTienda("Procesador Intel Core i5-10400F", "tienda2@test.com", "189.90", 20,
                                "https://www.impacto.com.pe/producto/procesador-intel-core-i5-10400f-2-9ghz-hasta-4-30-ghz-12mb-bx8070110400f-lga-1200-6-nucleos",
                                "https://www.impacto.com.pe/storage/products/md/173825982293231.webp", 15);

                crearProductoTienda("SSD Kingston NV2 1TB NVMe PCIe 4.0", "tienda3@test.com", "249.90", 35,
                                "https://sercoplus.com/570-ssd-m-2-pcie",
                                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTQ7xZo_o7KIao65S6RUmmvR37WzHYhRKvFOw&s", 16);

                crearProductoTienda("SSD WD Blue SN570 250GB NVMe PCIe 3.0", "tienda1@test.com", "49.90", 50,
                                "https://computershopperu.com/categoria/36-almacenamiento",
                                "https://cgs-computer.pe/public_html/public/img/productos/jCokfHiOE2efkP26F6fciVvmJRSPApg3bjtQSs3iVDqzysKccliwpxwDNblzU6aTrzCmLA4woY6auiOc.jpg", 17);

                crearProductoTienda("SSD Samsung 980 PRO 2TB NVMe PCIe 4.0", "tienda2@test.com", "499.90", 20,
                                "https://www.impacto.com.pe/catalogo?qsearch=SSD%20Samsung%20980%20PRO%202TB%20NVMe%20PCIe%204.0",
                                "https://www.impacto.com.pe/storage/products/md/17382785007183.webp",
                                18);

                crearProductoTienda("SSD Crucial P3 500GB NVMe PCIe 3.0", "tienda3@test.com", "79.90", 30,
                                "https://sercoplus.com/570-ssd-m-2-pcie",
                                "https://mercury.vtexassets.com/arquivos/ids/14642986/image-607980e266c14e92898ecc7b5913f845.jpg?v=638360940017830000",
                                20);

                crearProductoTienda("Fuente de Poder Corsair RM750x 750W 80+ Gold Full Modular", "tienda1@test.com",
                                "129.90", 15, "https://computershopperu.com/categoria/16-fuente-de-poder?order=product.price.asc&q=Marca-ASROCK-CORSAIR-COUGAR-MICRONICS-ANTRYX&page=2",
                                "https://www.impacto.com.pe/storage/products/lg/175640443480234.webp",
                                21);

                crearProductoTienda("Fuente de Poder EVGA SuperNOVA 1000 G5 1000W 80+ Gold Full Modular",
                                "tienda2@test.com", "179.90", 10, "https://www.impacto.com.pe/producto/fuente-de-poder-evga-1000w-g5-atx-nova-gold-80-plus-modular-color-negro-220-g5-1000-x1",
                                "https://www.impacto.com.pe/storage/products/md/16863493226204.jpg", 22);

                crearProductoTienda("Fuente de Poder Cooler Master MWE 650 Bronze V2 650W", "tienda3@test.com", "99.90",
                                20, "https://computershopperu.com/categoria/16-fuente-de-poder?order=product.price.desc&q=Marca-ANTEC-ASROCK-COOLER+MASTER-COUGAR-LIAN+LI/Disponibilidad-No+disponible&page=2",
                                "https://computershopperu.com/1760-home_default/fuente-de-poder-cooler-master-mwe-650-v2-650w-80-plus-bronze-.jpg", 23);

                crearProductoTienda("Fuente de Poder EVGA 500 W1 500W 80+ White", "tienda1@test.com", "59.90", 25,
                                "https://www.impacto.com.pe/producto/fuente-de-poder-evga-500w-atx-80-plus-white-no-modular-color-negro-100-w1-0500-kr",
                                "https://www.impacto.com.pe/storage/products/md/167450945014654.jpg", 44);

                crearProductoTienda("Cooler CPU Cooler Master Hyper 212 Black Edition", "tienda2@test.com", "49.90", 30,
                                "https://www.impacto.com.pe/producto/cooler-para-procesador-cooler-master-hyper-212-rgb-black-edition-rr-212s-20pc-r2-negro",
                                "https://www.impacto.com.pe/storage/products/md/174429937484693.webp", 45);

                crearProductoTienda("Cooler CPU Cooler Master Hyper T20", "tienda3@test.com", "29.90", 40,
                                "https://sercoplus.com/270-cooler-de-procesadores",
                                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSv_G112Tav_oZQUt9Vto5vUD8nklX9dGqnDg&s", 46);

                crearProductoTienda("Enfriamiento Líquido NZXT Kraken X63", "tienda1@test.com", "149.90", 12,
                                "https://computershopperu.com/categoria/17-refrigeracion-liquida",
                                "https://www.impacto.com.pe/storage/products/EH2COOHFDPLQWXQ1615568733RL-KRX63-01-1.jpg",
                                51);

                crearProductoTienda("Cooler CPU DeepCool GAMMAXX 400 V2", "tienda2@test.com", "39.90", 35,
                                "https://www.impacto.com.pe/producto/cooler-para-procesador-deep-cool-gammaxx-400-v2-155x129x77mm-rodamiento-hidraulico-1-ventilador-iluminacion-led-rojo-dp-mch4-gmx400v2-rd",
                                "https://www.impacto.com.pe/storage/products/1619114594DP-MCH4-GMX400V2-RD--1.jpg",
                                52);
// --------------------------------------------------------------------
                crearProductoTienda("Memoria RAM Corsair Vengeance RGB Pro 16GB (2x8GB) 3600MHz DDR4",
                                "tienda3@test.com", "79.90", 20, "https://sercoplus.com/55-memorias-ram",
                                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR-BmNYs4H1zTt0wWd4DuClGR8cj8RzJThynQ&s",
                                53);

                crearProductoTienda("Memoria RAM G.Skill Ripjaws V 32GB (2x16GB) 3600MHz DDR4", "tienda1@test.com",
                                "149.90", 15, "https://computershopperu.com/brand/52-gskill",
                                "https://www.impacto.com.pe/storage/products/lg/174239748099806.webp",
                                54);

                crearProductoTienda("Memoria RAM ADATA XPG GAMMIX D30 16GB (2x8GB) 3200MHz DDR4", "tienda2@test.com",
                                "69.90", 25, "https://www.impacto.com.pe/catalogo?qsearch=Memoria%20RAM%20ADATA%20XPG%20GAMMIX%20D30%2016GB%20(2x8GB)%203200MHz%20DDR4",
                                "https://m.media-amazon.com/images/I/71aH6ETrwlL.jpg",
                                55);
        }

        // Método genérico para crear una relación Producto - Tienda si no existe.
        private void crearProductoTienda(
                        String nombreProducto,
                        String emailTienda,
                        String precio,
                        int stock,
                        String urlProducto,
                        String urlImagen,
                        long idProductoApi) {
                Producto producto = productoRepository.findByNombre(nombreProducto)
                                .orElseThrow(() -> ProductoException.notFound());

                Tienda tienda = (Tienda) usuarioRepository.findByEmail(emailTienda)
                                .orElseThrow(() -> UserException.notFoundEmail(emailTienda));

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
                        productoTienda.getMetricas().add(generarMetrica(productoTienda));

                        productoTiendaRepository.save(productoTienda);

                        log.info("ProductoTienda creado para '{}' en '{}'", producto.getNombre(),
                                        tienda.getNombre());
                } else {
                        log.debug("El producto '{}' ya está asociado a la tienda '{}'", producto.getNombre(),
                                        tienda.getNombre());
                }
        }

        private Metrica generarMetrica(ProductoTienda producto){
                Metrica metrica = new Metrica();
                metrica.setProductoTienda(producto);
                metrica.setFecha(LocalDateTime.now());
                return metrica;
        }
}
