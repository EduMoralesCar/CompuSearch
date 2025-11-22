package com.universidad.compusearch.exception;

import java.time.LocalDateTime;

public class ReporteException extends CustomException{

    public ReporteException(String message) {
        super(message);
    }

    public static ReporteException obtenerTiendasPorFecha(LocalDateTime fecha) {
        return new ReporteException("Error al obtener las tiendas desde la fecha " + fecha);
    }

    public static ReporteException obtenerTiendasPorProductos() {
        return new ReporteException("Error al obtener las tiendas por productos");
    }

    public static ReporteException obtenerTiendasPorVisitas() {
        return new ReporteException("Error al obtener las tiendas por visitas");
    }

    public static ReporteException exportarExcel(String reporte) {
        return new ReporteException("Error al exportar el tipo de reporte: " + reporte);
    }
}
