package com.universidad.compuSearch.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ResetToken")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idToken;

    @Column(nullable = false, unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoToken estado = EstadoToken.ACTIVO;

    @Column(nullable = false)
    private Instant fechaCreacion = Instant.now();

    @Column(nullable = false)
    @NotNull
    private Instant fechaExpiracion;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    @NotNull
    private Usuario usuario;

    public boolean isExpired() {
        return Instant.now().isAfter(fechaExpiracion) || estado != EstadoToken.ACTIVO;
    }

    public ResetToken(Usuario usuario, Instant expiracion) {
        this.usuario = usuario;
        this.token = java.util.UUID.randomUUID().toString();
        this.fechaCreacion = Instant.now();
        this.fechaExpiracion = expiracion;
        this.estado = EstadoToken.ACTIVO;
    }
}
