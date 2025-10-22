package com.universidad.compusearch.config.initializer;

import org.springframework.stereotype.Component;

import com.universidad.compusearch.entity.Etiqueta;
import com.universidad.compusearch.repository.EtiquetaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Crear etiquetas al inicio
@Component
@RequiredArgsConstructor
@Slf4j
public class EtiquetaInitializer {

    private final EtiquetaRepository etiquetaRepository;

    public void init() {

        // Nombre de los etiquetas
        String[] etiquetas = {
                "Envío gratis", "Descuento", "PC Gamer",
                "Armado a medida", "Soporte técnico", "Componentes Premium",
                "Accesorios", "Workstation", "Reacondicionado",
                "Garantía extendida"
        };

        // Insercion de los etiquetas
        int nuevas = 0, existentes = 0;
        for (String nombre : etiquetas) {
            if (etiquetaRepository.findByNombre(nombre).isEmpty()) {
                Etiqueta etiqueta = new Etiqueta();
                etiqueta.setNombre(nombre);
                etiquetaRepository.save(etiqueta);
                nuevas++;
            } else {
                existentes++;
            }
        }
        log.info("Etiquetas: {} nuevas, {} existentes, total {}", nuevas, existentes, etiquetas.length);
    }
}
