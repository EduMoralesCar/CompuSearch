package com.universidad.compusearch.entity;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa un token de autenticación emitido para un {@link Usuario}.
 * 
 * Esta entidad permite gestionar las sesiones y validar la autenticidad de los
 * usuarios en el sistema CompuSearch.
 * Cada token puede tener un estado, tipo y tiempo de expiración determinados.
 * 
 *
 * Relaciones:
 * <ul>
 *   <li>{@link Usuario}: cada token pertenece a un usuario específico.</li>
 * </ul>
 *
 * El campo {@code fechaExpiracion} define el instante exacto en el que el token
 * deja de ser válido, mientras que el campo {@code estado} permite identificar
 * si el token ha sido revocado o ya expiró.
 * 
 *
 * Se utiliza {@link Instant} para registrar los tiempos en formato UTC,
 * garantizando compatibilidad con diferentes zonas horarias.
 *
 * 
 */
@Entity
@Table(name = "token")
@Getter
@Setter
@NoArgsConstructor
public class Token {

    /** Identificador único del token. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idToken;

    /** Cadena única que representa el valor del token. */
    @Column(nullable = false, unique = true, length = 512)
    private String token;

    /** Tipo de token (por ejemplo, acceso o refresco). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoToken tipo;

    /** Estado actual del token: ACTIVO, EXPIRADO o REVOCADO. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoToken estado = EstadoToken.ACTIVO;

    /** Fecha y hora (UTC) en que el token fue generado. */
    @Column(nullable = false)
    private Instant fechaCreacion = Instant.now();

    /** Fecha y hora (UTC) en que el token expira y deja de ser válido. */
    @Column(nullable = false)
    private Instant fechaExpiracion;

    /** Dirección IP del dispositivo desde donde se generó el token. */
    @Column(nullable = false)
    private String ipDispositivo;

    /** Usuario al que pertenece este token. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUsuario", nullable = false)
    @JsonBackReference
    private Usuario usuario;

    /**
     * Verifica si el token ha expirado o se encuentra marcado como expirado.
     *
     * @return {@code true} si el token ha expirado o su estado es {@link EstadoToken#EXPIRADO},
     *         {@code false} en caso contrario.
     */
    public boolean isExpired() {
        return Instant.now().isAfter(fechaExpiracion) || estado == EstadoToken.EXPIRADO;
    }
}
