package com.universidad.compusearch.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tienda")
@PrimaryKeyJoinColumn(name = "idUsuario")
@Getter
@Setter
@NoArgsConstructor
public class Tienda extends Usuario {

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String telefono;

    @Column(nullable = true)
    private String direccion;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = true)
    private String urlPagina;

    @Column(nullable = true, columnDefinition = "MEDIUMBLOB")
    private byte[] logo;

    @Column(nullable = false)
    private LocalDateTime fechaAfiliacion = LocalDateTime.now();

    @Column(nullable = false)
    private boolean verificado;

    // Referencia a la lista de suscripciones de una tienda
    @OneToMany(mappedBy = "tienda", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Suscripcion> suscripciones = new ArrayList<>();

    // Referencia a la api de la tienda 
    @OneToOne(mappedBy = "tienda", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private TiendaAPI tiendaAPI;

    // Referencias a las etiquetas que usa la tienda
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tienda_etiqueta", joinColumns = @JoinColumn(name = "tienda_id", referencedColumnName = "idUsuario"), inverseJoinColumns = @JoinColumn(name = "etiqueta_id", referencedColumnName = "idEtiqueta"))
    @JsonManagedReference
    private List<Etiqueta> etiquetas = new ArrayList<>();

    // Referencia a los productos que tiene la tienda
    @OneToMany(mappedBy = "tienda", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProductoTienda> productos = new ArrayList<>();

    // Roles de la tienda
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + getTipoUsuario().name()));
        return authorities;
    }
}
