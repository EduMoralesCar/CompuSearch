package com.universidad.compusearch.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.universidad.compusearch.dto.TiendaDetallesResponse;
import com.universidad.compusearch.dto.TiendaInfoResponse;
import com.universidad.compusearch.entity.Tienda;
import com.universidad.compusearch.exception.TiendaException;
import com.universidad.compusearch.repository.TiendaRepository;
import com.universidad.compusearch.util.Mapper;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TiendaService {

    private final TiendaRepository tiendaRepository;
    private final EntityManager entityManager;

    // Obtener tiendas verificadas
    public List<Tienda> obtenerTiendasVerificadas() {
        log.info("Buscando tiendas verificadas en la base de datos...");
        List<Tienda> tiendasVerificadas = tiendaRepository.findByVerificado(true);
        log.info("Se encontraron {} tiendas verificadas.", tiendasVerificadas.size());
        return tiendasVerificadas;
    }

    // Verificar si un usuario es tienda
    public boolean usuarioConTienda(long idUsuario) {
        log.info("Buscando si el usuario con id {} ya es un usuario tienda", idUsuario);
        return tiendaRepository.existsByIdUsuario(idUsuario);
    }

    // Guardar una tienda
    public void guardarTIenda(Tienda tienda) {
        log.info("Guardando tienda {}", tienda.getNombre());
        tiendaRepository.save(tienda);
    }

    // Insertar una tienda por sql
    @Transactional
    public void insertarTiendaDirectamente(Tienda tienda) {
        log.info("Insertando tienda con ID {}", tienda.getIdUsuario());

        String sql = """
                INSERT INTO tienda (id_usuario, nombre, telefono, direccion, descripcion, url_pagina, verificado, fecha_afiliacion)
                VALUES (:idUsuario, :nombre, :telefono, :direccion, :descripcion, :urlPagina, :verificado, CURRENT_TIMESTAMP())
                """;

        entityManager.createNativeQuery(sql)
                .setParameter("idUsuario", tienda.getIdUsuario())
                .setParameter("nombre", tienda.getNombre())
                .setParameter("telefono", tienda.getTelefono())
                .setParameter("direccion", tienda.getDireccion())
                .setParameter("descripcion", tienda.getDescripcion())
                .setParameter("urlPagina", tienda.getUrlPagina())
                .setParameter("verificado", tienda.isVerificado())
                .executeUpdate();

        entityManager.flush();

        log.info("Tienda insertada correctamente");
    }

    // Obtener todas las tiendas
    public Page<TiendaInfoResponse> findAllTiendas(Pageable pageable) {
        return tiendaRepository.findAll(pageable)
                .map(Mapper::mapToTiendaInfo);
    }

    public Tienda bucarPorId(Long idTienda) {
        return tiendaRepository.findById(idTienda).orElseThrow(() -> TiendaException.notFound());
    }

    // Obtener tienda por id para el fronted
    public TiendaDetallesResponse findTiendaById(Long idUsuario) {
        return tiendaRepository.findById(idUsuario)
                .map(Mapper::mapToTiendaDetalles)
                .orElseThrow(TiendaException::notFound);
    }

    // Actualizar estado
    @Transactional
    public TiendaDetallesResponse actualizarEstado(Long idUsuario, boolean activo) {
        Tienda tienda = bucarPorId(idUsuario);
        tienda.setActivo(activo);
        tiendaRepository.save(tienda);
        return Mapper.mapToTiendaDetalles(tienda);
    }

    // Actualziar verificacion
    @Transactional
    public TiendaDetallesResponse actualizarVerificacion(Long idUsuario, boolean verificado) {
        Tienda tienda = bucarPorId(idUsuario);
        tienda.setVerificado(verificado);
        tiendaRepository.save(tienda);
        return Mapper.mapToTiendaDetalles(tienda);
    }
}
