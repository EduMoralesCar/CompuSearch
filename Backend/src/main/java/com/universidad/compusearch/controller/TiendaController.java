package com.universidad.compusearch.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.universidad.compusearch.dto.EstadoResponse;
import com.universidad.compusearch.dto.MessageResponse;
import com.universidad.compusearch.dto.TiendaDetallesResponse;
import com.universidad.compusearch.dto.TiendaFormRequest;
import com.universidad.compusearch.dto.TiendaInfoDetalleResponse;
import com.universidad.compusearch.dto.TiendaInfoResponse;
import com.universidad.compusearch.dto.TiendaResponse;
import com.universidad.compusearch.dto.VerificacionResponse;
import com.universidad.compusearch.entity.EstadoAPI;
import com.universidad.compusearch.entity.TiendaAPI;
import com.universidad.compusearch.service.TiendaService;
import com.universidad.compusearch.util.Mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/tiendas")
@RequiredArgsConstructor
@Slf4j
public class TiendaController {

    private final TiendaService tiendaService;

    @GetMapping("/verificadas")
    public ResponseEntity<List<TiendaResponse>> obtenerTiendasVerificadas() {
        log.info("GET /tiendas/verificadas - Solicitando tiendas verificadas");

        List<TiendaResponse> tiendasResponse = tiendaService.obtenerTiendasVerificadas()
                .stream()
                .map(Mapper::mapToTienda)
                .toList();

        log.info("Se retornaron {} tiendas verificadas", tiendasResponse.size());
        return ResponseEntity.ok(tiendasResponse);
    }

    @GetMapping
    public ResponseEntity<Page<TiendaInfoResponse>> getAllTiendas(
            @RequestParam(required = false) String nombre,
            Pageable pageable) {

        log.info("GET /tiendas - Consultando tiendas con filtro nombre='{}' y pageable={}", nombre, pageable);

        Page<TiendaInfoResponse> tiendas = tiendaService.findAllTiendas(pageable, nombre);

        log.info("Consulta completada. Total elementos: {}, Total páginas: {}",
                tiendas.getTotalElements(), tiendas.getTotalPages());

        return ResponseEntity.ok(tiendas);
    }

    @GetMapping("/empleado/{idUsuario}")
    public ResponseEntity<TiendaDetallesResponse> getTiendaByIdForEmpleado(@PathVariable Long idUsuario) {
        log.info("GET /tiendas/{} - Solicitando tienda por ID de usuario", idUsuario);

        TiendaDetallesResponse tienda = tiendaService.findTiendaByIdForEmpleado(idUsuario);
        log.info("Tienda encontrada para usuario {}", idUsuario);
        return ResponseEntity.ok(tienda);
    }

    @GetMapping("/tienda/{idUsuario}")
    public ResponseEntity<TiendaInfoDetalleResponse> getTiendaByIdForTienda(@PathVariable Long idUsuario) {
        log.info("GET /tiendas/{} - Solicitando tienda por ID de usuario", idUsuario);

        TiendaInfoDetalleResponse tienda = tiendaService.findTiendaByIdForTienda(idUsuario);
        log.info("Tienda encontrada para usuario {}", idUsuario);
        return ResponseEntity.ok(tienda);
    }

    @PutMapping("/{idUsuario}/estado")
    public ResponseEntity<TiendaDetallesResponse> actualizarEstado(
            @PathVariable Long idUsuario,
            @RequestBody EstadoResponse estadoDTO) {

        log.info("PUT /tiendas/{}/estado - Actualizando estado a {}", idUsuario, estadoDTO.isActivo());

        try {
            TiendaDetallesResponse tiendaActualizada = tiendaService.actualizarEstado(idUsuario, estadoDTO.isActivo());

            log.info("Estado actualizado correctamente para el usuario {}", idUsuario);
            return ResponseEntity.ok(tiendaActualizada);

        } catch (NoSuchElementException e) {
            log.warn("No se pudo actualizar estado. Tienda no encontrada para usuario {}", idUsuario);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{idUsuario}/verificacion")
    public ResponseEntity<TiendaDetallesResponse> actualizarVerificacion(
            @PathVariable Long idUsuario,
            @RequestBody VerificacionResponse verificacionDTO) {

        log.info("PUT /tiendas/{}/verificacion - Actualizando verificación a {}",
                idUsuario, verificacionDTO.isVerificado());

        try {
            TiendaDetallesResponse tiendaActualizada = tiendaService.actualizarVerificacion(idUsuario,
                    verificacionDTO.isVerificado());

            log.info("Verificación actualizada correctamente para el usuario {}", idUsuario);
            return ResponseEntity.ok(tiendaActualizada);

        } catch (NoSuchElementException e) {
            log.warn("No se pudo actualizar verificación. Tienda no encontrada para usuario {}", idUsuario);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/actualizar/{idTienda}")
    public ResponseEntity<MessageResponse> actualizarDatosTienda(
            @PathVariable Long idTienda,
            @RequestBody TiendaFormRequest formulario) {
        log.info("Actualizando datos de la tienda con id {}", idTienda);

        tiendaService.actualizarDatos(idTienda, formulario);

        log.info("Datos de la tienda con id {} actualizados correctamente", idTienda);
        return ResponseEntity.ok(new MessageResponse("Datos actualizados correctamente."));
    }

    @PutMapping("/{idTienda}/logo")
    public ResponseEntity<MessageResponse> actualizarLogo(
            @PathVariable Long idTienda,
            @RequestParam("logo") MultipartFile logoFile) {
        try {
            log.info("Actualizando el logo para la tienda con id: {}", idTienda);
            byte[] logoBytes = logoFile.getBytes();

            tiendaService.actualizarLogo(idTienda, logoBytes);

            log.info("Logo actualizado correctamente para la tiend con id: {}", idTienda);
            return ResponseEntity.ok(new MessageResponse("Logo actualizado correctamente."));
        } catch (IOException e) {
            log.error("Error al actualizar el logo para la tienda con id: {}", idTienda);
            return ResponseEntity.badRequest().body(new MessageResponse("Error al procesar el archivo."));
        }
    }

    @PutMapping("/{idTienda}/api")
    public ResponseEntity<EstadoAPI> actualizarApi(
            @PathVariable Long idTienda,
            @RequestBody String api) {
        log.info("Actualizando API de la tienda con id {}", idTienda);

        EstadoAPI estado = tiendaService.actualizarApi(idTienda, api);

        log.info("Datos de la tienda con id {} actualizados correctamente", idTienda);
        return ResponseEntity.ok(estado);
    }

    @GetMapping("/{idTienda}/api")
    public ResponseEntity<TiendaAPI> obtenerApi(
        @PathVariable Long idTienda
    ) {
        log.info("Enviado api de la tienda con id {}", idTienda);

        TiendaAPI api = tiendaService.buscarApi(idTienda);

        log.info("Api enviada correctamente de la tienda con id {}", idTienda);
        return ResponseEntity.ok(api);
    }

    @PutMapping("/{idTienda}/probar")
    public ResponseEntity<EstadoAPI> probarApi(
            @PathVariable Long idTienda) {
        log.info("Probando API de la tienda con id {}", idTienda);

        EstadoAPI estado = tiendaService.probarApi(idTienda);

        log.info("Api probada correctamente", idTienda);
        return ResponseEntity.ok(estado);
    }

}
