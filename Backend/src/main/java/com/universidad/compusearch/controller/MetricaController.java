package com.universidad.compusearch.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.entity.Metrica;
import com.universidad.compusearch.service.MetricaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/metricas")
@RequiredArgsConstructor
@Slf4j
public class MetricaController {

    private final MetricaService metricaService;


    @GetMapping
    public ResponseEntity<List<Metrica>> getAllMetricas() {
        return ResponseEntity.ok(metricaService.findAll());
    }

    @GetMapping("/{idProductoTienda}")
    public ResponseEntity<Metrica> getMetricaByProductoTiendaId(@PathVariable Long idProductoTienda) {
        return metricaService.findByIdProductoTienda(idProductoTienda)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Metrica> createMetrica(@RequestBody Metrica metrica) {
        Metrica savedMetrica = metricaService.save(metrica);
        return new ResponseEntity<>(savedMetrica, HttpStatus.CREATED);
    }

    @DeleteMapping("/{idMetrica}")
    public ResponseEntity<Void> deleteMetrica(@PathVariable Long idMetrica) {
        metricaService.deleteById(idMetrica);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{idProductoTienda}/visitas")
    public ResponseEntity<Metrica> incrementarVisitas(@PathVariable Long idProductoTienda) {
        Metrica updatedMetrica = metricaService.incrementarVisitas(idProductoTienda);
        if (updatedMetrica != null) {
            return ResponseEntity.ok(updatedMetrica);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{idProductoTienda}/clicks")
    public ResponseEntity<Metrica> incrementarClicks(@PathVariable Long idProductoTienda) {
        Metrica updatedMetrica = metricaService.incrementarClicks(idProductoTienda);
        if (updatedMetrica != null) {
            return ResponseEntity.ok(updatedMetrica);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{idProductoTienda}/builds")
    public ResponseEntity<Metrica> incrementarBuilds(@PathVariable Long idProductoTienda) {
        Metrica updatedMetrica = metricaService.incrementarBuilds(idProductoTienda);
        if (updatedMetrica != null) {
            return ResponseEntity.ok(updatedMetrica);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
