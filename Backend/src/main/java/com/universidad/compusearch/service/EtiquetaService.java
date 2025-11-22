package com.universidad.compusearch.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.Etiqueta;
import com.universidad.compusearch.exception.EtiquetaException;
import com.universidad.compusearch.repository.EtiquetaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EtiquetaService {

    private final EtiquetaRepository etiquetaRepository;

    // Obtener todas las etiquetas
    public List<Etiqueta> obtenerTodas() {
        log.debug("Buscando todas las etiquetas en la base de datos...");
        List<Etiqueta> etiquetas = etiquetaRepository.findAll();
        log.info("Se encontraron {} etiquetas", etiquetas.size());
        return etiquetas;
    }

    // Obtener todas las etiquetas apginadas
    public Page<Etiqueta> obtenerTodasPaginadas(Pageable pageable) {
        return etiquetaRepository.findAll(pageable); 
    }

    // Buscar por nombre
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

    // Actualizar etiqueta
    public Etiqueta actualizar(Long id, String nombreEtiqueta) {
        log.debug("Actualizando etiqueta con id={}", id);
        Optional<Etiqueta> etiquetaExistente = etiquetaRepository.findById(id);

        if (etiquetaExistente.isEmpty()) {
            log.error("No se encontró ninguna etiqueta con id={}", id);
            throw EtiquetaException.notFound();
        }

        Etiqueta etiqueta = etiquetaExistente.get();
        etiqueta.setNombre(nombreEtiqueta);
        etiquetaRepository.save(etiqueta);

        log.info("Etiqueta con id={} actualizada correctamente", id);
        return etiqueta;
    }

    // Eliminar etiqueta
    public void eliminar(Long id) {
        log.debug("Eliminando etiqueta con id={}", id);

        if (!etiquetaRepository.existsById(id)) {
            log.error("No se encontró ninguna etiqueta con id={}", id);
            throw EtiquetaException.notFound();
        }
        etiquetaRepository.deleteById(id);
        log.info("Etiqueta con id={} eliminada correctamente", id);
    }

    // Crear etiqueta
    public Etiqueta crear(String nombreEtiqueta) {
        log.debug("Creando nueva etiqueta con nombre={}", nombreEtiqueta);

        Optional<Etiqueta> existente = etiquetaRepository.findByNombre(nombreEtiqueta.trim());
        if (existente.isPresent()) {
            log.error("Ya existe una etiqueta con el nombre: {}", nombreEtiqueta);
            throw EtiquetaException.exist();
        }

        Etiqueta etiqueta = new Etiqueta();
        etiqueta.setNombre(nombreEtiqueta.trim());

        Etiqueta guardada = etiquetaRepository.save(etiqueta);

        log.info("Etiqueta creada correctamente con id={} y nombre={}", guardada.getIdEtiqueta(), guardada.getNombre());
        return guardada;
    }

}
