package com.universidad.compusearch.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.universidad.compusearch.dto.TiendaDashboardResponse;
import com.universidad.compusearch.dto.TiendaDetallesResponse;
import com.universidad.compusearch.dto.TiendaFormRequest;
import com.universidad.compusearch.dto.TiendaInfoDetalleResponse;
import com.universidad.compusearch.dto.TiendaInfoResponse;
import com.universidad.compusearch.entity.EstadoAPI;
import com.universidad.compusearch.entity.Tienda;
import com.universidad.compusearch.entity.TiendaAPI;
import com.universidad.compusearch.exception.TiendaAPIException;
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
    private final RestTemplate restTemplate;

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

    @Transactional
    public Tienda insertarTiendaDirectamente(Tienda tienda) {
        log.info("Insertando tienda con ID {}", tienda.getIdUsuario());

        try {
            String updateUsuario = """
                    UPDATE usuario
                    SET tipo_usuario = 'TIENDA'
                    WHERE id_usuario = :idUsuario
                    """;

            entityManager.createNativeQuery(updateUsuario)
                    .setParameter("idUsuario", tienda.getIdUsuario())
                    .executeUpdate();

            String insertTienda = """
                    INSERT INTO tienda (id_usuario, nombre, telefono, direccion, descripcion, url_pagina, verificado, fecha_afiliacion)
                    VALUES (:idUsuario, :nombre, :telefono, :direccion, :descripcion, :urlPagina, :verificado, CURRENT_TIMESTAMP())
                    """;

            entityManager.createNativeQuery(insertTienda)
                    .setParameter("idUsuario", tienda.getIdUsuario())
                    .setParameter("nombre", tienda.getNombre())
                    .setParameter("telefono", tienda.getTelefono())
                    .setParameter("direccion", tienda.getDireccion())
                    .setParameter("descripcion", tienda.getDescripcion())
                    .setParameter("urlPagina", tienda.getUrlPagina())
                    .setParameter("verificado", tienda.isVerificado())
                    .executeUpdate();

            entityManager.flush();
            entityManager.clear();

            Tienda tiendaPersistida = entityManager.find(Tienda.class, tienda.getIdUsuario());

            if (tiendaPersistida == null) {
                log.error("No se pudo recuperar la tienda con ID {}", tienda.getIdUsuario());
                throw TiendaException.notFound();
            }

            log.info("Tienda con id {} registrada exitosamente", tiendaPersistida.getIdUsuario());

            return tiendaPersistida;

        } catch (DataIntegrityViolationException e) {
            log.warn("Violación de unicidad o integridad de datos al insertar tienda: {}", e.getMessage());
            throw TiendaException.duplicatedData();

        } catch (Exception e) {
            log.error("Error inesperado al insertar tienda: {}", e.getMessage(), e);
            throw TiendaException.errorInsertDirect();
        }
    }

    // Obtener todas las tiendas
    public Page<TiendaInfoResponse> findAllTiendas(Pageable pageable, String nombre) {
        if (nombre != null && !nombre.isBlank()) {
            return tiendaRepository.findByNombreContainingIgnoreCase(nombre, pageable)
                    .map(Mapper::mapToTiendaInfo);
        }
        return tiendaRepository.findAll(pageable)
                .map(Mapper::mapToTiendaInfo);
    }

    public Tienda bucarPorId(Long idTienda) {
        return tiendaRepository.findById(idTienda).orElseThrow(() -> TiendaException.notFound());
    }

    // Obtener tienda por id para el apartado del administrador empleado
    public TiendaDetallesResponse findTiendaByIdForEmpleado(Long idUsuario) {
        return tiendaRepository.findById(idUsuario)
                .map(Mapper::mapToTiendaDetalles)
                .orElseThrow(TiendaException::notFound);
    }

    // Obtener tienda por id para el apartado del administrador tienda
    public TiendaInfoDetalleResponse findTiendaByIdForTienda(Long idUsuario) {
        return tiendaRepository.findById(idUsuario)
                .map(Mapper::mapToTiendaInfoDetalle)
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

    // Actualiza los datos de la tienda
    @Transactional
    public void actualizarDatos(Long idTienda, TiendaFormRequest formulario) {
        Tienda tienda = tiendaRepository.findById(idTienda)
                .orElseThrow(() -> TiendaException.notFound());

        tienda.setDescripcion(formulario.getDescripcion());
        tienda.setTelefono(formulario.getTelefono());
        tienda.setDireccion(formulario.getDireccion());
        tienda.setUrlPagina(formulario.getUrlPagina());
        tienda.setNombre(formulario.getNombre());

        tiendaRepository.save(tienda);
    }

    @Transactional
    public EstadoAPI actualizarApi(Long idTienda, String urlApi) {
        Tienda tienda = tiendaRepository.findById(idTienda)
                .orElseThrow(() -> TiendaException.notFound());

        TiendaAPI api = tienda.getTiendaAPI();

        if (api == null) {
            api = new TiendaAPI();
            api.setTienda(tienda);
        }

        api.setUrlBase(urlApi);
        api.setProbada(false);
        api.setEstadoAPI(EstadoAPI.INACTIVA);

        tienda.setTiendaAPI(api);
        return api.getEstadoAPI();
    }

    @Transactional
    public EstadoAPI probarApi(Long idTienda) {
        Tienda tienda = tiendaRepository.findById(idTienda)
                .orElseThrow(() -> TiendaException.notFound());

        TiendaAPI api = tienda.getTiendaAPI();
        if (api == null) {
            throw TiendaAPIException.notRegistered(idTienda);
        }

        EstadoAPI estado = llamarApiExterna(api);

        api.setEstadoAPI(estado);
        api.setProbada(true);

        if (api.getEstadoAPI() == EstadoAPI.ACTIVA) {
            tienda.setVerificado(true);
        } else {
            tienda.setVerificado(false);
        }

        log.info("Estado de la API: {}", estado.name());
        return estado;
    }

    public EstadoAPI llamarApiExterna(TiendaAPI api) {
        String url = "http://localhost:8081/";

        log.info("Probando la url de la api: {}", url);

        try {
            restTemplate.getForEntity(url, Void.class);
            return EstadoAPI.ACTIVA;

        } catch (HttpClientErrorException | HttpServerErrorException httpError) {
            log.error("Error HTTP de la API externa: {}", httpError.getMessage());
            return EstadoAPI.ERROR;

        } catch (ResourceAccessException connectionError) {
            log.error("Error de conexión (Timeout, I/O): {}", connectionError.getMessage());
            return EstadoAPI.ERROR;

        } catch (Exception e) {
            log.error("Error desconocido al probar la API: {}", e.getMessage());
            return EstadoAPI.ERROR;
        }
    }

    public TiendaAPI buscarApi(Long idTienda) {
        Tienda tienda = tiendaRepository.findById(idTienda)
                .orElseThrow(() -> TiendaException.notFound());

        return tienda.getTiendaAPI();
    }

    @Transactional
    public void actualizarLogo(Long idTienda, byte[] logo) {
        Tienda tienda = tiendaRepository.findById(idTienda)
                .orElseThrow(() -> TiendaException.notFound());

        tienda.setLogo(logo);

        tiendaRepository.save(tienda);
    }

    public TiendaDashboardResponse obtenerDatosTienda(Long idTienda) {
        log.info("Obteniendo datos de dashboard para tienda con id {}", idTienda);

        Tienda tienda = tiendaRepository.findById(idTienda)
                .orElseThrow(() -> TiendaException.notFound());

        log.info("Tienda encontrada devolviendo datos para la tienda {}", tienda.getNombre());
        return Mapper.mapToTiendaDashboardResponse(tienda);
    }
}
