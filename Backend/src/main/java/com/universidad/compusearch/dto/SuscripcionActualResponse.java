package com.universidad.compusearch.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuscripcionActualResponse {
    private SuscripcionResponse actual;
    private SuscripcionResponse pendiente;
}
