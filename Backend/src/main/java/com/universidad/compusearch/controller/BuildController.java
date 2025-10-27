package com.universidad.compusearch.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.dto.BuildRequest;
import com.universidad.compusearch.dto.BuildsInfoResponse;
import com.universidad.compusearch.dto.ProductoBuildResponse;
import com.universidad.compusearch.entity.Build;
import com.universidad.compusearch.service.BuildExportService;
import com.universidad.compusearch.service.BuildService;
import com.universidad.compusearch.service.ProductoTiendaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST encargado de gestionar las operaciones relacionadas con las Builds de los usuarios.
 * <p>
 * Este controlador proporciona endpoints para:
 * <ul>
 *   <li>Crear, obtener, actualizar y eliminar builds</li>
 *   <li>Exportar builds a un archivo Excel</li>
 *   <li>Listar productos disponibles para la creación de builds</li>
 * </ul>
 *
 * <p>Utiliza los servicios {@link BuildService}, {@link BuildExportService} y {@link ProductoTiendaService}
 * para realizar las operaciones de negocio correspondientes.</p>
 * 
 */
@RestController
@RequestMapping("/builds")
@RequiredArgsConstructor
@Slf4j
public class BuildController {

    /** Servicio principal para la gestión de builds. */
    private final BuildService buildService;

    /** Servicio encargado de exportar builds a formato Excel. */
    private final BuildExportService buildExportService;

    /** Servicio que gestiona la obtención de productos de tiendas para builds. */
    private final ProductoTiendaService productoTiendaService;

    /**
     * Crea una nueva build a partir de la información proporcionada.
     *
     * @param buildRequest objeto {@link BuildRequest} con los datos necesarios para crear la build.
     * @return una {@link ResponseEntity} con la build creada.
     */
    @PostMapping
    public ResponseEntity<Build> crearBuild(@RequestBody BuildRequest buildRequest) {
        log.info("Request completo: {}", buildRequest);

        log.info("Petición recibida para crear una nueva Build. Usuario ID: {}, Nombre Build: {}",
                buildRequest.getIdUsuario(), buildRequest.getNombre());

        Build nuevaBuild = buildService.crearBuild(buildRequest);
        log.info("Build creada exitosamente con ID: {} para Usuario ID: {}",
                nuevaBuild.getIdBuild(), nuevaBuild.getUsuario().getIdUsuario());

        return ResponseEntity.ok(nuevaBuild);
    }

    /**
     * Obtiene una build específica por su identificador.
     *
     * @param idBuild identificador de la build.
     * @return una {@link ResponseEntity} con los detalles de la build en {@link BuildsInfoResponse}.
     */
    @GetMapping("/{idBuild}")
    public ResponseEntity<BuildsInfoResponse> obtenerBuildPorId(@PathVariable Long idBuild) {
        log.info("Buscando build con ID: {}", idBuild);

        BuildsInfoResponse build = buildService.obtenerBuildPorIdConInfo(idBuild);

        log.info("Build encontrada con ID: {}", build.getIdBuild());
        return ResponseEntity.ok(build);
    }

    /**
     * Obtiene una lista paginada de builds asociadas a un usuario.
     *
     * @param idUsuario identificador del usuario.
     * @param page número de página (por defecto 0).
     * @param size cantidad de elementos por página (por defecto 5).
     * @return una {@link ResponseEntity} con una página de {@link BuildsInfoResponse}.
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Page<BuildsInfoResponse>> obtenerBuilds(
            @PathVariable Long idUsuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("idBuild").descending());
        Page<BuildsInfoResponse> buildsPage = buildService.obtenerBuildsInfoPorUsuario(idUsuario, pageable);
        return ResponseEntity.ok(buildsPage);
    }

    /**
     * Elimina una build existente según su identificador.
     *
     * @param idBuild identificador de la build a eliminar.
     * @return una {@link ResponseEntity} sin contenido (204 No Content).
     */
    @DeleteMapping("/{idBuild}")
    public ResponseEntity<Void> eliminarBuild(@PathVariable Long idBuild) {
        log.info("Solicitud para eliminar build con ID: {}", idBuild);

        buildService.eliminarBuild(idBuild);

        log.info("Build con ID: {} eliminada correctamente", idBuild);
        return ResponseEntity.noContent().build();
    }

    /**
     * Exporta una build a formato Excel.
     * <p>
     * Genera un archivo `.xlsx` con la información de la build y lo devuelve como archivo descargable.
     * </p>
     *
     * @param idBuild identificador de la build a exportar.
     * @return una {@link ResponseEntity} con el archivo Excel en formato de bytes.
     */
    @GetMapping("/export/{idBuild}")
    public ResponseEntity<byte[]> exportarBuild(@PathVariable Long idBuild) {
        log.info("Solicitud para exportar build ID {} a Excel", idBuild);
        try {
            ByteArrayInputStream excelStream = buildExportService.exportarBuildAExcel(idBuild);

            byte[] bytes = excelStream.readAllBytes();
            String nombreArchivo = "build_" + idBuild + ".xlsx";

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + nombreArchivo)
                    .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .body(bytes);

        } catch (IOException e) {
            log.error("Error al exportar build ID {}: {}", idBuild, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Actualiza los datos de una build existente.
     *
     * @param idBuild identificador de la build a actualizar.
     * @param buildRequest objeto {@link BuildRequest} con los nuevos datos.
     * @return una {@link ResponseEntity} con la build actualizada.
     */
    @PutMapping("/{idBuild}")
    public ResponseEntity<Build> actualizarBuild(
            @PathVariable Long idBuild,
            @RequestBody BuildRequest buildRequest) {

        log.info("Solicitud para actualizar la build con ID: {}", idBuild);

        Build buildActualizada = buildService.actualizarBuild(idBuild, buildRequest);

        log.info("Build con ID: {} actualizada correctamente", idBuild);

        return ResponseEntity.ok(buildActualizada);
    }

    /**
     * Obtiene productos disponibles para la creación de builds según la categoría.
     * <p>
     * Devuelve una lista paginada de productos con sus detalles (precio, stock, tienda, etc.)
     * </p>
     *
     * @param page número de página (por defecto 0).
     * @param size cantidad de productos por página (por defecto 8).
     * @param categoria nombre de la categoría a filtrar.
     * @return una {@link ResponseEntity} con una página de {@link ProductoBuildResponse}.
     */
    @GetMapping("/productos")
    public ResponseEntity<Page<ProductoBuildResponse>> obtenerProductos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam String categoria) {

        log.info("Obteniendo productos para builds con la categoria: {}", categoria);
        Page<ProductoBuildResponse> resultados = productoTiendaService.obtenerProductosBuilds(categoria, page, size);

        if (resultados.isEmpty()) {
            log.warn("No se encontraron productos para las builds con categoria: {}", categoria);
            return ResponseEntity.ok(Page.empty());
        }

        log.info("Se encontraron {} productos para las builds: {}", resultados.getTotalElements(), categoria);
        return ResponseEntity.ok(resultados);
    }
}
