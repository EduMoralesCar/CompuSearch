package com.universidad.compusearch.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "producto_tienda")
@Getter
@Setter
@NoArgsConstructor
public class ProductoTienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProductoTienda;

    @Column(nullable = false)
    private BigDecimal precio;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private String urlProducto;

    @Column(nullable = false)
    private String urlImagen;

    @Column(nullable = false)
    private Boolean habilitado = true;

    @Column(nullable = true)
    private String idProductoApi;

    // Referencia al producto
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_producto", nullable = false)
    @JsonBackReference
    private Producto producto;

    // Referencia a que tienda pertenece
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tienda", nullable = false)
    @JsonBackReference
    private Tienda tienda;

    // Referencia a que builds tienen estos productos en sus detalles
    @OneToMany(mappedBy = "productoTienda", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<DetalleBuild> detalles = new ArrayList<>();

    // Referencia las metricas de ese producto
    @OneToMany(mappedBy = "productoTienda", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Metrica> metricas = new ArrayList<>();

}
