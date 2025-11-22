package com.universidad.compusearch.service;

import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Service;

import com.universidad.compusearch.dto.TiendaResponse;
import com.universidad.compusearch.entity.Tienda;
import com.universidad.compusearch.repository.TiendaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con las tiendas registradas.
 * <p>
 * Permite obtener tiendas verificadas y transformar las entidades {@link Tienda}
 * en objetos de transferencia de datos ({@link TiendaResponse}).
 * </p>
 *
 * <p>Funciones principales:</p>
 * <ul>
 *   <li>Listar tiendas verificadas.</li>
 *   <li>Convertir entidades {@code Tienda} a DTOs para ser enviados al cliente.</li>
 * </ul>
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TiendaService {

    private final TiendaRepository tiendaRepository;

    /**
     * Obtiene todas las tiendas que han sido verificadas.
     *
     * @return una lista de entidades {@link Tienda} con el atributo {@code verificado = true}.
     */
    public List<Tienda> obtenerTiendasVerificadas() {
        log.info("Buscando tiendas verificadas en la base de datos...");
        List<Tienda> tiendasVerificadas = tiendaRepository.findByVerificado(true);
        log.info("Se encontraron {} tiendas verificadas.", tiendasVerificadas.size());
        return tiendasVerificadas;
    }

    /**
     * Convierte una entidad {@link Tienda} en un objeto {@link TiendaResponse}.
     * <p>
     * La imagen del logo se transforma en una cadena Base64 para su transporte en JSON.
     * </p>
     *
     * @param tienda la entidad {@link Tienda} a transformar.
     * @return un objeto {@link TiendaResponse} con los datos de la tienda.
     */
    public TiendaResponse mapToTienda(Tienda tienda) {
        log.info("Mapeando entidad Tienda a DTO para: {}", tienda.getNombre());

        TiendaResponse tiendaResponse = new TiendaResponse(
                tienda.getNombre(),
                tienda.getDescripcion(),
                tienda.getTelefono(),
                tienda.getDireccion(),
                Base64.getEncoder().encodeToString(tienda.getLogo()),
                tienda.getUrlPagina(),
                tienda.getEtiquetas()
        );

        log.debug("DTO generado: {}", tiendaResponse);
        return tiendaResponse;
    }
}
