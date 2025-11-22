package com.universidad.compusearch.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "suscripcion")
@Getter
@Setter
@NoArgsConstructor
public class Suscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idSuscripcion;

    @Column(nullable = false)
    private LocalDateTime fechaInicio;

    @Column(nullable = true)
    private LocalDateTime fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSuscripcion estado;

    @Column(nullable = true, unique = true)
    private String stripeSubscriptionId;

    @Column(nullable = true)
    private String stripeLatestInvoiceId;

    @Column(nullable = true)
    private String stripeClientSecret;

    // Referencia al plan que tiene la suscripcion
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_plan", nullable = false)
    @JsonBackReference
    private Plan plan;

    // Referencia a la tienda que tiene la suscripcion
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tienda", nullable = false)
    @JsonBackReference
    private Tienda tienda;

    // Referencia a la lista de pagos de las suscripciones
    @OneToMany(mappedBy = "suscripcion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Pago> pagos = new ArrayList<>();
}
