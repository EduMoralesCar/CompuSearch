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
 * Representa una categoría de productos dentro del sistema.
 * 
 * Las categorías permiten organizar los productos en grupos lógicos,
 * como por ejemplo "Tarjetas Gráficas", "Procesadores" o "Monitores".
 * 
 * Relación con otras entidades:
 * <ul>
 *   <li>{@link Producto}: una categoría puede tener múltiples productos asociados
 *       (relación {@code OneToMany}).</li>
 * </ul>
 *
 * Se utilizan las anotaciones de Lombok {@code @Getter}, {@code @Setter}
 * y {@code @NoArgsConstructor} para generar automáticamente los métodos de acceso
 * y el constructor sin argumentos.
 *
 */
@Entity
@Table(name = "categoria")
@Getter
@Setter
@NoArgsConstructor
public class Categoria {

    /**
     * Identificador único de la categoría.
     * Se genera automáticamente mediante una estrategia de incremento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategoria;

    /**
     * Nombre de la categoría. Debe ser único y no nulo.
     * <p>
     * Ejemplo: "Procesadores", "Memorias RAM", "Placas Madre".
     * </p>
     */
    @Column(nullable = false, unique = true)
    private String nombre;

    /**
     * Descripción general de la categoría.
     * Puede incluir detalles sobre el tipo de productos que agrupa.
     */
    @Column(nullable = false)
    private String descripcion;

    /**
     * Nombre del archivo de imagen asociado a la categoría.
     * Generalmente corresponde a una imagen ilustrativa en la interfaz de usuario.
     */
    @Column(nullable = false)
    private String nombreImagen;

    /**
     * Lista de productos que pertenecen a esta categoría.
     * <p>
     * Relación {@code OneToMany} con la entidad {@link Producto}.
     * Se utiliza {@code @JsonManagedReference} para controlar la serialización
     * en relaciones bidireccionales con Jackson.
     * </p>
     */
    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Producto> productos = new ArrayList<>();
}
