package com.universidad.compusearch.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BackupService {

    @Value("${db.host}")
    private String dbHost;

    @Value("${db.port}")
    private String dbPort;

    @Value("${db.name}")
    private String dbName;

    @Value("${db.user}")
    private String dbUser;

    @Value("${db.password}")
    private String dbPassword;

    @Value("${backup.path}")
    private String backupPath;

    @Scheduled(cron = "0 0 2 * * *")
    public void realizarBackup() {
        try {
            Files.createDirectories(Paths.get(backupPath));

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            String backupFile = backupPath + "/" + dbName + "_" + timestamp + ".sql.gz";

            String command = String.format(
                    "mysqldump -h%s -P%s -u%s -p'%s' %s | gzip > %s",
                    dbHost, dbPort, dbUser, dbPassword, dbName, backupFile
            );

            log.info("Ejecutando backup MySQL...");
            log.info("Comando: {}", command);

            Process process = new ProcessBuilder("bash", "-c", command)
                    .redirectErrorStream(true)
                    .start();

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                log.info("Backup generado correctamente: {}", backupFile);
            } else {
                log.info("Error al generar backup. Código: {}", exitCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

