package com.universidad.compusearch.util;

import java.util.Map;

public class FiltroUtils {

    // Clase utilitaria para remover los filtros
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
