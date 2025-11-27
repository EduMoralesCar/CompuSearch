package com.universidad.compusearch.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.dto.MessageResponse;
import com.universidad.compusearch.service.ApiService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class ApiController {

    private final ApiService apiService;

    @PostMapping("/{idTienda}/actualizar")
    public ResponseEntity<MessageResponse> actualizarUnaTienda(@PathVariable Long idTienda) {
        log.info("Actualizando productos desde API para la tienda {}", idTienda);

        apiService.obtenerProductosDesdeApiPorTienda(idTienda);

        return ResponseEntity.ok(new MessageResponse("Actualización ejecutada para la tienda con ID: " + idTienda));
    }
}
