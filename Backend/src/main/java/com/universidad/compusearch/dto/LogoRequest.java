package com.universidad.compusearch.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LogoRequest {
    
    @NotNull(message = "El logo no debe enviarse vacio")
    private byte[] logo;
}
