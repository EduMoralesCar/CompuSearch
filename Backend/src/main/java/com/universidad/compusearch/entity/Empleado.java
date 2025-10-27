package com.universidad.compusearch.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa a un empleado dentro del sistema.
 * <p>
 * Esta entidad extiende de {@link Usuario}, heredando sus atributos básicos y añadiendo
 * información específica del empleado, como su nombre, apellido, rol y fecha de asignación.
 * </p>
 *
 * <p>
 * Los empleados tienen roles definidos en {@link Rol}, que determinan los permisos
 * y accesos dentro del sistema de acuerdo con las políticas de seguridad implementadas
 * mediante Spring Security.
 * </p>
 *
 * <p>
 * Se utilizan las anotaciones de Lombok {@code @Getter}, {@code @Setter} y {@code @NoArgsConstructor}
 * para reducir el código repetitivo, y la herencia JPA se gestiona mediante
 * {@code @PrimaryKeyJoinColumn}.
 * </p>
 *
 * @author Jesus
 * @version 1.0
 */
@Entity
@Table(name = "empleado")
@PrimaryKeyJoinColumn(name = "idUsuario")
@Getter
@Setter
@NoArgsConstructor
public class Empleado extends Usuario {

    /**
     * Nombre del empleado.
     * No puede superar los 50 caracteres ni ser nulo.
     */
    @Column(nullable = false, length = 50)
    private String nombre;

    /**
     * Apellido del empleado.
     * No puede superar los 50 caracteres ni ser nulo.
     */
    @Column(nullable = false, length = 50)
    private String apellido;

    /**
     * Rol asignado al empleado dentro del sistema.
     * <p>
     * El rol determina el nivel de permisos del empleado, como {@code ADMIN}, {@code SUPERVISOR}, etc.
     * </p>
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    /**
     * Fecha en que se asignó el rol o se registró al empleado.
     * Se inicializa automáticamente con la fecha y hora actuales.
     */
    @Column(nullable = false)
    private LocalDateTime fechaAsignacion = LocalDateTime.now();

    /**
     * Retorna las autoridades (roles) del empleado para el manejo de autenticación y autorización
     * en Spring Security.
     * <p>
     * Combina tanto el tipo de usuario heredado de {@link Usuario} como el {@link Rol} específico del empleado.
     * </p>
     *
     * @return una colección de {@link GrantedAuthority} con los roles del empleado.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + getTipoUsuario().name()));
        authorities.add(new SimpleGrantedAuthority("ROLE_" + rol.name()));
        return authorities;
    }
}
