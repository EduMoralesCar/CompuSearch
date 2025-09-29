package com.universidad.compuSearch.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tienda")
@PrimaryKeyJoinColumn(name = "idUsuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tienda extends Usuario{

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    @Pattern(regexp = "\\d{9,15}", message = "El teléfono debe tener entre 9 y 15 dígitos")
    private String telefono;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    @Pattern(regexp = "\\d{11}", message = "RUC debe tener exactamente 11 dígitos")
    private String ruc;

    @Column(nullable = false)
    private LocalDateTime fechaAfiliacion = LocalDateTime.now();
}
