package com.universidad.compusearch.config.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.universidad.compusearch.entity.Atributo;
import com.universidad.compusearch.repository.AtributoRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AtributoInitializer {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaInitializer.class);
    private final AtributoRepository atributoRepository;

    public void init() {
        String[] nombresAtributos = {
                "Socket", "Socket de Motherboard", "TDP", "Fabricante",
                "Núcleos", "Hilos", "Frecuencia Base", "Frecuencia Turbo", "Caché L3",
                "Tipo de Enfriamiento", "Tamaño", "Compatibilidad Socket", "Nivel de Ruido", "ARGB",
                "Tipo de Almacenamiento", "Capacidad", "Capacidad RAM", "Interfaz", "Velocidad Lectura", "Velocidad Escritura",
                "Tipo de RAM", "Compatibilidad de RAM", "Capacidad", "Frecuencia", "Latencia CAS", "Perfil XMP",
                "Chipset", "Factor de Forma", "Slots RAM", "RAM Máxima", "Puertos M.2", "Puertos SATA", "Wi-Fi",
                "Memoria VRAM", "Tipo de VRAM", "Interfaz de Memoria", "Frecuencia Base", "Frecuencia Boost",
                "Salidas de Video", "Tipo de RAM",
                "Potencia", "Certificación", "Formato", "Modularidad"
        };

        int nuevas = 0, existentes = 0;
        for (String nombre : nombresAtributos) {
            if (atributoRepository.findByNombre(nombre).isEmpty()) {
                Atributo nuevo = new Atributo();
                nuevo.setNombre(nombre);
                atributoRepository.save(nuevo);
                nuevas++;
            } else {
                existentes++;
            }
        }
        logger.info("Atributos: {} nuevos, {} existentes, total {}", nuevas, existentes, nombresAtributos.length);
    }
}

