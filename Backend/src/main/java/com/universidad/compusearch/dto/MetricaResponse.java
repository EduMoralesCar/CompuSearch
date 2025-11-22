package com.universidad.compusearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MetricaResponse {
    private Long idTienda;
    private String nombre;
    private Long metrica;
}
