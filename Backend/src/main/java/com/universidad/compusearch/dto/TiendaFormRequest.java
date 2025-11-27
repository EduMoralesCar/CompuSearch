package com.universidad.compusearch.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TiendaFormRequest {
    @NotBlank(message = "El nombre de la tienda es obligatorio")
    private String nombre;

    @NotBlank(message = "El telefono de la tienda es obligatorio")
    private String telefono;

    @NotBlank(message = "La descripcion de la tienda es obligatorio")
    private String descripcion;

    @NotBlank(message = "La direccion de la tienda es obligatorio")
    private String direccion;

    @NotBlank(message = "La pagina web de la tienda es obligatorio")
    private String urlPagina;
}
