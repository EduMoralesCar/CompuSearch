package com.universidad.compusearch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "etiqueta")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Etiqueta {

    @Id
    private long idEtiqueta;

    @Column(nullable = false, unique = true)
    private String nombre;
}

