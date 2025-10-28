package com.universidad.compusearch.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO que representa los datos de las solicitudes
 * de los usuarios para convertirse en tiendas
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SolicitudTiendaResponse {
    private Long idSolicitudTienda;
    private Long idUsuario;
    private String nombreUsuario;
    private String datosFormulario;
    private LocalDateTime fechaSolicitud;
    private String estado;
    private Long idEmpleado;
    private String nombreEmpleado;
}
