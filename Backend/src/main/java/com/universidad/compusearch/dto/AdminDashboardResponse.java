package com.universidad.compusearch.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardResponse {

    private int totalTiendas;
    private int tiendasVerificadas;
    private int tiendasNoVerificadas;

    private int totalEmpleados;
    private int totalUsuarios;

    private int totalProductos;
    private int productosActivos;
    private int productosInactivos;

    private int totalSolicitudes;
    private int solicitudesPendientes;
    private int solicitudesRechazadas;
    private int solicitudesAceptadas;

    private int totalIncidentes;

    private int totalSuscripciones;
    private int suscripcionesActivas;
    private int suscripcionesExpiradas;

    private double ingresosTotales;
    private int totalPagos;
    
    private List<UltimaTiendaResponse> ultimasTiendas;
    private List<UltimoEmpleadoResponse> ultimosEmpleados;
    private List<UltimoPagoResponse> ultimosPagos;
}

