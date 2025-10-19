package com.universidad.compusearch.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.Etiqueta;
import com.universidad.compusearch.repository.EtiquetaRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EtiquetaService {

    private static final Logger logger = LoggerFactory.getLogger(EtiquetaService.class);

    private final EtiquetaRepository etiquetaRepository;

    public List<Etiqueta> obtenerTodas() {
        logger.debug("Buscando todas las etiquetas en la base de datos...");
        List<Etiqueta> etiquetas = etiquetaRepository.findAll();
        logger.info("Se encontraron {} etiquetas", etiquetas.size());
        return etiquetas;
    }

    public Etiqueta buscarPorNombre(String nombre) {
        logger.debug("Buscando etiqueta con nombre exacto: {}", nombre);
        Optional<Etiqueta> etiqueta = etiquetaRepository.findByNombre(nombre);

        if (etiqueta.isEmpty()) {
            logger.warn("No se encontró ninguna etiqueta con el nombre: {}", nombre);
            return null;
        } else {
            logger.info("Etiqueta encontrada: {}", etiqueta.get().getNombre());
            return etiqueta.get();
        }
    }
}
