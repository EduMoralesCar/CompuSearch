package com.universidad.compusearch.config.initializer;

import org.springframework.stereotype.Component;

import com.universidad.compusearch.entity.Atributo;
import com.universidad.compusearch.repository.AtributoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Crear atributos al inicio
@Component
@RequiredArgsConstructor
@Slf4j
public class AtributoInitializer {

    private final AtributoRepository atributoRepository;

    public void init() {

        // Nombre de los atributos
        String[] nombresAtributos = {
                // CPU
                "Socket CPU", "Núcleos CPU", "Hilos CPU", "Frecuencia Base CPU",
                "Frecuencia Turbo CPU", "Caché L3 CPU", "Consumo CPU",

                // Motherboard
                "Socket Motherboard", "Chipset Motherboard", "Factor de Forma Motherboard",
                "Slots RAM", "RAM Máxima", "Tipo RAM Compatible", "Puertos M.2", "Puertos SATA",
                "Consumo Motherboard",

                // RAM
                "Tipo RAM", "Capacidad RAM", "Frecuencia RAM", "Latencia CAS RAM",
                "Perfil XMP RAM", "Número de Módulos RAM", "Consumo RAM",

                // Almacenamiento
                "Tipo de Almacenamiento", "Capacidad Almacenamiento",
                "Interfaz Almacenamiento",
                "Velocidad Lectura", "Velocidad Escritura", "Consumo Almacenamiento",

                // GPU
                "Interfaz PCIe GPU", "Conectores Energía GPU", "Factor de Forma GPU",
                "Memoria VRAM", "Tipo de VRAM", "Frecuencia Base GPU", "Frecuencia Boost GPU",
                "Salidas de Video GPU", "Consumo GPU", "Fabricante GPU",

                // Enfriamiento
                "Tipo de Enfriamiento", "Compatibilidad Socket Cooler",
                "Tamaño Ventilador", "Nivel de Ruido Cooler", "ARGB Cooler", "Consumo Cooler",

                // Fuente de Poder / PSU
                "Potencia PSU", "Certificación PSU", "Formato PSU", "Modularidad PSU", "Eficiencia PSU",
                "Consumo PSU"
        };

        // Insercion de los atributos
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
        log.info("Atributos: {} nuevos, {} existentes, total {}", nuevas, existentes, nombresAtributos.length);
    }
}
