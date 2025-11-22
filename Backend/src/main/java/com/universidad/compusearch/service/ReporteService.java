package com.universidad.compusearch.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.Tienda;
import com.universidad.compusearch.exception.ReporteException;
import com.universidad.compusearch.repository.TiendaRepository;
import com.universidad.compusearch.util.ExcelExporter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReporteService {

    private final TiendaRepository tiendaRepository;

    // Obtener todas las tiendas desde x fecha
    public List<Tienda> getTiendasDesdeFecha(LocalDateTime fechaInicio) {
        log.info("Buscando tiendas afiliadas desde la fecha: {}", fechaInicio);
        try {
            List<Tienda> tiendas = tiendaRepository.findByFechaAfiliacionGreaterThanEqual(fechaInicio);
            log.info("Consulta exitosa. Se encontraron {} tiendas.", tiendas.size());
            return tiendas;

        } catch (Exception e) {
            log.error("Error al buscar tiendas desde {}: {}", fechaInicio, e.getMessage(), e);
            throw ReporteException.obtenerTiendasPorFecha(fechaInicio);
        }
    }

    // Obtener x tiendas con mas productos desc
    public List<Tienda> getTopNTiendasConMasProductos(int n) {
        log.info("Buscando el Top {} de tiendas con más productos.", n);
        Pageable pageable = PageRequest.of(0, n);

        try {
            List<Tienda> tiendas = tiendaRepository.findTopNTiendasByProductoCount(pageable);
            log.info("Halladas {} tiendas para el Top {}", tiendas.size(), n);
            return tiendas;

        } catch (Exception e) {
            log.error("Error al buscar Top {} tiendas por productos: {}", n, e.getMessage(), e);
            throw ReporteException.obtenerTiendasPorProductos();
        }
    }

    // Obtener x tiendas con mas redirecciones a su tienda
    public List<Object[]> getTopNTiendasConMasRedirecciones(int n) {
        log.info("Buscando el Top {} de tiendas con más visitas.", n);
        Pageable pageable = PageRequest.of(0, n);

        try {
            List<Object[]> resultados = tiendaRepository.findTiendasByTotalVisitas(pageable);
            log.info("Hallados {} resultados para el Top {}", resultados.size(), n);
            return resultados;

        } catch (Exception e) {
            log.error("Error al buscar Top {} tiendas por visitas: {}", n, e.getMessage(), e);
            throw ReporteException.obtenerTiendasPorVisitas();
        }
    }

    // Exportar tiendas desde x fecha
    public byte[] exportTiendasDesdeFechaExcel(LocalDateTime fechaInicio) {
        List<Tienda> tiendas = getTiendasDesdeFecha(fechaInicio);

        try {
            return ExcelExporter.exportTiendas(tiendas, "Tiendas Afiliadas");
        } catch (Exception e) {
            log.error("Error exportando Excel de tiendas por fecha: {}", e.getMessage(), e);
            throw ReporteException.exportarExcel("Tiendas desde fecha");
        }
    }

    // Expotar x tiendas con mas productos
    public byte[] exportTopNTiendasPorProductosExcel(int n) {
        List<Tienda> tiendas = getTopNTiendasConMasProductos(n);

        try {
            return ExcelExporter.exportTiendas(tiendas, "Top Tiendas con más Productos");
        } catch (Exception e) {
            log.error("Error exportando Excel de Top {} tiendas por productos", n, e);
            throw ReporteException.exportarExcel("Top tiendas por productos");
        }
    }

    // Exportar x tiendas con mas visistas
    public byte[] exportTopNTiendasPorVisitasExcel(int n) {
        List<Object[]> datos = getTopNTiendasConMasRedirecciones(n);

        String[] headers = { "ID Tienda", "Nombre", "Total Visitas" };

        try {
            return ExcelExporter.exportResultadosGenericos(
                    datos,
                    "Top_Tiendas_Visitas",
                    headers);
        } catch (Exception e) {
            log.error("Error exportando Excel de Top {} tiendas por visitas", n, e);
            throw ReporteException.exportarExcel("Top tiendas por visitas");
        }
    }
}
