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
        log.info("Iniciando servicio de obtención de productos mediante API");

        List<Tienda> tiendas = tiendaService.obtenerTiendasVerificadas();
        log.info("Se encontraron {} tiendas verificadas", tiendas.size());

        for (Tienda tienda : tiendas) {
            if (!tienda.isVerificado() || tienda.getTiendaAPI() == null) {
                log.debug("Saltando tienda '{}' (ID: {}) porque no está verificada o no tiene API",
                        tienda.getNombre(), tienda.getIdUsuario());
                continue; // cambiar return a continue para procesar todas las tiendas
            }

            log.info("Procesando productos de la tienda '{}' (ID: {})", tienda.getNombre(), tienda.getIdUsuario());
            procesarProductosDeTienda(tienda);
        }

        log.info("Finalizado servicio de obtención de productos mediante API");
    }

    public void obtenerProductosDesdeApiPorTienda(Long idTienda) {
        log.info("Obteniendo productos desde API para la tienda con ID: {}", idTienda);
        Tienda tienda = tiendaService.bucarPorId(idTienda);

        if (!tienda.isVerificado() || tienda.getTiendaAPI() == null) {
            log.warn("La tienda '{}' (ID: {}) no está verificada o no tiene API",
                    tienda.getNombre(), tienda.getIdUsuario());
            return;
        }

        procesarProductosDeTienda(tienda);
        log.info("Finalizado proceso de obtención de productos para la tienda '{}' (ID: {})",
                tienda.getNombre(), tienda.getIdUsuario());
    }

    private void procesarProductosDeTienda(Tienda tienda) {
        String baseUrl = "http://host.docker.internal:8081/producto";
        String requestParam = tienda.getTiendaAPI().getUrlBase();

        try {
            log.info("Solicitando productos de la tienda '{}' a {}", tienda.getNombre(), baseUrl);

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
                log.info("La tienda '{}' devolvió {} productos", tienda.getNombre(), productos.size());

                productoService.crearProductosDeApi(productos, tienda);
                log.info("Productos de la tienda '{}' procesados correctamente", tienda.getNombre());

                eliminarProductosTiendaQueNoExisten(productos, tienda);
            } else {
                log.warn("La tienda '{}' devolvió una lista de productos vacía o nula", tienda.getNombre());
            }

        } catch (Exception e) {
            log.error("Error al obtener productos desde API de la tienda '{}': {}", tienda.getNombre(), e.getMessage(), e);
        }
    }

    public void eliminarProductosTiendaQueNoExisten(List<ProductoRequest> productosApi, Tienda tienda) {
        log.info("Verificando productos de la tienda '{}' que ya no existen en la API", tienda.getNombre());

        Set<Long> idsApi = productosApi.stream()
                .map(ProductoRequest::getId)
                .collect(Collectors.toSet());

        List<ProductoTienda> productosBD = productoTiendaRepository.findByTienda(tienda);
        int eliminados = 0;

        for (ProductoTienda pt : productosBD) {
            if (!idsApi.contains(pt.getIdProductoApi())) {
                log.info("Eliminando productoTienda '{}' (ID API: {}) porque ya no existe en la API",
                        pt.getProducto().getNombre(), pt.getIdProductoApi());
                productoTiendaRepository.delete(pt);
                eliminados++;
            }
        }

        log.info("Se eliminaron {} productos de la tienda '{}' que ya no existen en la API", eliminados, tienda.getNombre());
    }
}
