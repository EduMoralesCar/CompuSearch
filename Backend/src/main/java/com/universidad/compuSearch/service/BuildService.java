package com.universidad.compusearch.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.universidad.compusearch.dto.BuildRequest;
import com.universidad.compusearch.entity.Build;
import com.universidad.compusearch.entity.DetalleBuild;
import com.universidad.compusearch.entity.ProductoTienda;
import com.universidad.compusearch.entity.Usuario;
import com.universidad.compusearch.exception.BuildException;
import com.universidad.compusearch.repository.BuildRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Servicio de builds
@Service
@RequiredArgsConstructor
@Slf4j
public class BuildService {

    private final BuildRepository buildRepository;
    private final UsuarioService usuarioService;

    // Creaa y guarda la build
    public Build crearBuild(BuildRequest request) {
        log.info("Iniciando creación de build para el usuario con ID: {}", request.getIdUsuario());

        Usuario usuario = usuarioService.buscarPorId(request.getIdUsuario());

        Build build = new Build();
        build.setNombre(request.getNombre());
        build.setCompatible(request.isCompatible());
        build.setCostoTotal(request.getCostoTotal());
        build.setUsuario(usuario);

        List<DetalleBuild> detalles = Lists.newArrayList(
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
        log.info("Build creada exitosamente con ID: {} para el usuario ID: {}",
                buildGuardada.getIdBuild(), usuario.getIdUsuario());

        return buildGuardada;
    }

    // Obtiene la build por id
    public Build obtenerBuildPorId(Long idBuild) {
        log.info("Buscando build con ID: {}", idBuild);

        return buildRepository.findByIdBuild(idBuild)
                .orElseThrow(() -> {
                    log.warn("No se encontró la build con ID: {}", idBuild);
                    return BuildException.notFound();
                });
    }

    // Obtiene todas las buidls del usuario
    public List<Build> obtenerBuildsPorUsuario(Long idUsuario) {
        log.info("Obteniendo builds del usuario con ID: {}", idUsuario);
        List<Build> builds = buildRepository.findAllByUsuarioId(idUsuario);
        log.info("Se encontraron {} builds para el usuario ID: {}", builds.size(), idUsuario);
        return builds;
    }

    // Elimina una build del usuario
    public void eliminarBuild(Long idBuild) {
        log.info("Intentando eliminar build con ID: {}", idBuild);

        Build build = obtenerBuildPorId(idBuild);
        buildRepository.delete(build);

        log.info("Build con ID: {} eliminada correctamente", idBuild);
    }
}
