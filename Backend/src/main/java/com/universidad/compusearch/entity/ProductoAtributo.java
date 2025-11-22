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

/**
 * Representa la relación entre un {@link Producto} y un {@link Atributo},
 * permitiendo asociar valores específicos a cada característica del producto.
 * 
 * Por ejemplo, un producto con el atributo "Memoria RAM" puede tener el valor "16 GB".
 *
 * Relaciones:
 * <ul>
 *   <li>{@link Producto}: producto al que pertenece el atributo.</li>
 *   <li>{@link Atributo}: tipo de atributo o especificación asociada al producto.</li>
 * </ul>
 *
 * Esta entidad forma parte de una relación muchos a muchos entre {@code Producto}
 * y {@code Atributo}, con un campo adicional {@code valor} que describe el detalle
 * específico para ese producto.
 *
 */
@Entity
@Table(name = "producto_atributo")
@Getter
@Setter
@NoArgsConstructor
public class ProductoAtributo {

    /** Identificador único del producto-atributo. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idProductoAtributo;

    /** Producto al que pertenece este atributo. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_producto", nullable = false)
    @JsonBackReference 
    private Producto producto;

    /** Atributo o característica asociada al producto. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_atributo", nullable = false)
    @JsonBackReference 
    private Atributo atributo;

    /** Valor específico del atributo para el producto. */
    @Column(nullable = false)
    private String valor;
}
