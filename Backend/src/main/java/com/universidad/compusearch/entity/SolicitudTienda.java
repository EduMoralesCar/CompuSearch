package com.universidad.compusearch.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * Entidad que representa una solicitud de afiliación o registro de una tienda
 * en el sistema. 
 * 
 * Cada solicitud está asociada a un {@link Usuario} que la realiza y, de ser
 * aprobada o revisada, puede estar vinculada a un {@link Empleado} que gestiona
 * la evaluación del caso.
 * 
 * 
 * Los datos enviados por el usuario se almacenan en formato JSON dentro del
 * campo {@code datosFormulario}, lo cual permite flexibilidad en la estructura
 * del formulario según el tipo de solicitud.
 * 
 */
@Entity
@Table(name = "solicitud_tienda")
@Getter
@Setter
@NoArgsConstructor
public class SolicitudTienda {

    /** Identificador único de la solicitud. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idSolicitud;

    /** Fecha y hora en que se generó la solicitud. */
    @Column(nullable = false)
    private LocalDateTime fechaSolicitud;

    /** Estado actual de la solicitud (pendiente, aprobada, rechazada, etc.). */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado;

    /** 
     * Contenido del formulario de solicitud en formato JSON.
     * 
     * Ejemplo:
     * <pre>
     * {
     *   "nombreTienda": "TechWorld",
     *   "telefono": "987654321",
     *   "direccion": "Av. Siempre Viva 123",
     *   "descripcion": "Tienda de tecnología y accesorios"
     * }
     * </pre>
     * 
     */
    @Column(columnDefinition = "JSON", nullable = false)
    private String datosFormulario;

    /** Usuario que envió la solicitud. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonBackReference
    private Usuario usuario;

    /** Empleado que gestiona o revisa la solicitud. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_empleado")
    @JsonBackReference
    private Empleado empleado;
}
