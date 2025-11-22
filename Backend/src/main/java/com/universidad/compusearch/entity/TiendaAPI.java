package com.universidad.compusearch.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tienda_api")
@Getter
@Setter
@NoArgsConstructor
public class TiendaAPI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTiendaApi;

    @Column(nullable = false)
    private String urlBase;

    @Column(nullable = true)
    private String endpointCatalogo;

    @Column(nullable = true)
    private String endpointProducto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAPI estadoAPI;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAutenticacion tipoAutenticacion;

    @Column(nullable = true)
    private String apiKey;

    @Column(nullable = true)
    private String bearerToken;

    @Column(nullable = true)
    private String apiUsuario;

    @Column(nullable = true)
    private String apiContrasena;

    // Referencia a la tienda a la que esta asociada
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_tienda", referencedColumnName = "idUsuario", nullable = false, unique = true)
    @JsonBackReference
    private Tienda tienda;
}
