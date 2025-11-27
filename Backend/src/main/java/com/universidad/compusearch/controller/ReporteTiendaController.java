package com.universidad.compusearch.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.entity.ProductoTienda;
import com.universidad.compusearch.service.ProductoTiendaService;
import com.universidad.compusearch.service.ReporteTiendaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/reportes/tiendas")
@RequiredArgsConstructor
@Slf4j
public class ReporteTiendaController {

    private final ReporteTiendaService reporteTiendaService;
    private final ProductoTiendaService productoTiendaService;

    // CATÁLOGO COMPLETO
    @GetMapping("/{idTienda}/catalogo")
    public ResponseEntity<byte[]> exportarCatalogo(@PathVariable long idTienda) {
        List<ProductoTienda> productos = productoTiendaService.obtenerProductosPorTienda(idTienda);

        byte[] excel = reporteTiendaService.generarCatalogoProductos(productos,
                "Catálogo de Productos");

        return crearResponseEntity(excel, "catalogo_productos.xlsx");
    }

    // PRODUCTOS CON BAJO STOCK
    @GetMapping("/{idTienda}/stock-bajo")
    public ResponseEntity<byte[]> exportarProductosBajoStock(@PathVariable long idTienda) {
        List<ProductoTienda> productos = productoTiendaService.obtenerProductosPorTienda(idTienda);

        byte[] excel = reporteTiendaService.generarProductosBajoStock(productos,
                "Productos con Bajo Stock", 15);

        return crearResponseEntity(excel, "productos_bajo_stock.xlsx");
    }

    // MÉTRICAS DE PRODUCTOS
    @GetMapping("/{idTienda}/metricas")
    public ResponseEntity<byte[]> exportarProductosMetricas(@PathVariable long idTienda) {
        List<ProductoTienda> productos = productoTiendaService.obtenerProductosPorTienda(idTienda);

        byte[] excel = reporteTiendaService.generarMetricasTienda(productos,
                "Métricas de Productos");

        return crearResponseEntity(excel, "metricas_productos.xlsx");
    }

    // AUXILIAR PARA DESCARGA
    private ResponseEntity<byte[]> crearResponseEntity(byte[] bytes, String filename) {
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + filename)
                .header("Content-Type",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(bytes);
    }
}
