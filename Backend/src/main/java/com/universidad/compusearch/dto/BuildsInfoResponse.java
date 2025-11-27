package com.universidad.compusearch.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BuildsInfoResponse {

    private long idBuild;

    private String nombre;

    private long idUsuario;

    private BigDecimal costoTotal;

    private boolean compatible;

    private List<DetalleBuildResponse> detalles;
}
