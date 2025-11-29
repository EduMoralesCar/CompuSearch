package com.universidad.compusearch.controller;

import com.universidad.compusearch.scheduler.BackupScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backup")
@RequiredArgsConstructor
@Slf4j
public class BackupController {

    private final BackupScheduler backupScheduler;

    @PostMapping("/ejecutar")
    public ResponseEntity<String> ejecutarBackup() {
        try {
            log.info("Solicitud manual de backup recibida");
            backupScheduler.realizarBackup();
            return ResponseEntity.ok("Backup ejecutado correctamente.");
        } catch (Exception e) {
            log.error("Error al ejecutar backup manual: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al ejecutar backup: " + e.getMessage());
        }
    }
}
