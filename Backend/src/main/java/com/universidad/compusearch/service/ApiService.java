package com.universidad.compusearch.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.universidad.compusearch.dto.ProductoRequest;
import com.universidad.compusearch.entity.ProductoTienda;
import com.universidad.compusearch.entity.Tienda;
import com.universidad.compusearch.repository.ProductoTiendaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApiService {

    private final TiendaService tiendaService;
    private final RestTemplate restTemplate;
    private final ProductoService productoService;
    private final ProductoTiendaRepository productoTiendaRepository;

    @Scheduled(cron = "0 0 23 * * ?")
    public void ObtenerProductosDesdeApi() {
        log.info("Iniciando servicio de obtención de productos mediante api");

        List<Tienda> tiendas = tiendaService.obtenerTiendasVerificadas();

        for (Tienda tienda : tiendas) {
            if (!tienda.isVerificado() || tienda.getTiendaAPI() == null) {
                log.debug("La tienda {} con id {} no está verificada o no tiene API", tienda.getNombre(),
                        tienda.getIdUsuario());
                return;
            }

            procesarProductosDeTienda(tienda);
        }
    }

    public void obtenerProductosDesdeApiPorTienda(Long idTienda) {
        Tienda tienda = tiendaService.bucarPorId(idTienda);

        if (!tienda.isVerificado() || tienda.getTiendaAPI() == null) {
            log.warn("La tienda {} con id {} no está verificada o no tiene API",
                    tienda.getNombre(), tienda.getIdUsuario());
            return;
        }

        procesarProductosDeTienda(tienda);
    }

    private void procesarProductosDeTienda(Tienda tienda) {
        //String baseUrl = "http://localhost:8081/producto"; Usan sin docker
        String baseUrl = "http://host.docker.internal:8081/producto";
        String requestParam = tienda.getTiendaAPI().getUrlBase();

        try {
            log.info("Solicitando productos de la tienda {} a {}", tienda.getNombre(), baseUrl);

            String urlConParams = UriComponentsBuilder.fromUriString(baseUrl)
                        .queryParam("direccion", requestParam)
                        .build() 
                        .toUriString();

            ResponseEntity<List<ProductoRequest>> response = restTemplate.exchange(
                    urlConParams,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ProductoRequest>>() {
                    });

            List<ProductoRequest> productos = response.getBody();

            if (productos != null) {
                log.info("Tienda {} devolvió {} productos", tienda.getNombre(), productos.size());

                productoService.crearProductosDeApi(productos, tienda);
                eliminarProductosTiendaQueNoExisten(productos, tienda);
            }

        } catch (Exception e) {
            log.error("Error al obtener productos desde API de la tienda {}: {}", tienda.getNombre(), e.getMessage());
        }
    }

    public void eliminarProductosTiendaQueNoExisten(List<ProductoRequest> productosApi, Tienda tienda) {

        Set<Long> idsApi = productosApi.stream()
                .map(ProductoRequest::getId)
                .collect(Collectors.toSet());

        List<ProductoTienda> productosBD = productoTiendaRepository.findByTienda(tienda);

        for (ProductoTienda pt : productosBD) {
            if (!idsApi.contains(pt.getIdProductoApi())) {
                log.info("Eliminando productoTienda '{}' (ID API: {}) porque ya no existe en la API",
                        pt.getProducto().getNombre(), pt.getIdProductoApi());
                productoTiendaRepository.delete(pt);
            }
        }
    }
}
