package com.universidad.compusearch.entity;

import java.math.BigDecimal;

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
@Table(name = "detalle_build")
@Getter
@Setter
@NoArgsConstructor
public class DetalleBuild {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalleBuild;

    // Referencia al producto de la tienda que tienen
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_producto_tienda", nullable = false)
    @JsonBackReference
    private ProductoTienda productoTienda;

    // Referencia a que build pertenece
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_build", nullable = false)
    @JsonBackReference
    private Build build;

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private BigDecimal precioUnitario;
    
    @Column(nullable = false)
    private BigDecimal subTotal;
}
