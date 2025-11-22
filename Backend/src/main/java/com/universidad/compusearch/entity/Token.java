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

@Entity
@Table(name = "token")
@Getter
@Setter
@NoArgsConstructor
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

    // Referencia al usuario que pertenece el token
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUsuario", nullable = false)
    @JsonBackReference
    private Usuario usuario;

    // Verifica si el token a expirado
    public boolean isExpired() {
        return Instant.now().isAfter(fechaExpiracion) || estado == EstadoToken.EXPIRADO;
    }
}
