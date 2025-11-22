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

    @NotBlank(message = "El nombre de la build es obligatorio")
    private String nombre;

    @NotNull(message = "La compatibilidad de la build es obligatoria")
    private boolean compatible;

    @NotNull(message = "El costo total de la build es obligatorio")
    private BigDecimal costoTotal;

    @NotBlank(message = "El consumo total es obligatorio")
    private String consumoTotal;

    @NotNull(message = "El id del usuario de la build es obligatorio")
    private Long idUsuario;

    @NotNull(message = "La lista de detalles no puede estar vacía")
    private List<DetalleBuildRequest> detalles;
}
