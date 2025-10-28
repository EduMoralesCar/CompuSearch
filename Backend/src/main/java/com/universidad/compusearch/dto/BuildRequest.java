package com.universidad.compusearch.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO que representa la solicitud de creación de una nueva Build.
 * 
 * <p>Esta clase se utiliza para recibir los datos necesarios
 * para registrar una build personalizada de hardware creada por un usuario.</p>
 * 
 * <ul>
 *   <li><b>nombre:</b> Nombre asignado a la build.</li>
 *   <li><b>compatible:</b> Indica si la build es compatible.</li>
 *   <li><b>costoTotal:</b> Costo total de todos los componentes.</li>
 *   <li><b>consumoTotal:</b> Consumo energético total estimado.</li>
 *   <li><b>idUsuario:</b> Identificador del usuario que creó la build.</li>
 *   <li><b>detalles:</b> Lista de los productos que componen la build.</li>
 * </ul>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BuildRequest {

    /** Nombre asignado a la build. */
    @NotBlank(message = "El nombre de la build es obligatorio")
    private String nombre;

    /** Indica si la build es compatible. */
    @NotNull(message = "La compatibilidad de la build es obligatoria")
    private boolean compatible;

    /** Costo total de los componentes de la build. */
    @NotNull(message = "El costo total de la build es obligatorio")
    private BigDecimal costoTotal;

    /** Consumo energético total de la build. */
    @NotBlank(message = "El consumo total es obligatorio")
    private String consumoTotal;

    /** Identificador del usuario creador de la build. */
    @NotNull(message = "El id del usuario de la build es obligatorio")
    private Long idUsuario;

    /** Lista de componentes o detalles que forman parte de la build. */
    @NotNull(message = "La lista de detalles no puede estar vacía")
    private List<DetalleBuildRequest> detalles;
}
