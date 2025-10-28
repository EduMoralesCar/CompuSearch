package com.universidad.compusearch.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa a un usuario dentro del sistema CompuSearch.
 * 
 * Esta entidad es la base de la jerarquía de usuarios mediante
 * la estrategia de herencia {@link InheritanceType#JOINED},
 * permitiendo especializar tipos de usuarios (por ejemplo, administradores,
 * clientes, etc.).
 * 
 *
 * Implementa la interfaz {@link UserDetails} para integrarse con el
 * sistema de autenticación de Spring Security, proporcionando los
 * métodos necesarios para la gestión de credenciales y roles.
 *
 * Relaciones:
 * <ul>
 *   <li>{@link Build}: un usuario puede tener múltiples builds creadas.</li>
 *   <li>{@link Token}: un usuario puede poseer varios tokens de autenticación.</li>
 *   <li>{@link Incidente}: un usuario puede reportar múltiples incidentes.</li>
 *   <li>{@link SolicitudTienda}: un usuario puede enviar varias solicitudes
 *       para registrar o modificar tiendas.</li>
 * </ul>
 *
 * Se utilizan las anotaciones de Lombok {@code @Getter}, {@code @Setter}
 * y {@code @NoArgsConstructor} para reducir el código repetitivo.
 *
 */
@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
public class Usuario implements UserDetails {

    /** Identificador único del usuario. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUsuario;

    /** Nombre de usuario único utilizado para iniciar sesión. */
    @Column(nullable = false, unique = true)
    private String username;

    /** Correo electrónico único del usuario. */
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    /** Contraseña cifrada del usuario. */
    @Column(nullable = false)
    private String contrasena;

    /** Indica si el usuario está activo en el sistema. */
    @Column(nullable = false)
    private boolean activo = true;

    /** Tipo o rol del usuario dentro del sistema. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipoUsuario;

    /** Fecha y hora en la que el usuario se registró en el sistema. */
    @Column(nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    /** Lista de builds creadas por el usuario. */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Build> builds = new ArrayList<>();

    /** Lista de tokens de autenticación asociados al usuario. */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Token> tokens = new ArrayList<>();

    /** Lista de incidentes reportados por el usuario. */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Incidente> incidentes = new ArrayList<>();

    /** Lista de solicitudes de tienda realizadas por el usuario. */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<SolicitudTienda> solicitudes = new ArrayList<>();

    // Implementación de UserDetails

    /**
     * Retorna las autoridades del usuario basadas en su tipo de usuario.
     * Por ejemplo: "ROLE_ADMIN" o "ROLE_CLIENTE".
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + tipoUsuario.name()));
    }

    /** Retorna la contraseña del usuario. */
    @Override
    public String getPassword() {
        return contrasena;
    }

    /** Retorna el nombre de usuario. */
    @Override
    public String getUsername() {
        return username;
    }

    /** Indica si la cuenta no ha expirado (siempre true). */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /** Indica si la cuenta no está bloqueada (depende del campo activo). */
    @Override
    public boolean isAccountNonLocked() {
        return activo;
    }

    /** Indica si las credenciales no han expirado (siempre true). */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /** Indica si el usuario está habilitado (depende del campo activo). */
    @Override
    public boolean isEnabled() {
        return activo;
    }
}
