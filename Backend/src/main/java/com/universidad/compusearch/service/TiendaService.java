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
        log.info("Comprobando si el usuario con ID {} ya es una tienda...", idUsuario);
        boolean existe = tiendaRepository.existsByIdUsuario(idUsuario);
        log.info("Usuario con ID {} {} es tienda", idUsuario, existe ? "sí" : "no");
        return existe;
    }

    // Guardar tienda
    public void guardarTienda(Tienda tienda) {
        log.info("Guardando tienda '{}'", tienda.getNombre());
        tiendaRepository.save(tienda);
        log.info("Tienda '{}' guardada correctamente", tienda.getNombre());
    }

    @Transactional
    public Tienda insertarTiendaDirectamente(Tienda tienda) {
        log.info("Insertando tienda con ID {}", tienda.getIdUsuario());

        try {
            // Actualizar tipo de usuario
            entityManager.createNativeQuery("""
                    UPDATE usuario
                    SET tipo_usuario = 'TIENDA'
                    WHERE id_usuario = :idUsuario
                    """)
                    .setParameter("idUsuario", tienda.getIdUsuario())
                    .executeUpdate();
            log.info("Usuario con ID {} actualizado a tipo TIENDA", tienda.getIdUsuario());

            // Insertar tienda
            entityManager
                    .createNativeQuery(
                            """
                                    INSERT INTO tienda (id_usuario, nombre, telefono, direccion, descripcion, url_pagina, verificado, fecha_afiliacion)
                                    VALUES (:idUsuario, :nombre, :telefono, :direccion, :descripcion, :urlPagina, :verificado, CURRENT_TIMESTAMP())
                                    """)
                    .setParameter("idUsuario", tienda.getIdUsuario())
                    .setParameter("nombre", tienda.getNombre())
                    .setParameter("telefono", tienda.getTelefono())
                    .setParameter("direccion", tienda.getDireccion())
                    .setParameter("descripcion", tienda.getDescripcion())
                    .setParameter("urlPagina", tienda.getUrlPagina())
                    .setParameter("verificado", tienda.isVerificado())
                    .executeUpdate();
            log.info("Tienda '{}' insertada correctamente", tienda.getNombre());

            entityManager.flush();
            entityManager.clear();

            Tienda tiendaPersistida = entityManager.find(Tienda.class, tienda.getIdUsuario());
            if (tiendaPersistida == null) {
                log.error("No se pudo recuperar la tienda con ID {}", tienda.getIdUsuario());
                throw TiendaException.notFound();
            }

            log.info("Tienda con ID {} registrada exitosamente", tiendaPersistida.getIdUsuario());
            return tiendaPersistida;

        } catch (DataIntegrityViolationException e) {
            log.warn("Violación de integridad de datos al insertar tienda '{}': {}", tienda.getNombre(),
                    e.getMessage());
            throw TiendaException.duplicatedData();

        } catch (Exception e) {
            log.error("Error inesperado al insertar tienda '{}': {}", tienda.getNombre(), e.getMessage(), e);
            throw TiendaException.errorInsertDirect();
        }
    }

    public Page<TiendaInfoResponse> findAllTiendas(Pageable pageable, String nombre) {
        log.info("Obteniendo listado de tiendas con filtro nombre: '{}'", nombre);
        Page<TiendaInfoResponse> result;
        if (nombre != null && !nombre.isBlank()) {
            result = tiendaRepository.findByNombreContainingIgnoreCase(nombre, pageable)
                    .map(Mapper::mapToTiendaInfo);
        } else {
            result = tiendaRepository.findAll(pageable)
                    .map(Mapper::mapToTiendaInfo);
        }
        log.info("Se retornaron {} registros de tiendas", result.getNumberOfElements());
        return result;
    }

    public Tienda bucarPorId(Long idTienda) {
        log.info("Buscando tienda con ID {}", idTienda);
        return tiendaRepository.findById(idTienda).orElseThrow(() -> {
            log.error("Tienda con ID {} no encontrada", idTienda);
            return TiendaException.notFound();
        });
    }

    @Transactional
    public TiendaDetallesResponse actualizarEstado(Long idUsuario, boolean activo) {
        log.info("Actualizando estado de la tienda ID {} a {}", idUsuario, activo);
        Tienda tienda = bucarPorId(idUsuario);
        tienda.setActivo(activo);
        tiendaRepository.save(tienda);
        log.info("Estado actualizado correctamente para la tienda ID {}", idUsuario);
        return Mapper.mapToTiendaDetalles(tienda);
    }

    @Transactional
    public TiendaDetallesResponse actualizarVerificacion(Long idUsuario, boolean verificado) {
        log.info("Actualizando verificación de la tienda ID {} a {}", idUsuario, verificado);
        Tienda tienda = bucarPorId(idUsuario);
        tienda.setVerificado(verificado);
        tiendaRepository.save(tienda);
        log.info("Verificación actualizada correctamente para la tienda ID {}", idUsuario);
        return Mapper.mapToTiendaDetalles(tienda);
    }

    @Transactional
    public void actualizarDatos(Long idTienda, TiendaFormRequest formulario) {
        log.info("Actualizando datos de la tienda ID {}", idTienda);
        Tienda tienda = tiendaRepository.findById(idTienda)
                .orElseThrow(() -> {
                    log.error("Tienda con ID {} no encontrada para actualizar datos", idTienda);
                    return TiendaException.notFound();
                });

        tienda.setDescripcion(formulario.getDescripcion());
        tienda.setTelefono(formulario.getTelefono());
        tienda.setDireccion(formulario.getDireccion());
        tienda.setUrlPagina(formulario.getUrlPagina());
        tienda.setNombre(formulario.getNombre());

        tiendaRepository.save(tienda);
        log.info("Datos de la tienda ID {} actualizados correctamente", idTienda);
    }

    @Transactional
    public EstadoAPI actualizarApi(Long idTienda, String urlApi) {
        log.info("Actualizando API de la tienda ID {} con URL {}", idTienda, urlApi);
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
        log.info("API actualizada para la tienda ID {}", idTienda);
        return api.getEstadoAPI();
    }

    @Transactional
    public EstadoAPI probarApi(Long idTienda) {
        log.info("Probando API de la tienda ID {}", idTienda);
        Tienda tienda = tiendaRepository.findById(idTienda)
                .orElseThrow(() -> TiendaException.notFound());

        TiendaAPI api = tienda.getTiendaAPI();
        if (api == null) {
            log.error("La tienda ID {} no tiene API registrada", idTienda);
            throw TiendaAPIException.notRegistered(idTienda);
        }

        EstadoAPI estado = llamarApiExterna(api);

        api.setEstadoAPI(estado);
        api.setProbada(true);

        tienda.setVerificado(estado == EstadoAPI.ACTIVA);

        log.info("Resultado prueba API tienda ID {}: {}", idTienda, estado.name());
        return estado;
    }

    public EstadoAPI llamarApiExterna(TiendaAPI api) {
        String url = "http://host.docker.internal:8081/";
        log.info("Llamando API externa: {}", url);

        try {
            restTemplate.getForEntity(url, Void.class);
            log.info("API externa respondio correctamente");
            return EstadoAPI.ACTIVA;

        } catch (HttpClientErrorException | HttpServerErrorException httpError) {
            log.error("Error HTTP al llamar API externa: {}", httpError.getMessage(), httpError);
            return EstadoAPI.ERROR;

        } catch (ResourceAccessException connectionError) {
            log.error("Error de conexión al llamar API externa: {}", connectionError.getMessage(), connectionError);
            return EstadoAPI.ERROR;

        } catch (Exception e) {
            log.error("Error desconocido al llamar API externa: {}", e.getMessage(), e);
            return EstadoAPI.ERROR;
        }
    }

    public TiendaAPI buscarApi(Long idTienda) {
        log.info("Buscando API de la tienda ID {}", idTienda);
        Tienda tienda = tiendaRepository.findById(idTienda)
                .orElseThrow(() -> {
                    log.error("Tienda con ID {} no encontrada para buscar API", idTienda);
                    return TiendaException.notFound();
                });
        return tienda.getTiendaAPI();
    }

    @Transactional
    public void actualizarLogo(Long idTienda, byte[] logo) {
        log.info("Actualizando logo de la tienda ID {}", idTienda);
        Tienda tienda = tiendaRepository.findById(idTienda)
                .orElseThrow(() -> {
                    log.error("Tienda con ID {} no encontrada para actualizar logo", idTienda);
                    return TiendaException.notFound();
                });
        tienda.setLogo(logo);
        tiendaRepository.save(tienda);
        log.info("Logo actualizado correctamente para la tienda ID {}", idTienda);
    }

    public TiendaDashboardResponse obtenerDatosTienda(Long idTienda) {
        log.info("Obteniendo datos de dashboard para tienda ID {}", idTienda);
        Tienda tienda = tiendaRepository.findById(idTienda)
                .orElseThrow(() -> {
                    log.error("Tienda con ID {} no encontrada para dashboard", idTienda);
                    return TiendaException.notFound();
                });
        log.info("Datos de dashboard obtenidos correctamente para la tienda '{}'", tienda.getNombre());
        return Mapper.mapToTiendaDashboardResponse(tienda);
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
}
