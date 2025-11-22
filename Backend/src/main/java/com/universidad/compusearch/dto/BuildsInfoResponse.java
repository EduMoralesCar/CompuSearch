package com.universidad.compusearch.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO que representa la información general de una build registrada.
 * 
 * <p>Se utiliza para enviar los datos de una build al cliente,
 * incluyendo su información básica, costo total, compatibilidad
 * y los detalles de los componentes que la conforman.</p>
 * 
 * <ul>
 *   <li><b>idBuild:</b> Identificador único de la build.</li>
 *   <li><b>nombre:</b> Nombre asignado a la build.</li>
 *   <li><b>idUsuario:</b> Identificador del usuario creador.</li>
 *   <li><b>costoTotal:</b> Costo total de la build.</li>
 *   <li><b>compatible:</b> Indica si la build es compatible.</li>
 *   <li><b>detalles:</b> Lista de componentes o productos incluidos en la build.</li>
 * </ul>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BuildsInfoResponse {

    /** Identificador único de la build. */
    private long idBuild;

    /** Nombre asignado a la build. */
    private String nombre;

    /** Identificador del usuario que creó la build. */
    private long idUsuario;

    /** Costo total de la build. */
    private BigDecimal costoTotal;

    /** Indica si la build es compatible. */
    private boolean compatible;

    /** Lista de componentes o detalles asociados a la build. */
    private List<DetalleBuildResponse> detalles;
}
