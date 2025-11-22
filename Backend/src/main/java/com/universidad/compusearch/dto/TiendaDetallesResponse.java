package com.universidad.compusearch.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.universidad.compusearch.entity.Etiqueta;
import com.universidad.compusearch.entity.TiendaAPI;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TiendaDetallesResponse {
    private Long idUsuario;
    private String nombre;
    private String email;
    private boolean activo;
    private LocalDateTime fechaAfiliacion;
    private boolean verificado;
    private String telefono;
    private String direccion;
    private String pagina;
    private TiendaAPI tiendaAPI;
    private TiendaSuscripcionActualInfoResponse suscripcionActual;
    private List<Etiqueta> etiquetas;
    private List<ProductoTiendaInfoResponse> productos;
}
