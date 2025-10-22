package com.universidad.compusearch.entity;

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

// Esta entidad representa los detalles de
// una producto
@Entity
@Table(name = "producto_atributo")
@Getter
@Setter
@NoArgsConstructor
public class ProductoAtributo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idProductoAtributo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_producto", nullable = false)
    @JsonBackReference 
    private Producto producto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_atributo", nullable = false)
    @JsonBackReference 
    private Atributo atributo;

    @Column(nullable = false)
    private String valor;
}

