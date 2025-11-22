package com.universidad.compusearch.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO utilizado para la creación y modificación de Planes.
 */
@Getter
@Setter
public class PlanRequest {

    @NotBlank(message = "El nombre del plan no puede estar vacío.")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres.")
    private String nombre;

    @NotNull(message = "La duración en meses es obligatoria.")
    @Min(value = 1, message = "La duración mínima es de 1 mes.")
    private Integer duracionMeses;

    @NotNull(message = "El precio mensual es obligatorio.")
    @DecimalMin(value = "0.01", inclusive = true, message = "El precio debe ser positivo.")
    private BigDecimal precioMensual;

    @NotBlank(message = "La descripción del plan no puede estar vacía.")
    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres.")
    private String descripcion;

    // Beneficios es opcional, pero limitamos la longitud si se proporciona.
    @Size(max = 500, message = "Los beneficios no pueden exceder los 500 caracteres.")
    private String beneficios;
}