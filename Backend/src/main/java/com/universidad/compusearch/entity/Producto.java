package com.universidad.compusearch.entity;

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

/**
 * Representa un producto dentro del sistema CompuSearch.
 * 
 * Los productos contienen información detallada como su nombre, marca, modelo y descripción,
 * y se asocian con una {@link Categoria}. Además, pueden tener varios atributos técnicos
 * y estar disponibles en distintas tiendas.
 *
 * Relaciones:
 * <ul>
 *   <li>{@link Categoria}: cada producto pertenece a una categoría específica.</li>
 *   <li>{@link ProductoAtributo}: define los atributos técnicos asociados al producto.</li>
 *   <li>{@link ProductoTienda}: representa la disponibilidad del producto en diferentes tiendas.</li>
 * </ul>
 *
 */
@Entity
@Table(name = "producto")
@Getter
@Setter
@NoArgsConstructor
public class Producto {

    /** Identificador único del producto. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

    /** Nombre del producto. */
    @Column(nullable = false)
    private String nombre;

    /** Marca del producto. */
    @Column(nullable = false)
    private String marca;

    /** Modelo del producto. */
    @Column(nullable = false)
    private String modelo;

    /** Descripción detallada del producto. */
    @Column(nullable = false)
    private String descripcion;

    /** Categoría a la que pertenece el producto. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    @JsonBackReference
    private Categoria categoria;

    /** Lista de atributos técnicos asociados al producto. */
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference 
    private List<ProductoAtributo> atributos;

    /** Lista de relaciones que indican en qué tiendas está disponible el producto. */
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference 
    private List<ProductoTienda> tiendas;
}
