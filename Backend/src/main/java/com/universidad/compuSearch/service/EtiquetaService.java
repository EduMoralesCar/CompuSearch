package com.universidad.compusearch.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.Etiqueta;
import com.universidad.compusearch.repository.EtiquetaRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

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

    public List<Etiqueta> buscarPorNombre(String nombre) {
        logger.debug("Buscando etiquetas con nombre exacto: {}", nombre);
        List<Etiqueta> etiquetas = etiquetaRepository.findByNombre(nombre);
        if (etiquetas.isEmpty()) {
            logger.warn("No se encontraron etiquetas con el nombre: {}", nombre);
        } else {
            logger.info("Se encontraron {} etiquetas con el nombre: {}", etiquetas.size(), nombre);
        }
        return etiquetas;
    }
}

