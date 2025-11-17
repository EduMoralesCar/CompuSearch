package com.universidad.compusearch.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@Table(name = "pago")
@Getter
@Setter
@NoArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idPago;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estado;

    @Column(name = "id_operacion", nullable = false, unique = true, length = 100)
    private String idOperacion;

    @Column(nullable = false)
    private LocalDateTime fechaPago;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    // Referencia a que suscripcion es el pago
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_suscripcion", nullable = false)
    @JsonBackReference
    private Suscripcion suscripcion;
}
