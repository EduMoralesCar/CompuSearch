package com.universidad.compusearch.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.Metrica;
import com.universidad.compusearch.entity.ProductoTienda;
import com.universidad.compusearch.repository.MetricaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetricaService {

    private final MetricaRepository metricaRepository;
    private final ProductoTiendaService productoTiendaService;

    // Obtener todas las metricas
    public List<Metrica> findAll() {
        return metricaRepository.findAll();
    }

    // Obtener una metrica por id de producto tienda
    public Optional<Metrica> findByIdProductoTienda(Long id) {
        return metricaRepository.findByProductoTiendaIdProductoTienda(id);
    }

    // Obtener una metrica por id
    public Optional<Metrica> findById(Long id) {
        return metricaRepository.findById(id);
    }

    // Guardar una metrica por id
    public Metrica save(Metrica metrica) {
        return metricaRepository.save(metrica);
    }

    // Eliminar una metrica por id
    public void deleteById(Long id) {
        metricaRepository.deleteById(id);
    }

    @Transactional
    public Metrica incrementarVisitas(Long idProductoTienda) {
        Metrica metrica = obtenerMetricaDelDia(idProductoTienda);
        metrica.setVisitas(metrica.getVisitas() + 1);
        return metricaRepository.save(metrica);
    }

    @Transactional
    public Metrica incrementarClicks(Long idProductoTienda) {
        Metrica metrica = obtenerMetricaDelDia(idProductoTienda);
        metrica.setClicks(metrica.getClicks() + 1);
        return metricaRepository.save(metrica);
    }

    @Transactional
    public Metrica incrementarBuilds(Long idProductoTienda) {
        Metrica metrica = obtenerMetricaDelDia(idProductoTienda);
        metrica.setBuilds(metrica.getBuilds() + 1);
        return metricaRepository.save(metrica);
    }

    @Transactional
    private Metrica obtenerMetricaDelDia(Long idProductoTienda) {
        ProductoTienda producto = productoTiendaService.obtenerPorId(idProductoTienda);

        if (producto.getMetricas() == null) {
            producto.setMetricas(new ArrayList<>());
        }

        // Ordenar métricas de forma descendente por fecha
        producto.getMetricas()
                .sort((m1, m2) -> m2.getFecha().compareTo(m1.getFecha()));

        LocalDateTime ahora = LocalDateTime.now();
        LocalDate hoy = ahora.toLocalDate();

        Metrica ultima = producto.getMetricas().isEmpty()
                ? null
                : producto.getMetricas().get(0);

        // Si no existe ninguna métrica → crear la primera
        if (ultima == null) {
            return crearNuevaMetrica(producto, ahora);
        }

        // Si la última métrica NO es del mismo día → crear nueva
        if (!ultima.getFecha().toLocalDate().isEqual(hoy)) {
            return crearNuevaMetrica(producto, ahora);
        }

        // Si la métrica es del mismo día → usarla
        return ultima;
    }

    private Metrica crearNuevaMetrica(ProductoTienda producto, LocalDateTime fecha) {
        Metrica nueva = new Metrica();
        nueva.setProductoTienda(producto);
        nueva.setFecha(fecha);
        nueva.setVisitas(0);
        nueva.setClicks(0);
        nueva.setBuilds(0);

        producto.getMetricas().add(nueva);

        return metricaRepository.save(nueva);
    }

}
