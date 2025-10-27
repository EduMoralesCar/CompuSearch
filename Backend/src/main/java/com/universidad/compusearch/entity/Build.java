package com.universidad.compusearch.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

/**
 * Representa una build (ensamblaje o configuración) de componentes generada por un usuario.
 * 
 * Una build puede considerarse como un conjunto de componentes o productos seleccionados
 * por el usuario para formar un sistema completo (por ejemplo, una PC personalizada).
 * 
 * Relación con otras entidades:
 * <ul>
 *   <li>{@link Usuario}: cada build pertenece a un único usuario (relación {@code ManyToOne}).</li>
 *   <li>{@link DetalleBuild}: una build puede contener múltiples detalles de componentes
 *       asociados (relación {@code OneToMany}).</li>
 * </ul>
 * 
 * Se utilizan las anotaciones de Lombok {@code @Getter}, {@code @Setter} y {@code @NoArgsConstructor}
 * para generar automáticamente los métodos de acceso y el constructor sin argumentos.
 *
 */
@Entity
@Table(name = "build")
@Getter
@Setter
@NoArgsConstructor
public class Build {

    /**
     * Identificador único de la build.
     * Se genera automáticamente mediante una estrategia de incremento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idBuild;

    /**
     * Nombre asignado por el usuario a la build.
     * No puede ser nulo.
     */
    @Column(nullable = false)
    private String nombre;

    /**
     * Fecha y hora en que la build fue creada.
     * Se inicializa automáticamente con la fecha y hora actual.
     */
    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    /**
     * Indica si la build es compatible en términos de componentes seleccionados.
     * {@code true} si todos los componentes son compatibles, de lo contrario {@code false}.
     */
    @Column(nullable = false)
    private boolean compatible;

    /**
     * Costo total de todos los componentes incluidos en la build.
     * Representado como un {@link BigDecimal} para mantener precisión monetaria.
     */
    @Column(nullable = false)
    private BigDecimal costoTotal;

    /**
     * Consumo energético total de la build (por ejemplo, "450W").
     * No puede ser nulo.
     */
    @Column(nullable = false)
    private String consumoTotal;

    /**
     * Usuario propietario de la build.
     * <p>
     * Relación {@code ManyToOne} con la entidad {@link Usuario}.
     * Se utiliza {@code @JsonBackReference} para evitar recursividad
     * en la serialización JSON bidireccional.
     * </p>
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonBackReference
    private Usuario usuario;

    /**
     * Lista de detalles que conforman la build.
     * <p>
     * Cada {@link DetalleBuild} representa un componente o producto incluido.
     * La relación es {@code OneToMany} y está configurada con {@code CascadeType.ALL}
     * para propagar operaciones de persistencia, y {@code orphanRemoval = true}
     * para eliminar los detalles huérfanos.
     * </p>
     *
     * <p>
     * Se usa {@code @JsonManagedReference} para gestionar la serialización
     * en relaciones bidireccionales con Jackson.
     * </p>
     */
    @OneToMany(mappedBy = "build", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<DetalleBuild> detalles = new ArrayList<>();
}
