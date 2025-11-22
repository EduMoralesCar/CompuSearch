package com.universidad.compusearch.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.dto.BuildRequest;
import com.universidad.compusearch.dto.BuildsInfoResponse;
import com.universidad.compusearch.dto.DetalleBuildRequest;
import com.universidad.compusearch.dto.DetalleBuildResponse;
import com.universidad.compusearch.entity.Build;
import com.universidad.compusearch.entity.DetalleBuild;
import com.universidad.compusearch.entity.ProductoTienda;
import com.universidad.compusearch.entity.Usuario;
import com.universidad.compusearch.exception.BuildException;
import com.universidad.compusearch.repository.BuildRepository;
import com.universidad.compusearch.util.Mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BuildService {

    private final BuildRepository buildRepository;
    private final UsuarioService usuarioService;
    private final ProductoTiendaService productoTiendaService;

    // Crear build
    public Build crearBuild(BuildRequest request) {
        log.info("Iniciando creación de build para el usuario con ID: {}", request.getIdUsuario());

        Usuario usuario = usuarioService.buscarPorId(request.getIdUsuario());

        Build build = new Build();
        build.setNombre(request.getNombre());
        build.setCompatible(request.isCompatible());
        build.setCostoTotal(request.getCostoTotal());
        build.setConsumoTotal(request.getConsumoTotal());
        build.setUsuario(usuario);

        List<DetalleBuild> detalles = new ArrayList<>(
                request.getDetalles().stream().map(det -> {
                    DetalleBuild detalle = new DetalleBuild();
                    ProductoTienda productoTienda = new ProductoTienda();
                    productoTienda.setIdProductoTienda(det.getIdProductoTienda());

                    detalle.setProductoTienda(productoTienda);
                    detalle.setCantidad(det.getCantidad());
                    detalle.setPrecioUnitario(det.getPrecioUnitario());
                    detalle.setSubTotal(det.getSubTotal());
                    detalle.setBuild(build);

                    return detalle;
                }).toList());

        build.setDetalles(detalles);

        Build buildGuardada = buildRepository.save(build);
        log.info("Build creada exitosamente con ID: {} para el usuario ID: {}", buildGuardada.getIdBuild(),
                usuario.getIdUsuario());

        return buildGuardada;
    }

    // Obtener build por id
    public Build obtenerBuildPorId(Long idBuild) {
        log.info("Buscando build con ID: {}", idBuild);
        return buildRepository.findById(idBuild)
                .orElseThrow(() -> {
                    log.warn("No se encontró la build con ID: {}", idBuild);
                    return BuildException.notFound();
                });
    }

    // Obtener informacion de build por id
    public BuildsInfoResponse obtenerBuildPorIdConInfo(Long idBuild) {
        log.info("Buscando build con ID: {}", idBuild);

        Build build = buildRepository.findById(idBuild)
                .orElseThrow(() -> {
                    log.warn("No se encontró la build con ID: {}", idBuild);
                    return BuildException.notFound();
                });

        BuildsInfoResponse response = Mapper.mapToBuildsInfo(build);

        List<DetalleBuildResponse> detalleResponse = Mapper.mapToDetalleBuild(build.getDetalles());

        response.setDetalles(detalleResponse);
        return response;
    }

    // Eliminar build por id
    public void eliminarBuild(Long idBuild) {
        log.info("Intentando eliminar build con ID: {}", idBuild);

        Build build = obtenerBuildPorId(idBuild);
        buildRepository.delete(build);

        log.info("Build con ID: {} eliminada correctamente", idBuild);
    }

    // Actualizar build por id
    public Build actualizarBuild(Long idBuild, BuildRequest buildRequest) {
        Build buildExistente = buildRepository.findById(idBuild)
                .orElseThrow(() -> BuildException.notFound());

        buildExistente.setNombre(buildRequest.getNombre());
        buildExistente.setCostoTotal(buildRequest.getCostoTotal());
        buildExistente.setConsumoTotal(buildRequest.getConsumoTotal());
        buildExistente.setCompatible(buildRequest.isCompatible());

        buildExistente.getDetalles().clear();

        buildRequest.getDetalles().forEach(detalleReq -> {
            ProductoTienda productoTienda = productoTiendaService.obtenerPorId(detalleReq.getIdProductoTienda());

            DetalleBuild detalle = new DetalleBuild();
            detalle.setBuild(buildExistente);
            detalle.setProductoTienda(productoTienda);
            detalle.setCantidad(detalleReq.getCantidad());
            detalle.setPrecioUnitario(detalleReq.getPrecioUnitario());
            detalle.setSubTotal(detalleReq.getSubTotal());

            buildExistente.getDetalles().add(detalle);
        });

        return buildRepository.save(buildExistente);
    }

    // Obtener informacion de las builds por usuario
    public Page<BuildsInfoResponse> obtenerBuildsInfoPorUsuario(Long idUsuario, Pageable pageable) {
        log.info("Obteniendo builds con detalles para el usuario con ID: {}", idUsuario);

        Page<Build> buildsPage = buildRepository.findAllByUsuarioId(idUsuario, pageable);

        if (buildsPage.isEmpty()) {
            log.warn("No se encontraron builds para el usuario con ID: {}", idUsuario);
            return Page.empty(pageable);
        }

        return buildsPage.map(build -> {
            // Usar tu mapper para la info principal
            BuildsInfoResponse response = Mapper.mapToBuildsInfo(build);

            // Usar tu mapper para los detalles
            List<DetalleBuildResponse> detalleResponses = Mapper.mapToDetalleBuild(build.getDetalles());

            response.setDetalles(detalleResponses);

            return response;
        });
    }

    // Metodo auxiliar para convetir un request a detalle build
    public static DetalleBuild requestABuild(DetalleBuildRequest request, ProductoTienda productoTienda, Build build) {
        DetalleBuild detalle = new DetalleBuild();
        detalle.setProductoTienda(productoTienda);
        detalle.setBuild(build);
        detalle.setCantidad(request.getCantidad());
        detalle.setPrecioUnitario(request.getPrecioUnitario());
        detalle.setSubTotal(request.getSubTotal());
        return detalle;
    }
}
