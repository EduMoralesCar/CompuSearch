package com.universidad.compusearch.service;

import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.Etiqueta;
import com.universidad.compusearch.repository.EtiquetaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

// Servicio de etiquetas
@Service
@RequiredArgsConstructor
@Slf4j
public class EtiquetaService {

    private final EtiquetaRepository etiquetaRepository;

    // Obtiene todas las etiquetas
    public List<Etiqueta> obtenerTodas() {
        log.debug("Buscando todas las etiquetas en la base de datos...");
        List<Etiqueta> etiquetas = etiquetaRepository.findAll();
        log.info("Se encontraron {} etiquetas", etiquetas.size());
        return etiquetas;
    }

    // Busca una etiqueta por nombre
    public Etiqueta buscarPorNombre(String nombre) {
        log.debug("Buscando etiqueta con nombre exacto: {}", nombre);
        Optional<Etiqueta> etiqueta = etiquetaRepository.findByNombre(nombre);

        if (etiqueta.isEmpty()) {
            log.warn("No se encontró ninguna etiqueta con el nombre: {}", nombre);
            return null;
        } else {
            log.info("Etiqueta encontrada: {}", etiqueta.get().getNombre());
            return etiqueta.get();
        }
    }
}
