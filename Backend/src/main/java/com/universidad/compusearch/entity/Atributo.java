package com.universidad.compusearch.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa un atributo o especificación que puede asociarse a uno o más productos.
 * <p>
 * Esta entidad modela los diferentes tipos de características que pueden describir
 * un producto (por ejemplo, "color", "tamaño", "marca", etc.).
 *
 * Relación con otras entidades:
 * <ul>
 *   <li>{@link ProductoAtributo}: relación uno a muchos. Un atributo puede estar asociado
 *       a múltiples instancias de {@code ProductoAtributo}.</li>
 * </ul>
 *
 * Se utilizan las anotaciones de Lombok {@code @Getter}, {@code @Setter} y {@code @NoArgsConstructor}
 * para generar automáticamente los métodos de acceso y el constructor por defecto.
 *
 */

@Entity
@Table(name = "atributo")
@Getter
@Setter
@NoArgsConstructor
public class Atributo {

    /**
     * Identificador único del atributo.
     * Se genera automáticamente mediante una estrategia de incremento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAtributo;

    /**
     * Nombre del atributo. No puede ser nulo ni duplicado.
     * <p>
     * Ejemplo: "Color", "Tamaño", "Procesador".
     * </p>
     */
    @Column(nullable = false, unique = true)
    private String nombre;

    /**
     * Lista de asociaciones entre este atributo y los productos que lo contienen.
     * <p>
     * Se define una relación {@code OneToMany} con la entidad {@link ProductoAtributo},
     * donde el campo {@code atributo} en dicha entidad es el lado propietario.
     * </p>
     *
     * <p>
     * Se utiliza {@code @JsonManagedReference} para evitar ciclos de serialización
     * en relaciones bidireccionales con Jackson.
     * </p>
     */
    @OneToMany(mappedBy = "atributo", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ProductoAtributo> productos = new ArrayList<>();
}
