package com.universidad.compusearch.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO que representa la información básica de un usuario.
 * 
 * <p>Esta clase se utiliza para enviar datos resumidos del usuario hacia el cliente,
 * evitando exponer entidades completas.</p>
 * 
 * <ul>
 *   <li><b>username:</b> Nombre de usuario único.</li>
 *   <li><b>email:</b> Correo electrónico asociado.</li>
 *   <li><b>fechaRegistro:</b> Fecha en la que el usuario se registró en el sistema.</li>
 *   <li><b>builds:</b> Cantidad de builds creadas por el usuario.</li>
 *   <li><b>incidentes:</b> Cantidad de incidentes reportados.</li>
 *   <li><b>solicitudes:</b> Número de solicitudes de tienda realizadas.</li>
 * </ul>
 * 
 */
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
