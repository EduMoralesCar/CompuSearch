package com.universidad.compusearch.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
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

    @Column(nullable = false, unique = true)
    private String telefono;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false, unique = true)
    private String ruc;

    @Column(nullable = true)
    private String urlPagina;

    @Column(nullable = true, columnDefinition = "MEDIUMBLOB")
    private byte[] logo;

    @Column(nullable = false)
    private LocalDateTime fechaAfiliacion = LocalDateTime.now();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + getTipoUsuario().name()));
        return authorities;
    }
}