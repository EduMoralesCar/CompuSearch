package com.universidad.compusearch.entity;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idToken;

    @Column(nullable = false, unique = true, length = 512)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoToken tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoToken estado = EstadoToken.ACTIVO;

    @Column(nullable = false)
    private Instant fechaCreacion = Instant.now();

    @Column(nullable = false)
    private Instant fechaExpiracion;

    @Column(nullable = false)
    private String ipDispositivo;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    public boolean isExpired() {
        return Instant.now().isAfter(fechaExpiracion) || estado == EstadoToken.EXPIRADO;
    }
}