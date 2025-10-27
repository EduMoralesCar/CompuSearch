package com.universidad.compusearch.util;

import java.util.Map;

/**
 * Clase utilitaria que proporciona métodos para la limpieza y manipulación
 * de filtros utilizados en consultas de productos dentro del sistema CompuSearch.
 * 
 * <p>Esta clase contiene únicamente métodos estáticos y no debe ser instanciada.
 * Su función principal es eliminar parámetros innecesarios o reservados
 * antes de procesar filtros de búsqueda.</p>
 * 
 */
public class FiltroUtils {

    /**
     * Elimina del mapa los filtros que no deben considerarse en ciertas operaciones
     * de búsqueda o filtrado (como paginación, categoría o disponibilidad).
     * 
     * <p>Este método modifica directamente el mapa recibido y luego lo retorna.</p>
     *
     * @param filtros mapa que contiene los filtros originales, donde la clave representa el nombre del filtro
     *                y el valor su correspondiente criterio.
     * @return el mismo mapa recibido, pero sin las claves {@code categoria}, {@code nombreTienda},
     *         {@code precioMax}, {@code precioMin}, {@code disponible}, {@code marca},
     *         {@code page} y {@code size}.
     */
    public static Map<String, String> limpiarFiltros(Map<String, String> filtros) {
        filtros.remove("categoria");
        filtros.remove("nombreTienda");
        filtros.remove("precioMax");
        filtros.remove("precioMin");
        filtros.remove("disponible");
        filtros.remove("marca");
        filtros.remove("page");
        filtros.remove("size");
        return filtros;
    }
}
