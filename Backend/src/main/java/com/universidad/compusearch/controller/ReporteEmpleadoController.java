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
                LocalDateTime fechaInicio = LocalDateTime.parse(fechaInicioStr);
                byte[] bytes = reporteEmpleadoService.exportTiendasDesdeFechaExcel(fechaInicio);
                return crearResponseEntity(bytes, "Tiendas_Desde_Fecha.xlsx");
        }

        @GetMapping("/tiendas/top-productos")
        public ResponseEntity<byte[]> exportTopTiendasPorProductos(@RequestParam("n") int n) {
                byte[] bytes = reporteEmpleadoService.exportTopNTiendasPorProductosExcel(n);
                return crearResponseEntity(bytes, "Top_Tiendas_Productos.xlsx");
        }

        @GetMapping("/tiendas/top-visitas")
        public ResponseEntity<byte[]> exportTopTiendasPorVisitas(@RequestParam("n") int n) {
                byte[] bytes = reporteEmpleadoService.exportTopNTiendasPorVisitasExcel(n);
                return crearResponseEntity(bytes, "Top_Tiendas_Visitas.xlsx");
        }

        // REPORTES DE USUARIOS
        @GetMapping("/usuarios/desde-fecha")
        public ResponseEntity<byte[]> exportUsuariosDesdeFecha(@RequestParam("fechaInicio") String fechaInicioStr) {
                LocalDateTime fechaInicio = LocalDateTime.parse(fechaInicioStr);
                byte[] bytes = reporteEmpleadoService.exportUsuariosDesdeFechaExcel(fechaInicio);
                return crearResponseEntity(bytes, "Usuarios_Desde_Fecha.xlsx");
        }

        @GetMapping("/usuarios/activos-inactivos")
        public ResponseEntity<byte[]> exportUsuariosActivosInactivos() {
                byte[] bytes = reporteEmpleadoService.exportUsuariosActivosInactivosExcel();
                return crearResponseEntity(bytes, "Usuarios_Activos_Inactivos.xlsx");
        }

        // MÉTODO AUXILIAR PARA DESCARGA
        private ResponseEntity<byte[]> crearResponseEntity(byte[] bytes, String filename) {
                return ResponseEntity.ok()
                                .header("Content-Disposition", "attachment; filename=" + filename)
                                .header("Content-Type",
                                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                                .body(bytes);
        }
}
