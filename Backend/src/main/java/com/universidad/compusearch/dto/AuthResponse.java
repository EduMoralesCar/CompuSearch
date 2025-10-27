package com.universidad.compusearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO que representa la respuesta de autenticación de un usuario.
 * 
 * <p>Esta clase se utiliza para enviar al cliente la información básica del usuario
 * luego de un proceso de autenticación exitoso, incluyendo su identificador y rol.</p>
 * 
 * <ul>
 *   <li><b>idUsuario:</b> Identificador único del usuario en la base de datos.</li>
 *   <li><b>identificador:</b> Nombre de usuario o correo con el que inició sesión.</li>
 *   <li><b>tipoUsuario:</b> Tipo general de usuario (por ejemplo, USUARIO, EMPLEADO o TIENDA).</li>
 *   <li><b>rol:</b> Rol específico del usuario dentro del sistema (por ejemplo, ADMIN, MONITOREO, SOPORTE).</li>
 * </ul>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthResponse {

    /** Identificador único del usuario. */
    private long idUsuario;

    /** Nombre de usuario o correo con el que inició sesión. */
    private String identificador;

    /** Tipo general de usuario (USUARIO, EMPLEADO o TIENDA). */
    private String tipoUsuario;

    /** Rol específico del usuario dentro del sistema. */
    private String rol;
}
