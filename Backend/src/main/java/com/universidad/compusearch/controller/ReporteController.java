package com.universidad.compusearch.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.service.ReporteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
@Slf4j
public class ReporteController {

        private final ReporteService reporteService;

        @GetMapping("/tiendas/desde-fecha/excel")
        public ResponseEntity<byte[]> exportTiendasDesdeFecha(
                        @RequestParam("fecha") String fecha) {

                fecha = fecha.trim().replaceAll("[\\r\\n]", "");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                LocalDateTime fechaReal = LocalDateTime.parse(fecha, formatter);

                byte[] excel = reporteService.exportTiendasDesdeFechaExcel(fechaReal);

                return ResponseEntity.ok()
                                .header("Content-Disposition", "attachment; filename=tiendas_desde_fecha.xlsx")
                                .body(excel);
        }

        @GetMapping("/tiendas/top-productos/excel")
        public ResponseEntity<byte[]> exportTopNTiendasProductos(
                        @RequestParam("n") int n) {

                byte[] excel = reporteService.exportTopNTiendasPorProductosExcel(n);

                return ResponseEntity.ok()
                                .header("Content-Disposition", "attachment; filename=top_tiendas_productos.xlsx")
                                .body(excel);
        }

        @GetMapping("/tiendas/top-visitas/excel")
        public ResponseEntity<byte[]> exportTopNTiendasVisitas(
                        @RequestParam("n") int n) {

                byte[] excel = reporteService.exportTopNTiendasPorVisitasExcel(n);

                return ResponseEntity.ok()
                                .header("Content-Disposition", "attachment; filename=top_tiendas_visitas.xlsx")
                                .body(excel);
        }
}
