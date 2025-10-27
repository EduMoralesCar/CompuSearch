package com.universidad.compusearch.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
 * Representa la relación entre un {@link Producto} y una {@link Tienda},
 * especificando los detalles de comercialización como precio, stock y enlaces.
 * 
 * Esta entidad permite determinar en qué tiendas está disponible un producto
 * y bajo qué condiciones (precio, disponibilidad, enlace directo, etc.).
 *
 * Relaciones:
 * <ul>
 *   <li>{@link Producto}: producto asociado a la tienda.</li>
 *   <li>{@link Tienda}: tienda que ofrece el producto.</li>
 *   <li>{@link DetalleBuild}: detalles de builds que incluyen este producto.</li>
 * </ul>
 *
 * También almacena información adicional proveniente de APIs externas,
 * como el identificador del producto remoto ({@code idProductoApi}).
 *
 * @author Jesus
 * @version 1.0
 */
@Entity
@Table(name = "producto_tienda")
@Getter
@Setter
@NoArgsConstructor
public class ProductoTienda {

    /** Identificador único de la relación producto-tienda. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProductoTienda;

    /** Precio actual del producto en la tienda. */
    @Column(nullable = false)
    private BigDecimal precio;

    /** Cantidad disponible en stock en la tienda. */
    @Column(nullable = false)
    private int stock;

    /** URL del producto en la tienda. */
    @Column(nullable = false)
    private String urlProducto;

    /** URL de la imagen asociada al producto. */
    @Column(nullable = false)
    private String urlImagen;

    /** Indica si el producto está habilitado para su visualización o compra. */
    @Column(nullable = false)
    private Boolean habilitado = true;

    /** Identificador del producto en una API externa, si aplica. */
    @Column(nullable = true)
    private String idProductoApi;

    /** Producto asociado a esta relación. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_producto", nullable = false)
    @JsonBackReference
    private Producto producto;

    /** Tienda que ofrece este producto. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tienda", nullable = false)
    @JsonBackReference
    private Tienda tienda;

    /** Lista de detalles de builds que contienen este producto. */
    @OneToMany(mappedBy = "productoTienda", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DetalleBuild> detalles = new ArrayList<>();
}
