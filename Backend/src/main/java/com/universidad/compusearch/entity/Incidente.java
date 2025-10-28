package com.universidad.compusearch.entity;

import java.time.LocalDateTime;

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
 * Representa un incidente o reporte generado por un {@link Usuario} dentro del sistema.
 * 
 * Los incidentes permiten registrar problemas, observaciones o sugerencias relacionados
 * con el uso de la plataforma CompuSearch.
 *
 * Relaciones:
 * <ul>
 *   <li>{@link Usuario}: cada incidente pertenece a un usuario específico que lo reportó.</li>
 * </ul>
 *
 * La fecha de creación se genera automáticamente al instanciar el incidente.
 *
 * 
 */
@Entity
@Table(name = "incidente")
@Getter
@Setter
@NoArgsConstructor
public class Incidente {

    /** Identificador único del incidente. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idIncidente;

    /** Título breve que resume el incidente reportado. */
    @Column(nullable = false)
    private String titulo;

    /** Descripción detallada del incidente o problema reportado. */
    @Column(nullable = false)
    private String descripcion;

    /** Fecha y hora en que se creó el incidente. */
    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    /** Estado para saber si el incidente se reviso */
    @Column(nullable = false)
    private boolean revisado = false;

    /** Usuario que reportó el incidente. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonBackReference
    private Usuario usuario;
}
