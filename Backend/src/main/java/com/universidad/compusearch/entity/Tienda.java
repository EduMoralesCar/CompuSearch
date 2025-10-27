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
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa una tienda afiliada al sistema.
 * 
 * Cada tienda es un tipo de {@link Usuario} con información adicional
 * sobre su negocio, incluyendo datos de contacto, descripción, logo,
 * etiquetas asociadas y los productos que ofrece.
 * 
 *
 * 
 * Además, esta entidad implementa integración con Spring Security,
 * otorgando automáticamente el rol {@code ROLE_TIENDA}.
 * 
 */
@Entity
@Table(name = "tienda")
@PrimaryKeyJoinColumn(name = "idUsuario")
@Getter
@Setter
@NoArgsConstructor
public class Tienda extends Usuario {

    /** Nombre comercial de la tienda. */
    @Column(nullable = false)
    private String nombre;

    /** Teléfono de contacto único de la tienda. */
    @Column(nullable = false, unique = true)
    private String telefono;

    /** Dirección física o principal del negocio. */
    @Column(nullable = true)
    private String direccion;

    /** Descripción general de la tienda. */
    @Column(nullable = false)
    private String descripcion;

    /** URL opcional del sitio web de la tienda. */
    @Column(nullable = true)
    private String urlPagina;

    /** Logo de la tienda almacenado como arreglo de bytes (imagen binaria). */
    @Column(nullable = true, columnDefinition = "MEDIUMBLOB")
    private byte[] logo;

    /** Fecha en la que la tienda se afilió al sistema. */
    @Column(nullable = false)
    private LocalDateTime fechaAfiliacion = LocalDateTime.now();

    /** Indica si la tienda ha sido verificada sus datos. */
    @Column(nullable = false)
    private boolean verificado;

    /**
     * Etiquetas o categorías que describen los tipos de productos o servicios
     * que ofrece la tienda.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tienda_etiqueta",
        joinColumns = @JoinColumn(name = "tienda_id", referencedColumnName = "idUsuario"),
        inverseJoinColumns = @JoinColumn(name = "etiqueta_id", referencedColumnName = "idEtiqueta")
    )
    @JsonManagedReference
    private List<Etiqueta> etiquetas = new ArrayList<>();

    /**
     * Lista de productos que la tienda ofrece, con su respectiva información
     * de precio, stock y enlaces externos.
     */
    @OneToMany(mappedBy = "tienda", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProductoTienda> productos = new ArrayList<>();

    /**
     * Retorna las autoridades (roles) de la tienda para el contexto de
     * autenticación de Spring Security.
     *
     * @return una colección con el rol {@code ROLE_TIENDA}.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + getTipoUsuario().name()));
        return authorities;
    }
}
