package com.universidad.compusearch.service;

import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.Etiqueta;
import com.universidad.compusearch.exception.EtiquetaException;
import com.universidad.compusearch.repository.EtiquetaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de la gestión de etiquetas.
 * <p>
 * Proporciona métodos para obtener todas las etiquetas, buscar etiquetas
 * por nombre exacto, actualizar y eliminar etiquetas.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EtiquetaService {

    private final EtiquetaRepository etiquetaRepository;

    /**
     * Obtiene todas las etiquetas registradas en la base de datos.
     *
     * @return lista de todas las etiquetas encontradas; puede estar vacía si no hay
     *         registros.
     */
    public List<Etiqueta> obtenerTodas() {
        log.debug("Buscando todas las etiquetas en la base de datos...");
        List<Etiqueta> etiquetas = etiquetaRepository.findAll();
        log.info("Se encontraron {} etiquetas", etiquetas.size());
        return etiquetas;
    }

    /**
     * Busca una etiqueta por su nombre exacto.
     *
     * @param nombre el nombre de la etiqueta a buscar.
     * @return la etiqueta encontrada, o {@code null} si no existe ninguna con ese
     *         nombre.
     */
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

    /**
     * Actualiza una etiqueta existente por su ID.
     *
     * @param id    Identificador de la etiqueta a actualizar
     * @param nombreEtiqueta Objeto {@link Etiqueta} con los nuevos datos
     * @return la etiqueta actualizada
     * @throws EtiquetaException si la etiqueta con el ID dado no existe
     */
    public Etiqueta actualizar(Long id, String nombreEtiqueta) {
        log.debug("Actualizando etiqueta con id={}", id);
        Optional<Etiqueta> etiquetaExistente = etiquetaRepository.findById(id);

        if (etiquetaExistente.isEmpty()) {
            log.error("No se encontró ninguna etiqueta con id={}", id);
            throw EtiquetaException.notFound();
        }

        Etiqueta etiqueta = etiquetaExistente.get();
        etiqueta.setNombre(nombreEtiqueta);
        // Si hay más campos en Etiqueta, actualizarlos aquí
        etiquetaRepository.save(etiqueta);

        log.info("Etiqueta con id={} actualizada correctamente", id);
        return etiqueta;
    }

    /**
     * Elimina una etiqueta por su ID.
     *
     * <p>
     * Antes de eliminar, verifica si alguna tienda está usando la etiqueta.
     * Si está en uso, lanza una {@link EtiquetaException}.
     * </p>
     *
     * @param id Identificador de la etiqueta a eliminar
     * @throws EtiquetaException si la etiqueta no existe o está en uso por alguna
     *                           tienda
     */
    public void eliminar(Long id) {
        log.debug("Eliminando etiqueta con id={}", id);

        if (!etiquetaRepository.existsById(id)) {
            log.error("No se encontró ninguna etiqueta con id={}", id);
            throw EtiquetaException.notFound();
        }
        // Si pasa la validación, eliminar la etiqueta
        etiquetaRepository.deleteById(id);
        log.info("Etiqueta con id={} eliminada correctamente", id);
    }

    /**
     * Crea una nueva etiqueta.
     *
     * <p>
     * Verifica que no exista una etiqueta con el mismo nombre antes de crearla.
     * </p>
     *
     * @param nombreEtiqueta nombre de la etiqueta a crear
     * @return la etiqueta creada
     * @throws EtiquetaException si ya existe una etiqueta con el mismo nombre o el
     *                           nombre es inválido
     */
    public Etiqueta crear(String nombreEtiqueta) {
        log.debug("Creando nueva etiqueta con nombre={}", nombreEtiqueta);

        // Verificar si ya existe una etiqueta con ese nombre
        Optional<Etiqueta> existente = etiquetaRepository.findByNombre(nombreEtiqueta.trim());
        if (existente.isPresent()) {
            log.error("Ya existe una etiqueta con el nombre: {}", nombreEtiqueta);
            throw EtiquetaException.exist();
        }

        // Crear nueva etiqueta
        Etiqueta etiqueta = new Etiqueta();
        etiqueta.setNombre(nombreEtiqueta.trim());

        Etiqueta guardada = etiquetaRepository.save(etiqueta);

        log.info("Etiqueta creada correctamente con id={} y nombre={}", guardada.getIdEtiqueta(), guardada.getNombre());
        return guardada;
    }

}
