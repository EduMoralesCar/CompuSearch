package com.universidad.compusearch.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.universidad.compusearch.dto.BuildRequest;
import com.universidad.compusearch.dto.BuildsInfoResponse;
import com.universidad.compusearch.dto.DetalleAtributoResponse;
import com.universidad.compusearch.dto.DetalleBuildRequest;
import com.universidad.compusearch.dto.DetalleBuildResponse;
import com.universidad.compusearch.entity.Build;
import com.universidad.compusearch.entity.DetalleBuild;
import com.universidad.compusearch.entity.ProductoTienda;
import com.universidad.compusearch.entity.Usuario;
import com.universidad.compusearch.exception.BuildException;
import com.universidad.compusearch.repository.BuildRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio responsable de gestionar las operaciones relacionadas con las
 * {@link Build} dentro del sistema.
 * <p>
 * Permite crear, actualizar, eliminar y consultar builds asociadas a los
 * usuarios, así como obtener información detallada de cada una de ellas.
 * </p>
 *
 * <p>
 * Cada build representa una configuración de componentes (productos) seleccionados
 * por un usuario, junto con sus atributos y precios.
 * </p>
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BuildService {

    private final BuildRepository buildRepository;
    private final UsuarioService usuarioService;
    private final ProductoTiendaService productoTiendaService;

    /**
     * Crea y guarda una nueva {@link Build} a partir de los datos del request.
     *
     * @param request objeto {@link BuildRequest} con la información de la build y sus detalles.
     * @return la {@link Build} creada y persistida en base de datos.
     * @throws BuildException si ocurre algún error al crear la build.
     */
    public Build crearBuild(BuildRequest request) {
        log.info("Iniciando creación de build para el usuario con ID: {}", request.getIdUsuario());

        Usuario usuario = usuarioService.buscarPorId(request.getIdUsuario());

        Build build = new Build();
        build.setNombre(request.getNombre());
        build.setCompatible(request.isCompatible());
        build.setCostoTotal(request.getCostoTotal());
        build.setConsumoTotal(request.getConsumoTotal());
        build.setUsuario(usuario);

        // Mapeo de los detalles
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
        log.info("Build creada exitosamente con ID: {} para el usuario ID: {}", buildGuardada.getIdBuild(),
                usuario.getIdUsuario());

        return buildGuardada;
    }

    /**
     * Busca una {@link Build} específica por su identificador.
     *
     * @param idBuild ID único de la build.
     * @return la {@link Build} encontrada.
     * @throws BuildException si no se encuentra la build con el ID proporcionado.
     */
    public Build obtenerBuildPorId(Long idBuild) {
        log.info("Buscando build con ID: {}", idBuild);
        return buildRepository.findByIdBuild(idBuild)
                .orElseThrow(() -> {
                    log.warn("No se encontró la build con ID: {}", idBuild);
                    return BuildException.notFound();
                });
    }

    /**
     * Obtiene una {@link Build} junto con su información detallada,
     * incluyendo productos, atributos y totales.
     *
     * @param idBuild ID único de la build.
     * @return un {@link BuildsInfoResponse} con los datos completos de la build.
     * @throws BuildException si no se encuentra la build.
     */
    public BuildsInfoResponse obtenerBuildPorIdConInfo(Long idBuild) {
        log.info("Buscando build con ID: {}", idBuild);

        Build build = buildRepository.findByIdBuild(idBuild)
                .orElseThrow(() -> {
                    log.warn("No se encontró la build con ID: {}", idBuild);
                    return BuildException.notFound();
                });

        BuildsInfoResponse response = new BuildsInfoResponse();
        response.setIdBuild(build.getIdBuild());
        response.setNombre(build.getNombre());
        response.setIdUsuario(build.getUsuario().getIdUsuario());
        response.setCompatible(build.isCompatible());
        response.setCostoTotal(build.getCostoTotal());

        List<DetalleBuildResponse> detalleResponses = build.getDetalles().stream().map(detalle -> {
            DetalleBuildResponse detalleResponse = new DetalleBuildResponse();
            detalleResponse.setIdProductoTienda(detalle.getProductoTienda().getIdProductoTienda());
            detalleResponse.setNombreProducto(detalle.getProductoTienda().getProducto().getNombre());
            detalleResponse.setNombreTienda(detalle.getProductoTienda().getTienda().getNombre());
            detalleResponse.setStock(detalle.getProductoTienda().getStock());
            detalleResponse.setPrecio(detalle.getProductoTienda().getPrecio());
            detalleResponse.setSubTotal(detalle.getSubTotal());
            detalleResponse.setCantidad(detalle.getCantidad());
            detalleResponse.setCategoria(detalle.getProductoTienda().getProducto().getCategoria().getNombre());
            detalleResponse.setUrlProducto(detalle.getProductoTienda().getUrlProducto());

            // Mapear atributos del producto
            List<DetalleAtributoResponse> atributos = detalle.getProductoTienda().getProducto().getAtributos()
                    .stream()
                    .map(attr -> new DetalleAtributoResponse(
                            attr.getAtributo().getNombre(),
                            attr.getValor()))
                    .collect(Collectors.toList());

            detalleResponse.setDetalles(atributos);
            return detalleResponse;
        }).collect(Collectors.toList());

        response.setDetalles(detalleResponses);
        return response;
    }

    /**
     * Elimina una {@link Build} existente a partir de su identificador.
     *
     * @param idBuild ID único de la build a eliminar.
     * @throws BuildException si la build no existe.
     */
    public void eliminarBuild(Long idBuild) {
        log.info("Intentando eliminar build con ID: {}", idBuild);

        Build build = obtenerBuildPorId(idBuild);
        buildRepository.delete(build);

        log.info("Build con ID: {} eliminada correctamente", idBuild);
    }

    /**
     * Actualiza los datos de una {@link Build} existente.
     *
     * @param idBuild ID de la build a actualizar.
     * @param buildRequest objeto {@link BuildRequest} con los nuevos valores.
     * @return la {@link Build} actualizada y guardada en base de datos.
     * @throws BuildException si la build no se encuentra.
     */
    public Build actualizarBuild(Long idBuild, BuildRequest buildRequest) {
        Build buildExistente = buildRepository.findById(idBuild)
                .orElseThrow(() -> BuildException.notFound());

        buildExistente.setNombre(buildRequest.getNombre());
        buildExistente.setCostoTotal(buildRequest.getCostoTotal());
        buildExistente.setConsumoTotal(buildRequest.getConsumoTotal());
        buildExistente.setCompatible(buildRequest.isCompatible());

        // Limpiar y volver a cargar los detalles
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

    /**
     * Obtiene una lista paginada de builds de un usuario, con sus respectivos detalles.
     *
     * @param idUsuario ID del usuario propietario de las builds.
     * @param pageable parámetros de paginación (número de página, tamaño, orden).
     * @return una página de {@link BuildsInfoResponse} con los datos de cada build.
     */
    public Page<BuildsInfoResponse> obtenerBuildsInfoPorUsuario(Long idUsuario, Pageable pageable) {
        log.info("Obteniendo builds con detalles para el usuario con ID: {}", idUsuario);

        Page<Build> buildsPage = buildRepository.findAllByUsuarioId(idUsuario, pageable);

        if (buildsPage.isEmpty()) {
            log.warn("No se encontraron builds para el usuario con ID: {}", idUsuario);
            return Page.empty(pageable);
        }

        return buildsPage.map(build -> {
            BuildsInfoResponse response = new BuildsInfoResponse();
            response.setIdBuild(build.getIdBuild());
            response.setNombre(build.getNombre());
            response.setIdUsuario(build.getUsuario().getIdUsuario());
            response.setCompatible(build.isCompatible());
            response.setCostoTotal(build.getCostoTotal());

            List<DetalleBuildResponse> detalleResponses = build.getDetalles().stream().map(detalle -> {
                DetalleBuildResponse detalleResponse = new DetalleBuildResponse();
                detalleResponse.setIdProductoTienda(detalle.getProductoTienda().getIdProductoTienda());
                detalleResponse.setNombreProducto(detalle.getProductoTienda().getProducto().getNombre());
                detalleResponse.setNombreTienda(detalle.getProductoTienda().getTienda().getNombre());
                detalleResponse.setStock(detalle.getProductoTienda().getStock());
                detalleResponse.setPrecio(detalle.getProductoTienda().getPrecio());
                detalleResponse.setSubTotal(detalle.getSubTotal());
                detalleResponse.setCantidad(detalle.getCantidad());
                detalleResponse.setCategoria(detalle.getProductoTienda().getProducto().getCategoria().getNombre());
                detalleResponse.setUrlProducto(detalle.getProductoTienda().getUrlProducto());

                List<DetalleAtributoResponse> atributos = detalle.getProductoTienda().getProducto().getAtributos()
                        .stream()
                        .map(attr -> new DetalleAtributoResponse(attr.getAtributo().getNombre(), attr.getValor()))
                        .collect(Collectors.toList());

                detalleResponse.setDetalles(atributos);
                return detalleResponse;
            }).collect(Collectors.toList());

            response.setDetalles(detalleResponses);
            return response;
        });
    }

    /**
     * Convierte un {@link DetalleBuildRequest} en una entidad {@link DetalleBuild}.
     *
     * @param request el objeto de solicitud con la información del detalle.
     * @param productoTienda la entidad {@link ProductoTienda} asociada.
     * @param build la {@link Build} a la cual pertenece este detalle.
     * @return una instancia de {@link DetalleBuild} lista para persistirse.
     */
    public static DetalleBuild toEntity(DetalleBuildRequest request, ProductoTienda productoTienda, Build build) {
        DetalleBuild detalle = new DetalleBuild();
        detalle.setProductoTienda(productoTienda);
        detalle.setBuild(build);
        detalle.setCantidad(request.getCantidad());
        detalle.setPrecioUnitario(request.getPrecioUnitario());
        detalle.setSubTotal(request.getSubTotal());
        return detalle;
    }
}
