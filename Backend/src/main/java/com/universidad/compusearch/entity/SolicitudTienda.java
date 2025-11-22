package com.universidad.compusearch.entity;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.universidad.compusearch.util.MapToJsonConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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

@Entity
@Table(name = "solicitud_tienda")
@Getter
@Setter
@NoArgsConstructor
public class SolicitudTienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idSolicitud;

    @Column(nullable = false)
    private LocalDateTime fechaSolicitud;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado;

    @Convert(converter = MapToJsonConverter.class)
    @Column(columnDefinition = "JSON", nullable = false)
    private Map<String, Object> datosFormulario;

    // Referencia a que usuario pertenece la solicitud
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonBackReference
    private Usuario usuario;

    // Referencia a que empleado reviso la solicitud
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_empleado")
    @JsonBackReference
    private Empleado empleado;
}
