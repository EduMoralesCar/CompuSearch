package com.universidad.compusearch.config.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.universidad.compusearch.entity.Atributo;
import com.universidad.compusearch.entity.Categoria;
import com.universidad.compusearch.entity.Producto;
import com.universidad.compusearch.entity.ProductoAtributo;
import com.universidad.compusearch.repository.AtributoRepository;
import com.universidad.compusearch.repository.CategoriaRepository;
import com.universidad.compusearch.repository.ProductoAtributoRepository;
import com.universidad.compusearch.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductoInitializer {

        private static final Logger logger = LoggerFactory.getLogger(CategoriaInitializer.class);
        private final ProductoRepository productoRepository;
        private final CategoriaRepository categoriaRepository;
        private final AtributoRepository atributoRepository;
        private final ProductoAtributoRepository productoAtributoRepository;

        public void init() {
                String productoTarjetta = "Tarjeta Gráfica ASUS TUF Gaming GeForce RTX 4070 12GB GDDR6X";

                if (productoRepository.findByNombre(productoTarjetta).isEmpty()) {

                        Categoria categoriaGPU = categoriaRepository.findByNombre("Tarjeta de Video")
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Categoría 'Tarjeta de Video' no encontrada"));

                        Producto producto = new Producto();
                        producto.setNombre(productoTarjetta);
                        producto.setMarca("ASUS");
                        producto.setModelo("TUF Gaming GeForce RTX 4070 OC");
                        producto.setDescripcion(
                                        "Potente tarjeta gráfica con 12 GB de memoria GDDR6X, ideal para juegos en 1440p con alto rendimiento, refrigeración avanzada y diseño robusto de la línea TUF.");
                        producto.setCategoria(categoriaGPU);
                        productoRepository.save(producto);

                        crearProductoAtributo(producto, "Memoria VRAM", "12 GB");
                        crearProductoAtributo(producto, "Tipo de VRAM", "GDDR6X");
                        crearProductoAtributo(producto, "Interfaz de Memoria", "192-bit");
                        crearProductoAtributo(producto, "Frecuencia Base", "1920 MHz");
                        crearProductoAtributo(producto, "Frecuencia Boost", "2475 MHz");
                        crearProductoAtributo(producto, "Fabricante", "NVIDIA");
                        crearProductoAtributo(producto, "Salidas de Video", "HDMI 2.1 x1, DisplayPort 1.4a x3");
                        crearProductoAtributo(producto, "TDP", "200 W");

                        logger.info("Producto creado: {} con sus atributos.", productoTarjetta);
                } else {
                        logger.debug("El producto {} ya existe.", productoTarjetta);
                }

                String productoProcesador = "Procesador AMD Ryzen 7 5800X 8 Núcleos 16 Hilos";

                if (productoRepository.findByNombre(productoProcesador).isEmpty()) {

                        Categoria categoriaCPU = categoriaRepository.findByNombre("Procesador")
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Categoría 'Procesador' no encontrada"));

                        Producto producto = new Producto();
                        producto.setNombre(productoProcesador);
                        producto.setMarca("AMD");
                        producto.setModelo("Ryzen 7 5800X");
                        producto.setDescripcion(
                                        "Procesador de alto rendimiento con 8 núcleos y 16 hilos, ideal para gaming y productividad.");
                        producto.setCategoria(categoriaCPU);
                        productoRepository.save(producto);

                        crearProductoAtributo(producto, "Socket", "AM4");
                        crearProductoAtributo(producto, "Núcleos", "8");
                        crearProductoAtributo(producto, "Hilos", "16");
                        crearProductoAtributo(producto, "Frecuencia Base", "3.8 GHz");
                        crearProductoAtributo(producto, "Frecuencia Turbo", "4.7 GHz");
                        crearProductoAtributo(producto, "TDP", "105 W");
                        crearProductoAtributo(producto, "Caché L3", "32 MB");

                        logger.info("Procesador creado: {} con sus atributos.", productoProcesador);
                } else {
                        logger.debug("El procesador {} ya existe.", productoProcesador);
                }

                String productoAlmacenamiento = "SSD Kingston NV2 1TB NVMe PCIe 4.0";

                if (productoRepository.findByNombre(productoAlmacenamiento).isEmpty()) {

                        Categoria categoriaAlmacenamiento = categoriaRepository.findByNombre("Almacenamiento")
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Categoría 'Almacenamiento' no encontrada"));

                        Producto producto = new Producto();
                        producto.setNombre(productoAlmacenamiento);
                        producto.setMarca("Kingston");
                        producto.setModelo("NV2");
                        producto.setDescripcion("SSD NVMe PCIe 4.0 de 1TB con gran velocidad de lectura y escritura.");
                        producto.setCategoria(categoriaAlmacenamiento);
                        productoRepository.save(producto);

                        crearProductoAtributo(producto, "Tipo de Almacenamiento", "SSD NVMe");
                        crearProductoAtributo(producto, "Capacidad", "1 TB");
                        crearProductoAtributo(producto, "Interfaz", "PCIe 4.0 x4 NVMe");
                        crearProductoAtributo(producto, "Velocidad Lectura", "3500 MB/s");
                        crearProductoAtributo(producto, "Velocidad Escritura", "2100 MB/s");

                        logger.info("SSD creado: {} con sus atributos.", productoAlmacenamiento);
                } else {
                        logger.debug("El SSD {} ya existe.", productoAlmacenamiento);
                }

                String productoFuentePoder = "Fuente de Poder Corsair RM750x 750W 80+ Gold Full Modular";

                if (productoRepository.findByNombre(productoFuentePoder).isEmpty()) {
                        Categoria categoriaFuente = categoriaRepository.findByNombre("Fuente de Poder")
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Categoría 'Fuente de Poder' no encontrada"));

                        Producto producto = new Producto();
                        producto.setNombre(productoFuentePoder);
                        producto.setMarca("Corsair");
                        producto.setModelo("RM750x");
                        producto.setDescripcion(
                                        "Fuente de poder de 750W con certificación 80+ Gold, totalmente modular y silenciosa.");
                        producto.setCategoria(categoriaFuente);
                        productoRepository.save(producto);

                        crearProductoAtributo(producto, "Potencia", "750 W");
                        crearProductoAtributo(producto, "Certificación", "80+ Gold");
                        crearProductoAtributo(producto, "Formato", "ATX");
                        crearProductoAtributo(producto, "Modularidad", "Full Modular");

                        logger.info("Fuente de poder creada: {}", productoFuentePoder);
                } else {
                        logger.debug("La fuente de poder {} ya existe.", productoFuentePoder);
                }

                String productoRefrigeracion = "Cooler CPU Cooler Master Hyper 212 Black Edition";

                if (productoRepository.findByNombre(productoRefrigeracion).isEmpty()) {
                        Categoria categoriaRefrigeracion = categoriaRepository.findByNombre("Refrigeración CPU")
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Categoría 'Refrigeración CPU' no encontrada"));

                        Producto producto = new Producto();
                        producto.setNombre(productoRefrigeracion);
                        producto.setMarca("Cooler Master");
                        producto.setModelo("Hyper 212 Black Edition");
                        producto.setDescripcion(
                                        "Refrigeración por aire para CPU, diseño clásico con excelente rendimiento térmico y bajo ruido.");
                        producto.setCategoria(categoriaRefrigeracion);
                        productoRepository.save(producto);

                        crearProductoAtributo(producto, "Tipo de Enfriamiento", "Aire");
                        crearProductoAtributo(producto, "Compatibilidad Socket", "Intel LGA1700");
                        crearProductoAtributo(producto, "Compatibilidad Socket", "AM4");
                        crearProductoAtributo(producto, "Tamaño", "120 mm");
                        crearProductoAtributo(producto, "Nivel de Ruido", "26 dBA");

                        logger.info("Refrigeración CPU creada: {}", productoRefrigeracion);
                } else {
                        logger.debug("La refrigeración {} ya existe.", productoRefrigeracion);
                }

                String productoRAM = "Memoria RAM Corsair Vengeance RGB Pro 16GB (2x8GB) 3600MHz DDR4";

                if (productoRepository.findByNombre(productoRAM).isEmpty()) {
                        Categoria categoriaRAM = categoriaRepository.findByNombre("Memoria RAM")
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Categoría 'Memoria RAM' no encontrada"));

                        Producto producto = new Producto();
                        producto.setNombre(productoRAM);
                        producto.setMarca("Corsair");
                        producto.setModelo("Vengeance RGB Pro");
                        producto.setDescripcion(
                                        "Kit de memoria RAM DDR4 de 16 GB (2 x 8 GB) a 3600 MHz, con iluminación RGB y perfil XMP 2.0.");
                        producto.setCategoria(categoriaRAM);
                        productoRepository.save(producto);

                        crearProductoAtributo(producto, "Tipo de RAM", "DDR4");
                        crearProductoAtributo(producto, "Capacidad RAM", "16 GB (2x8)");
                        crearProductoAtributo(producto, "Frecuencia", "3600 MHz");
                        crearProductoAtributo(producto, "Latencia CAS", "18-22-22-42");
                        crearProductoAtributo(producto, "Perfil XMP", "Sí");

                        logger.info("Memoria RAM creada: {}", productoRAM);
                } else {
                        logger.debug("La memoria RAM {} ya existe.", productoRAM);
                }

                String productoPlacaMadre = "Placa Madre ASUS ROG STRIX B550-F GAMING Wi-Fi II";

                if (productoRepository.findByNombre(productoPlacaMadre).isEmpty()) {
                        Categoria categoriaPlaca = categoriaRepository.findByNombre("Placa Madre")
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Categoría 'Placa Madre' no encontrada"));

                        Producto producto = new Producto();
                        producto.setNombre(productoPlacaMadre);
                        producto.setMarca("ASUS");
                        producto.setModelo("ROG STRIX B550-F GAMING");
                        producto.setDescripcion(
                                        "Placa madre ATX con chipset B550, Wi-Fi integrado, soporte para memoria DDR4 y múltiples puertos M.2.");
                        producto.setCategoria(categoriaPlaca);
                        productoRepository.save(producto);

                        crearProductoAtributo(producto, "Socket de Motherboard", "AM4");
                        crearProductoAtributo(producto, "Chipset", "B550");
                        crearProductoAtributo(producto, "Factor de Forma", "ATX");
                        crearProductoAtributo(producto, "Slots RAM", "4");
                        crearProductoAtributo(producto, "RAM Máxima", "128 GB");
                        crearProductoAtributo(producto, "Puertos M.2", "2");
                        crearProductoAtributo(producto, "Puertos SATA", "6");
                        crearProductoAtributo(producto, "Wi-Fi", "Sí");
                        crearProductoAtributo(producto, "Compatibilidad de RAM", "DDR4");

                        logger.info("Placa madre creada: {}", productoPlacaMadre);
                } else {
                        logger.debug("La placa madre {} ya existe.", productoPlacaMadre);
                }
        }

        private void crearProductoAtributo(Producto producto,
                        String nombreAtributo,
                        String valor) {

                Atributo atributo = atributoRepository.findByNombre(nombreAtributo)
                                .orElseThrow(() -> new RuntimeException("Atributo no encontrado: " + nombreAtributo));

                ProductoAtributo pa = new ProductoAtributo();
                pa.setProducto(producto);
                pa.setAtributo(atributo);
                pa.setValor(valor);

                productoAtributoRepository.save(pa);
        }
}
