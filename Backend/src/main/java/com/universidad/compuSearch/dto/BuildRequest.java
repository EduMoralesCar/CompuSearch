package com.universidad.compusearch.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BuildRequest {

    @NotBlank(message = "El nombre de la build es obligatoria")
    private String nombre;

    @NotBlank(message = "La compatibilidad de la build es obligatoria")
    private boolean compatible;

    @NotBlank(message = "El costo total de la build es obligatoria")
    private BigDecimal costoTotal;

    @NotBlank(message = "El id del usuario de la build es obligatoria")
    private Long idUsuario;

    @NotNull(message = "La lista de atributos no puede ser vacio")
    private List<DetalleBuildRequest> detalles;
}
