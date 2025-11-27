package com.universidad.compusearch.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SolicitudTiendaResponse {
    private Long idSolicitudTienda;
    private Long idUsuario;
    private String nombreUsuario;
    private Map<String, Object> datosFormulario;
    private LocalDateTime fechaSolicitud;
    private String estado;
    private Long idEmpleado;
    private String nombreEmpleado;
}
