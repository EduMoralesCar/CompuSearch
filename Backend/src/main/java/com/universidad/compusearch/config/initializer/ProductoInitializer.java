package com.universidad.compusearch.config.initializer;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.universidad.compusearch.entity.Atributo;
import com.universidad.compusearch.entity.Categoria;
import com.universidad.compusearch.entity.Producto;
import com.universidad.compusearch.entity.ProductoAtributo;
import com.universidad.compusearch.exception.AtributoException;
import com.universidad.compusearch.exception.CategoriaException;
import com.universidad.compusearch.repository.AtributoRepository;
import com.universidad.compusearch.repository.CategoriaRepository;
import com.universidad.compusearch.repository.ProductoAtributoRepository;
import com.universidad.compusearch.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Crear productos al inicio
@Component
@RequiredArgsConstructor
@Slf4j
public class ProductoInitializer {

        private final ProductoRepository productoRepository;
        private final CategoriaRepository categoriaRepository;
        private final AtributoRepository atributoRepository;
        private final ProductoAtributoRepository productoAtributoRepository;

        public void init() {

                // TARJETAS DE VIDEO
                crearProductoConAtributos(
                                "Tarjeta Gráfica ASUS TUF Gaming GeForce RTX 4070 12GB GDDR6X",
                                "Tarjeta de Video",
                                "ASUS",
                                "TUF Gaming GeForce RTX 4070 OC",
                                "Potente tarjeta gráfica con 12 GB de memoria GDDR6X, ideal para juegos en 1440p con alto rendimiento, refrigeración avanzada y diseño robusto de la línea TUF.",
                                Map.of(
                                                "Interfaz PCIe GPU", "PCIe 4.0 x16",
                                                "Conectores Energía GPU", "2 x 8 pines",
                                                "Factor de Forma GPU", "2.5 Slots",
                                                "Memoria VRAM", "12 GB",
                                                "Tipo de VRAM", "GDDR6X",
                                                "Frecuencia Base GPU", "1920 MHz",
                                                "Frecuencia Boost GPU", "2475 MHz",
                                                "Salidas de Video GPU", "HDMI 2.1 x1, DisplayPort 1.4a x3",
                                                "Consumo GPU", "200 W",
                                                "Fabricante GPU", "NVIDIA"));

                crearProductoConAtributos(
                                "Placa Madre ASRock B450M-HDV R4.0",
                                "Placa Madre",
                                "ASRock",
                                "B450M-HDV R4.0",
                                "Placa madre micro ATX económica compatible con procesadores AMD AM4.",
                                Map.of(
                                                "Socket Motherboard", "AM4",
                                                "Chipset Motherboard", "B450",
                                                "Factor de Forma Motherboard", "Micro-ATX",
                                                "Slots RAM", "2",
                                                "RAM Máxima", "64 GB",
                                                "Tipo RAM Compatible", "DDR4",
                                                "Puertos M.2", "1",
                                                "Puertos SATA", "4",
                                                "Consumo Motherboard", "35 W"));

                crearProductoConAtributos(
                                "Tarjeta Gráfica MSI GeForce RTX 4080 GAMING X TRIO 16GB",
                                "Tarjeta de Video",
                                "MSI",
                                "GAMING X TRIO 16GB",
                                "Tarjeta gráfica de gama alta con 16 GB GDDR6X para resoluciones 4K y ray tracing.",
                                Map.of(
                                                "Interfaz PCIe GPU", "PCIe 4.0 x16",
                                                "Conectores Energía GPU", "1 x 16 pines",
                                                "Factor de Forma GPU", "3 Slots",
                                                "Memoria VRAM", "16 GB",
                                                "Tipo de VRAM", "GDDR6X",
                                                "Frecuencia Base GPU", "2205 MHz",
                                                "Frecuencia Boost GPU", "2505 MHz",
                                                "Salidas de Video GPU", "HDMI 2.1 x1, DisplayPort 1.4a x3",
                                                "Consumo GPU", "320 W",
                                                "Fabricante GPU", "NVIDIA"));

                crearProductoConAtributos(
                                "Tarjeta Gráfica GIGABYTE GeForce RTX 3060 GAMING OC 12G",
                                "Tarjeta de Video",
                                "GIGABYTE",
                                "GAMING OC 12G",
                                "Tarjeta gráfica de gama media con excelente rendimiento en 1080p y 1440p.",
                                Map.of(
                                                "Interfaz PCIe GPU", "PCIe 4.0 x16",
                                                "Conectores Energía GPU", "1 x 8 pines",
                                                "Factor de Forma GPU", "2 Slots",
                                                "Memoria VRAM", "12 GB",
                                                "Tipo de VRAM", "GDDR6",
                                                "Frecuencia Base GPU", "1320 MHz",
                                                "Frecuencia Boost GPU", "1837 MHz",
                                                "Salidas de Video GPU", "HDMI 2.1 x2, DisplayPort 1.4a x2",
                                                "Consumo GPU", "170 W",
                                                "Fabricante GPU", "NVIDIA"));

                crearProductoConAtributos(
                                "Tarjeta Gráfica ASUS Dual GeForce GTX 1650 OC 4GB",
                                "Tarjeta de Video",
                                "ASUS",
                                "Dual GTX 1650 OC",
                                "Tarjeta gráfica de entrada ideal para gaming en 1080p con bajo consumo energético.",
                                Map.of(
                                                "Interfaz PCIe GPU", "PCIe 3.0 x16",
                                                "Conectores Energía GPU", "1 x 6 pines",
                                                "Factor de Forma GPU", "2 Slots",
                                                "Memoria VRAM", "4 GB",
                                                "Tipo de VRAM", "GDDR5",
                                                "Frecuencia Base GPU", "1485 MHz",
                                                "Frecuencia Boost GPU", "1665 MHz",
                                                "Salidas de Video GPU", "HDMI 2.0b x1, DisplayPort 1.4 x1, DVI-D x1",
                                                "Consumo GPU", "75 W",
                                                "Fabricante GPU", "NVIDIA"));

                // PLACA MADRE
                crearProductoConAtributos(
                                "Placa Madre ASUS PRIME B550M-A WIFI II",
                                "Placa Madre",
                                "ASUS",
                                "PRIME B550M-A WIFI II",
                                "Placa madre Micro-ATX con chipset B550, Wi-Fi integrado y soporte para procesadores AMD Ryzen.",
                                Map.of(
                                                "Socket Motherboard", "AM4",
                                                "Chipset Motherboard", "B550",
                                                "Factor de Forma Motherboard", "Micro-ATX",
                                                "Slots RAM", "4",
                                                "RAM Máxima", "128 GB",
                                                "Tipo RAM Compatible", "DDR4",
                                                "Puertos M.2", "2",
                                                "Puertos SATA", "4",
                                                "Consumo Motherboard", "50 W"));

                crearProductoConAtributos(
                                "Placa Madre MSI B450 TOMAHAWK MAX II",
                                "Placa Madre",
                                "MSI",
                                "B450 TOMAHAWK MAX II",
                                "Placa madre ATX confiable para plataformas AM4, ideal para builds de gama media.",
                                Map.of(
                                                "Socket Motherboard", "AM4",
                                                "Chipset Motherboard", "B450",
                                                "Factor de Forma Motherboard", "ATX",
                                                "Slots RAM", "4",
                                                "RAM Máxima", "128 GB",
                                                "Tipo RAM Compatible", "DDR4",
                                                "Puertos M.2", "1",
                                                "Puertos SATA", "6",
                                                "Consumo Motherboard", "45 W"));

                // PROCESADOR
                crearProductoConAtributos(
                                "Procesador AMD Ryzen 7 5800X 8 Núcleos 16 Hilos",
                                "Procesador",
                                "AMD",
                                "Ryzen 7 5800X",
                                "Procesador de alto rendimiento con 8 núcleos y 16 hilos...",
                                Map.of(
                                                "Socket CPU", "AM4",
                                                "Núcleos CPU", "8",
                                                "Hilos CPU", "16",
                                                "Frecuencia Base CPU", "3.8 GHz",
                                                "Frecuencia Turbo CPU", "4.7 GHz",
                                                "Caché L3 CPU", "32 MB",
                                                "Consumo CPU", "105 W"));

                crearProductoConAtributos(
                                "Procesador Intel Core i7-12700K",
                                "Procesador",
                                "Intel",
                                "Core i7-12700K",
                                "Procesador de 12 núcleos (8P+4E) de 12ª generación con alto rendimiento para gaming y productividad.",
                                Map.of(
                                                "Socket CPU", "LGA1700",
                                                "Núcleos CPU", "12",
                                                "Hilos CPU", "20",
                                                "Frecuencia Base CPU", "3.6 GHz",
                                                "Frecuencia Turbo CPU", "5.0 GHz",
                                                "Caché L3 CPU", "25 MB",
                                                "Consumo CPU", "125 W"));

                crearProductoConAtributos(
                                "Procesador AMD Ryzen 5 5600X",
                                "Procesador",
                                "AMD",
                                "Ryzen 5 5600X",
                                "Procesador de 6 núcleos y 12 hilos con gran eficiencia y rendimiento en juegos.",
                                Map.of(
                                                "Socket CPU", "AM4",
                                                "Núcleos CPU", "6",
                                                "Hilos CPU", "12",
                                                "Frecuencia Base CPU", "3.7 GHz",
                                                "Frecuencia Turbo CPU", "4.6 GHz",
                                                "Caché L3 CPU", "32 MB",
                                                "Consumo CPU", "65 W"));

                crearProductoConAtributos(
                                "Procesador Intel Core i5-10400F",
                                "Procesador",
                                "Intel",
                                "Core i5-10400F",
                                "Procesador de 6 núcleos y 12 hilos con gran rendimiento y buena relación calidad-precio.",
                                Map.of(
                                                "Socket CPU", "LGA1200",
                                                "Núcleos CPU", "6",
                                                "Hilos CPU", "12",
                                                "Frecuencia Base CPU", "2.9 GHz",
                                                "Frecuencia Turbo CPU", "4.3 GHz",
                                                "Caché L3 CPU", "12 MB",
                                                "Consumo CPU", "65 W"));

                // ALMACENAMIENTO
                crearProductoConAtributos(
                                "SSD Kingston NV2 1TB NVMe PCIe 4.0",
                                "Almacenamiento",
                                "Kingston",
                                "NV2",
                                "SSD NVMe PCIe 4.0 de 1TB...",
                                Map.of(
                                                "Tipo de Almacenamiento", "SSD NVMe",
                                                "Capacidad Almacenamiento", "1 TB",
                                                "Interfaz Almacenamiento", "PCIe 4.0 x4 NVMe",
                                                "Velocidad Lectura", "3500 MB/s",
                                                "Velocidad Escritura", "2100 MB/s",
                                                "Consumo Almacenamiento", "4.5 W"));

                crearProductoConAtributos(
                                "SSD WD Blue SN570 250GB NVMe PCIe 3.0",
                                "Almacenamiento",
                                "Western Digital",
                                "Blue SN570",
                                "SSD NVMe de 250 GB ideal para builds económicas y rápidas.",
                                Map.of(
                                                "Tipo de Almacenamiento", "SSD NVMe",
                                                "Capacidad Almacenamiento", "250 GB",
                                                "Interfaz Almacenamiento", "PCIe 3.0 x4 NVMe",
                                                "Velocidad Lectura", "3300 MB/s",
                                                "Velocidad Escritura", "1200 MB/s",
                                                "Consumo Almacenamiento", "3 W"));

                crearProductoConAtributos(
                                "SSD Samsung 980 PRO 2TB NVMe PCIe 4.0",
                                "Almacenamiento",
                                "Samsung",
                                "980 PRO",
                                "SSD de alto rendimiento con velocidades extremas para gaming y productividad.",
                                Map.of(
                                                "Tipo de Almacenamiento", "SSD NVMe",
                                                "Capacidad Almacenamiento", "2 TB",
                                                "Interfaz Almacenamiento", "PCIe 4.0 x4 NVMe",
                                                "Velocidad Lectura", "7000 MB/s",
                                                "Velocidad Escritura", "5100 MB/s",
                                                "Consumo Almacenamiento", "8 W"));

                crearProductoConAtributos(
                                "SSD Crucial P3 500GB NVMe PCIe 3.0",
                                "Almacenamiento",
                                "Crucial",
                                "P3",
                                "SSD NVMe PCIe 3.0 económico con buen rendimiento para gaming y uso general.",
                                Map.of(
                                                "Tipo de Almacenamiento", "SSD NVMe",
                                                "Capacidad Almacenamiento", "500 GB",
                                                "Interfaz Almacenamiento", "PCIe 3.0 x4 NVMe",
                                                "Velocidad Lectura", "3500 MB/s",
                                                "Velocidad Escritura", "1900 MB/s",
                                                "Consumo Almacenamiento", "3.5 W"));

                // FUENTE DE PODER
                crearProductoConAtributos(
                                "Fuente de Poder Corsair RM750x 750W 80+ Gold Full Modular",
                                "Fuente de Poder",
                                "Corsair",
                                "RM750x",
                                "Fuente de poder de 750W con certificación 80+ Gold, totalmente modular y silenciosa.",
                                Map.of(
                                                "Potencia PSU", "750 W",
                                                "Certificación PSU", "80+ Gold",
                                                "Formato PSU", "ATX",
                                                "Modularidad PSU", "Full Modular",
                                                "Eficiencia PSU", "90%",
                                                "Consumo PSU", "750 W"));

                crearProductoConAtributos(
                                "Fuente de Poder EVGA SuperNOVA 1000 G5 1000W 80+ Gold Full Modular",
                                "Fuente de Poder",
                                "EVGA",
                                "SuperNOVA 1000 G5",
                                "Fuente de poder de 1000 W con certificación 80+ Gold, totalmente modular y eficiente.",
                                Map.of(
                                                "Potencia PSU", "1000 W",
                                                "Certificación PSU", "80+ Gold",
                                                "Formato PSU", "ATX",
                                                "Modularidad PSU", "Full Modular",
                                                "Eficiencia PSU", "90%",
                                                "Consumo PSU", "1000 W"));

                crearProductoConAtributos(
                                "Fuente de Poder Cooler Master MWE 650 Bronze V2 650W",
                                "Fuente de Poder",
                                "Cooler Master",
                                "MWE 650 Bronze V2",
                                "Fuente de poder eficiente y silenciosa, ideal para builds de gama media.",
                                Map.of(
                                                "Potencia PSU", "650 W",
                                                "Certificación PSU", "80+ Bronze",
                                                "Formato PSU", "ATX",
                                                "Modularidad PSU", "No Modular",
                                                "Eficiencia PSU", "85%",
                                                "Consumo PSU", "650 W"));

                crearProductoConAtributos(
                                "Fuente de Poder EVGA 500 W1 500W 80+ White",
                                "Fuente de Poder",
                                "EVGA",
                                "500 W1",
                                "Fuente de poder básica con certificación 80+ White, ideal para builds de entrada.",
                                Map.of(
                                                "Potencia PSU", "500 W",
                                                "Certificación PSU", "80+ White",
                                                "Formato PSU", "ATX",
                                                "Modularidad PSU", "No Modular",
                                                "Eficiencia PSU", "80%",
                                                "Consumo PSU", "500 W"));

                // REGRIGERACION
                crearProductoConAtributos(
                                "Cooler CPU Cooler Master Hyper 212 Black Edition",
                                "Refrigeración CPU",
                                "Cooler Master",
                                "Hyper 212 Black Edition",
                                "Refrigeración por aire para CPU, diseño clásico con excelente rendimiento térmico y bajo ruido.",
                                Map.of(
                                                "Tipo de Enfriamiento", "Aire",
                                                "Compatibilidad Socket Cooler", "Intel LGA1700, AM4",
                                                "Tamaño Ventilador", "120 mm",
                                                "Nivel de Ruido Cooler", "26 dBA",
                                                "ARGB Cooler", "No",
                                                "Consumo Cooler", "5 W"));

                crearProductoConAtributos(
                                "Cooler CPU Cooler Master Hyper T20",
                                "Refrigeración CPU",
                                "Cooler Master",
                                "Hyper T20",
                                "Refrigeración por aire económica y compacta, ideal para procesadores de gama baja.",
                                Map.of(
                                                "Tipo de Enfriamiento", "Aire",
                                                "Compatibilidad Socket Cooler", "Intel LGA115x, AM4",
                                                "Tamaño Ventilador", "92 mm",
                                                "Nivel de Ruido Cooler", "30 dBA",
                                                "ARGB Cooler", "No",
                                                "Consumo Cooler", "3 W"));

                crearProductoConAtributos(
                                "Enfriamiento Líquido NZXT Kraken X63",
                                "Refrigeración CPU",
                                "NZXT",
                                "Kraken X63",
                                "Sistema de refrigeración líquida AIO con radiador de 280 mm y ventiladores RGB.",
                                Map.of(
                                                "Tipo de Enfriamiento", "Líquido",
                                                "Compatibilidad Socket Cooler", "Intel LGA1700, AM4, AM5",
                                                "Tamaño Ventilador", "140 mm",
                                                "Nivel de Ruido Cooler", "21 dBA",
                                                "ARGB Cooler", "Sí",
                                                "Consumo Cooler", "12 W"));

                crearProductoConAtributos(
                                "Cooler CPU DeepCool GAMMAXX 400 V2",
                                "Refrigeración CPU",
                                "DeepCool",
                                "GAMMAXX 400 V2",
                                "Enfriamiento por aire eficiente y económico compatible con múltiples sockets.",
                                Map.of(
                                                "Tipo de Enfriamiento", "Aire",
                                                "Compatibilidad Socket Cooler", "Intel LGA1700, AM4, AM5",
                                                "Tamaño Ventilador", "120 mm",
                                                "Nivel de Ruido Cooler", "27 dBA",
                                                "ARGB Cooler", "Sí",
                                                "Consumo Cooler", "4.5 W"));

                // MEMORIA
                crearProductoConAtributos(
                                "Memoria RAM Corsair Vengeance RGB Pro 16GB (2x8GB) 3600MHz DDR4",
                                "Memoria RAM",
                                "Corsair",
                                "Vengeance RGB Pro",
                                "Kit de memoria RAM DDR4 de 16 GB (2 x 8 GB) a 3600 MHz, con iluminación RGB y perfil XMP 2.0.",
                                Map.of(
                                                "Tipo RAM", "DDR4",
                                                "Capacidad RAM", "16 GB (2x8)",
                                                "Frecuencia RAM", "3600 MHz",
                                                "Latencia CAS RAM", "18-22-22-42",
                                                "Perfil XMP RAM", "Sí",
                                                "Número de Módulos RAM", "2",
                                                "Consumo RAM", "10 W"));

                crearProductoConAtributos(
                                "Memoria RAM G.Skill Ripjaws V 32GB (2x16GB) 3600MHz DDR4",
                                "Memoria RAM",
                                "G.Skill",
                                "Ripjaws V",
                                "Kit de RAM DDR4 de 32 GB (2 x 16 GB) a 3600 MHz, con bajo perfil y gran estabilidad.",
                                Map.of(
                                                "Tipo RAM", "DDR4",
                                                "Capacidad RAM", "32 GB (2x16)",
                                                "Frecuencia RAM", "3600 MHz",
                                                "Latencia CAS RAM", "18-22-22-42",
                                                "Perfil XMP RAM", "Sí",
                                                "Número de Módulos RAM", "2",
                                                "Consumo RAM", "15 W"));

                crearProductoConAtributos(
                                "Memoria RAM ADATA XPG GAMMIX D30 16GB (2x8GB) 3200MHz DDR4",
                                "Memoria RAM",
                                "ADATA",
                                "GAMMIX D30",
                                "Kit de RAM DDR4 de 16 GB (2 x 8 GB) a 3200 MHz, confiable y económica.",
                                Map.of(
                                                "Tipo RAM", "DDR4",
                                                "Capacidad RAM", "16 GB (2x8)",
                                                "Frecuencia RAM", "3200 MHz",
                                                "Latencia CAS RAM", "16-20-20",
                                                "Perfil XMP RAM", "Sí",
                                                "Número de Módulos RAM", "2",
                                                "Consumo RAM", "8 W"));

        }

        // Metodo genérico para crear productos con atributos.
        private void crearProductoConAtributos(
                        String nombreProducto,
                        String nombreCategoria,
                        String marca,
                        String modelo,
                        String descripcion,
                        Map<String, String> atributos) {
                if (productoRepository.findByNombre(nombreProducto).isPresent()) {
                        log.debug("El producto '{}' ya existe, se omite creación.", nombreProducto);
                        return;
                }

                // Buscar categoría
                Categoria categoria = categoriaRepository.findByNombre(nombreCategoria)
                                .orElseThrow(() -> CategoriaException.notFound());

                // Crear y guardar producto
                Producto producto = new Producto();
                producto.setNombre(nombreProducto);
                producto.setMarca(marca);
                producto.setModelo(modelo);
                producto.setDescripcion(descripcion);
                producto.setCategoria(categoria);
                productoRepository.save(producto);

                // Crear atributos asociados
                atributos.forEach((nombreAttr, valor) -> crearProductoAtributo(producto, nombreAttr, valor));

                log.info("Producto '{}' creado con {} atributos.", nombreProducto, atributos.size());
        }

        private void crearProductoAtributo(Producto producto, String nombreAtributo, String valor) {
                if (StringUtils.isBlank(nombreAtributo)) {
                        log.warn("Nombre de atributo vacío para producto '{}', se ignora.", producto.getNombre());
                        return;
                }

                Atributo atributo = atributoRepository.findByNombre(nombreAtributo)
                                .orElseThrow(() -> AtributoException.notFound());

                ProductoAtributo pa = new ProductoAtributo();
                pa.setProducto(producto);
                pa.setAtributo(atributo);
                pa.setValor(valor);

                productoAtributoRepository.save(pa);
        }
}
