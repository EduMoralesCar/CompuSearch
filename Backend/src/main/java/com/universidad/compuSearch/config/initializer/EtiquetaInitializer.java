package com.universidad.compusearch.config.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.universidad.compusearch.entity.Etiqueta;
import com.universidad.compusearch.repository.EtiquetaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EtiquetaInitializer {

    private static final Logger logger = LoggerFactory.getLogger(EtiquetaInitializer.class);
    private final EtiquetaRepository etiquetaRepository;

    public void init() {
        String[] etiquetas = {
            "Envío gratis", "Descuento", "PC Gamer", "Armado a medida",
            "Soporte técnico", "Componentes Premium", "Accesorios",
            "Workstation", "Reacondicionado", "Garantía extendida"
        };

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
        logger.info("Etiquetas: {} nuevas, {} existentes, total {}", nuevas, existentes, etiquetas.length);
    }
}
