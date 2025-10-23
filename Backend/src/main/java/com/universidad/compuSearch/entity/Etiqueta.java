package com.universidad.compusearch.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

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

// Esta entidad representa las etiquetas
// asociadas a las tiendas
@Entity
@Table(name = "etiqueta")
@Getter
@Setter
@NoArgsConstructor
public class Etiqueta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idEtiqueta;

    @Column(nullable = false, unique = true)
    private String nombre;

    @ManyToMany(mappedBy = "etiquetas", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Tienda> tiendas = new ArrayList<>();
}
