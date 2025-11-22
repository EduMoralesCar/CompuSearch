package com.universidad.compusearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthResponse {

    private long idUsuario;
    private String identificador;
    private String tipoUsuario;
    private String rol;
}
