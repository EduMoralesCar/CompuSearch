package com.universidad.compusearch.service;

import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.dto.TiendaResponse;
import com.universidad.compusearch.entity.Tienda;
import com.universidad.compusearch.repository.TiendaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TiendaService {

    private final TiendaRepository tiendaRepository;

    private static final Logger logger = LoggerFactory.getLogger(TiendaService.class);

    public List<Tienda> obtenerTiendasVerificadas() {
        logger.info("Buscando tiendas verificadas en la base de datos...");
        List<Tienda> tiendasVerificadas = tiendaRepository.findByVerificado(true);
        logger.info("Se encontraron {} tiendas verificadas.", tiendasVerificadas.size());
        return tiendasVerificadas;
    }

    public TiendaResponse mapToTienda(Tienda tienda) {
        logger.info("Mapeando entidad Tienda a DTO para: {}", tienda.getNombre());

        TiendaResponse tiendaResponse = new TiendaResponse(
                tienda.getNombre(),
                tienda.getDescripcion(),
                tienda.getTelefono(),
                tienda.getDireccion(),
                Base64.getEncoder().encodeToString(tienda.getLogo()),
                tienda.getUrlPagina(),
                tienda.getEtiquetas()
                );

        logger.debug("DTO generado: {}", tiendaResponse);
        return tiendaResponse;
    }
}
