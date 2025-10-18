package com.universidad.compusearch.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.dto.BuildRequest;
import com.universidad.compusearch.dto.MessageResponse;
import com.universidad.compusearch.entity.Build;
import com.universidad.compusearch.service.BuildExportService;
import com.universidad.compusearch.service.BuildService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/builds")
@RequiredArgsConstructor
@Slf4j
public class BuildController {

    private final BuildService buildService;
    private final BuildExportService buildExportService;

    @PostMapping
    public ResponseEntity<MessageResponse> crearBuild(@RequestBody BuildRequest buildRequest) {
        log.info("Petición recibida para crear una nueva Build. Usuario ID: {}, Nombre Build: {}",
                buildRequest.getIdUsuario(), buildRequest.getNombre());

        Build nuevaBuild = buildService.crearBuild(buildRequest);
        log.info("Build creada exitosamente con ID: {} para Usuario ID: {}",
                nuevaBuild.getIdBuild(), nuevaBuild.getUsuario().getIdUsuario());

        return ResponseEntity.ok(new MessageResponse("Build guardada con éxito."));
    }

    @GetMapping("/{idBuild}")
    public ResponseEntity<Build> obtenerBuildPorId(@PathVariable Long idBuild) {
        log.info("Buscando build con ID: {}", idBuild);

        Build build = buildService.obtenerBuildPorId(idBuild);

        log.info("Build encontrada con ID: {}", build.getIdBuild());
        return ResponseEntity.ok(build);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Build>> obtenerBuildsPorUsuario(@PathVariable Long idUsuario) {
        log.info("Solicitando builds del usuario ID: {}", idUsuario);

        List<Build> builds = buildService.obtenerBuildsPorUsuario(idUsuario);

        log.info("Se encontraron {} builds para el usuario ID: {}", builds.size(), idUsuario);
        return ResponseEntity.ok(builds);
    }

    @DeleteMapping("/{idBuild}")
    public ResponseEntity<Void> eliminarBuild(@PathVariable Long idBuild) {
        log.info("Solicitud para eliminar build con ID: {}", idBuild);

        buildService.eliminarBuild(idBuild);

        log.info("Build con ID: {} eliminada correctamente", idBuild);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{idBuild}/export")
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

}
