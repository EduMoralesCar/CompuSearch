package com.universidad.compusearch.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsuarioInfoResponse {
    private String username;
    private String email;
    private LocalDateTime fechaRegistro;
    private int builds;
    private int incidentes;
    private int solicitudes;
}
