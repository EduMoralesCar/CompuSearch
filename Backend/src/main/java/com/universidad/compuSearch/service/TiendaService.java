package com.universidad.compusearch.service;

import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Service;

import com.universidad.compusearch.dto.TiendaResponse;
import com.universidad.compusearch.entity.Tienda;
import com.universidad.compusearch.repository.TiendaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Servicio de tienda
@Service
@RequiredArgsConstructor
@Slf4j
public class TiendaService {

    private final TiendaRepository tiendaRepository;

    // Obtener todas las tiendas verificadas
    public List<Tienda> obtenerTiendasVerificadas() {
        log.info("Buscando tiendas verificadas en la base de datos...");
        List<Tienda> tiendasVerificadas = tiendaRepository.findByVerificado(true);
        log.info("Se encontraron {} tiendas verificadas.", tiendasVerificadas.size());
        return tiendasVerificadas;
    }

    // Mapear las tiendas a un DTO
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
