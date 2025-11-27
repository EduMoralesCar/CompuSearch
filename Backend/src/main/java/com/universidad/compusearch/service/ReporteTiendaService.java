package com.universidad.compusearch.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.ProductoTienda;
import com.universidad.compusearch.excel.ExcelExporter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReporteTiendaService {

    private final ExcelExporter excelExporter;

    public byte[] generarCatalogoProductos(List<ProductoTienda> productos, String titulo) {
        try {
            return excelExporter.exportCatalogoProductos(productos, titulo);
        } catch (IOException e) {
            throw new RuntimeException("Error al generar reporte de catálogo", e);
        }
    }

    public byte[] generarProductosBajoStock(List<ProductoTienda> productos, String titulo, int limiteStock) {
        try {
            return excelExporter.exportProductosBajoStock(productos, titulo, limiteStock);
        } catch (IOException e) {
            throw new RuntimeException("Error al generar reporte de bajo stock", e);
        }
    }

    public byte[] generarMetricasTienda(List<ProductoTienda> productos, String titulo) {
        try {
            return excelExporter.exportMetricasTienda(productos, titulo);
        } catch (IOException e) {
            throw new RuntimeException("Error al generar reporte de métricas", e);
        }
    }
}
