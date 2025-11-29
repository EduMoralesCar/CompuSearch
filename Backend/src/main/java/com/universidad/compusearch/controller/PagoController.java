package com.universidad.compusearch.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.dto.PagoHistorialResponse;
import com.universidad.compusearch.service.PagoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/pagos")
@RequiredArgsConstructor
@Slf4j
public class PagoController {

    private final PagoService pagoService;

    @GetMapping("/historial/{idTienda}")
    public Page<PagoHistorialResponse> obtenerHistorial(
            @PathVariable Long idTienda,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Obteniendo pagos de la tienda con id {}", idTienda);
        return pagoService.obtenerHistorialPagos(idTienda, PageRequest.of(page, size));
    }
}
