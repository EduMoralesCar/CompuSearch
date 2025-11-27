package com.universidad.compusearch.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.universidad.compusearch.entity.Etiqueta;

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
public class TiendaInfoDetalleResponse {
    private Long idUsuario;
    private String nombre;
    private String email;
    private boolean activo;
    private String descripcion;
    private LocalDateTime fechaAfiliacion;
    private String telefono;
    private String direccion;
    private String pagina;
    private List<Etiqueta> etiquetas;
    private int productos;
    private byte[] logo;
}
