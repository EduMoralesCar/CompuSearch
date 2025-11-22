package com.universidad.compusearch.entity;

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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa una etiqueta asociada a una o varias tiendas.
 * 
 * Las etiquetas permiten clasificar o agrupar las tiendas según características
 * específicas, como su tipo de productos, ubicación o especialización.
 *
 * Relación con otras entidades:
 * <ul>
 *   <li>{@link Tienda}: relación {@code ManyToMany}, ya que una etiqueta puede aplicarse
 *       a múltiples tiendas y una tienda puede tener varias etiquetas.</li>
 * </ul>
 *
 * Se utilizan las anotaciones de Lombok {@code @Getter}, {@code @Setter}
 * y {@code @NoArgsConstructor} para generar automáticamente los métodos de acceso
 * y el constructor por defecto.
 * 
 */
@Entity
@Table(name = "etiqueta")
@Getter
@Setter
@NoArgsConstructor
public class Etiqueta {

    /**
     * Identificador único de la etiqueta.
     * Se genera automáticamente mediante una estrategia de incremento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idEtiqueta;

    /**
     * Nombre de la etiqueta. Debe ser único y no nulo.
     * Ejemplo: "Oficial", "Distribuidor Autorizado", "Gamer", "Outlet".
     */
    @Column(nullable = false, unique = true)
    private String nombre;

    /**
     * Lista de tiendas asociadas a esta etiqueta.
     * Relación {@code ManyToMany} con la entidad {@link Tienda}, donde el campo
     * {@code etiquetas} en {@link Tienda} es el lado propietario.
     * Se usa {@code @JsonBackReference} para evitar recursividad en la serialización JSON.
     */
    @ManyToMany(mappedBy = "etiquetas", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<Tienda> tiendas = new ArrayList<>();
}
