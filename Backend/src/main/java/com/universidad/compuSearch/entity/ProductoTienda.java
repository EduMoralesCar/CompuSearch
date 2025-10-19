package com.universidad.compusearch.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "producto_tienda")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    @JsonBackReference 
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_tienda", nullable = false)
    @JsonBackReference 
    private Tienda tienda;
}
