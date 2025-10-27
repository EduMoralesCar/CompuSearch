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

/**
 * Representa un detalle dentro de una build, el cual contiene la información
 * de un producto incluido en dicha configuración.
 * 
 * Cada instancia de {@code DetalleBuild} asocia un producto específico de la tienda
 * con una build creada por el usuario, registrando la cantidad, el precio unitario
 * y el subtotal correspondiente.
 * 
 *
 * 
 * Relación con otras entidades:
 * <ul>
 *   <li>{@link ProductoTienda}: relación {@code ManyToOne}, indica qué producto de la tienda
 *       está incluido en la build.</li>
 *   <li>{@link Build}: relación {@code ManyToOne}, indica a qué build pertenece este detalle.</li>
 * </ul>
 *
 * <p>
 * Se utilizan las anotaciones de Lombok {@code @Getter}, {@code @Setter} y {@code @NoArgsConstructor}
 * para generar automáticamente los métodos de acceso y el constructor sin argumentos.
 *
 */
@Entity
@Table(name = "detalle_build")
@Getter
@Setter
@NoArgsConstructor
public class DetalleBuild {

    /**
     * Identificador único del detalle de la build.
     * Se genera automáticamente mediante una estrategia de incremento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalleBuild;

    /**
     * Producto de la tienda incluido en esta build.
     * Relación {@code ManyToOne} con la entidad {@link ProductoTienda}.
     * Se utiliza {@code @JsonBackReference} para evitar ciclos de serialización JSON.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_producto_tienda", nullable = false)
    @JsonBackReference
    private ProductoTienda productoTienda;

    /**
     * Build a la que pertenece este detalle.
     * Relación {@code ManyToOne} con la entidad {@link Build}.
     * Se utiliza {@code @JsonBackReference} para manejar la serialización bidireccional.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_build", nullable = false)
    @JsonBackReference
    private Build build;

    /**
     * Cantidad del producto incluido en la build.
     * No puede ser nula ni negativa.
     */
    @Column(nullable = false)
    private int cantidad;

    /**
     * Precio unitario del producto en el momento en que fue agregado a la build.
     */
    @Column(nullable = false)
    private BigDecimal precioUnitario;

    /**
     * Subtotal del producto, calculado como {@code cantidad * precioUnitario}.
     */
    @Column(nullable = false)
    private BigDecimal subTotal;
}
