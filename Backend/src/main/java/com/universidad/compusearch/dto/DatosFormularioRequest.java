package com.universidad.compusearch.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DatosFormularioRequest {

    @NotBlank(message = "El nombre de la tienda es obligatorio")
    private String nombreTienda;

    @NotBlank(message = "El ruc de la tienda es obligatorio")
    private String ruc;

    @NotBlank(message = "El DNI del dueño es obligatorio")
    private String documentoIdentidad;

    @NotBlank(message = "La descripcion de la tienda es obligatorio")
    private String descripcion;
    
    @NotBlank(message = "El telefono o dueño de la tienda es obligatorio")
    private String telefono;

    @NotBlank(message = "El email de contacto de la tienda es obligatorio")
    private String emailContacto;

    private String aniosExperiencia;

    @NotBlank(message = "La direccion de la tienda es obligatorio")
    private String direccion;

    @NotBlank(message = "El sitio web de la tienda es obligatorio")
    private String sitioWeb;

    private String redesSociales;

    private String fotoLocal;
}