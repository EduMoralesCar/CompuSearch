package com.universidad.compusearch.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.service.ReporteEmpleadoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/reportes/empleado")
@RequiredArgsConstructor
@Slf4j
public class ReporteEmpleadoController {

        private final ReporteEmpleadoService reporteEmpleadoService;

        // REPORTES DE TIENDAS
        @GetMapping("/tiendas/desde-fecha")
        public ResponseEntity<byte[]> exportTiendasDesdeFecha(@RequestParam("fechaInicio") String fechaInicioStr) {
                log.info("Iniciando exportación de reportes de tiendas desde fecha: {}", fechaInicioStr);
                LocalDateTime fechaInicio = LocalDateTime.parse(fechaInicioStr);

                byte[] bytes = reporteEmpleadoService.exportTiendasDesdeFechaExcel(fechaInicio);
                log.info("Reporte de tiendas desde fecha generado, tamaño del archivo: {} bytes", bytes.length);

                return crearResponseEntity(bytes, "Tiendas_Desde_Fecha.xlsx");
        }

        @GetMapping("/tiendas/top-productos")
        public ResponseEntity<byte[]> exportTopTiendasPorProductos(@RequestParam("n") int n) {
                log.info("Iniciando exportación de top {} tiendas por cantidad de productos", n);

                byte[] bytes = reporteEmpleadoService.exportTopNTiendasPorProductosExcel(n);
                log.info("Reporte top {} tiendas por productos generado, tamaño del archivo: {} bytes", n,
                                bytes.length);

                return crearResponseEntity(bytes, "Top_Tiendas_Productos.xlsx");
        }

        @GetMapping("/tiendas/top-visitas")
        public ResponseEntity<byte[]> exportTopTiendasPorVisitas(@RequestParam("n") int n) {
                log.info("Iniciando exportación de top {} tiendas por visitas", n);

                byte[] bytes = reporteEmpleadoService.exportTopNTiendasPorVisitasExcel(n);
                log.info("Reporte top {} tiendas por visitas generado, tamaño del archivo: {} bytes", n, bytes.length);

                return crearResponseEntity(bytes, "Top_Tiendas_Visitas.xlsx");
        }

        // REPORTES DE USUARIOS
        @GetMapping("/usuarios/desde-fecha")
        public ResponseEntity<byte[]> exportUsuariosDesdeFecha(@RequestParam("fechaInicio") String fechaInicioStr) {
                log.info("Iniciando exportación de reportes de usuarios desde fecha: {}", fechaInicioStr);
                LocalDateTime fechaInicio = LocalDateTime.parse(fechaInicioStr);

                byte[] bytes = reporteEmpleadoService.exportUsuariosDesdeFechaExcel(fechaInicio);
                log.info("Reporte de usuarios desde fecha generado, tamaño del archivo: {} bytes", bytes.length);

                return crearResponseEntity(bytes, "Usuarios_Desde_Fecha.xlsx");
        }

        @GetMapping("/usuarios/activos-inactivos")
        public ResponseEntity<byte[]> exportUsuariosActivosInactivos() {
                log.info("Iniciando exportación de reporte de usuarios activos e inactivos");

                byte[] bytes = reporteEmpleadoService.exportUsuariosActivosInactivosExcel();
                log.info("Reporte de usuarios activos/inactivos generado, tamaño del archivo: {} bytes", bytes.length);

                return crearResponseEntity(bytes, "Usuarios_Activos_Inactivos.xlsx");
        }

        // MÉTODO AUXILIAR PARA DESCARGA
        private ResponseEntity<byte[]> crearResponseEntity(byte[] bytes, String filename) {
                log.info("Preparando archivo '{}' para descarga, tamaño: {} bytes", filename, bytes.length);
                return ResponseEntity.ok()
                                .header("Content-Disposition", "attachment; filename=" + filename)
                                .header("Content-Type",
                                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                                .body(bytes);
        }
}
