package com.universidad.compusearch.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "metrica")
@Getter
@Setter
@NoArgsConstructor
public class Metrica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idMetrica;

    @Column(nullable = false)
    private int visitas = 0;

    @Column(nullable = false)
    private int clicks = 0;

    @Column(nullable = false)
    private int builds = 0;

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    // Referencia al producto de la tienda
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_producto_tienda", nullable = false)
    @JsonBackReference
    private ProductoTienda productoTienda;
}
