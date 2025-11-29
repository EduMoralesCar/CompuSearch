package com.universidad.compusearch.scheduler;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.exception.BackupException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BackupScheduler {

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

    @Value("${backup.mysqldump.path}")
    private String mysqldumpPath;

    @Scheduled(cron = "0 0 1 * * *")
    public void realizarBackup() {
        try {
            Files.createDirectories(Paths.get(backupPath));

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String backupFile = backupPath + "/" + dbName + "_" + timestamp + ".sql.gz";

            log.info("Ejecutando backup MySQL...");

            // Determinar ruta de mysqldump
            String dumpExecutable = mysqldumpPath.isBlank() ? "mysqldump" : mysqldumpPath;

            // Construir comandos
            List<String> command = new ArrayList<>();
            command.add(dumpExecutable);
            command.add("-h");
            command.add(dbHost);
            command.add("-P");
            command.add(dbPort);
            command.add("-u");
            command.add(dbUser);
            command.add("-p" + dbPassword);
            command.add(dbName);

            log.info("Comando: {}", String.join(" ", command));

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            StringBuilder logOutput = new StringBuilder();

            // Comprimir salida directamente en gzip y almacenar copia para logs
            try (InputStream is = process.getInputStream();
                    GZIPOutputStream gos = new GZIPOutputStream(new FileOutputStream(backupFile))) {

                byte[] buffer = new byte[8192];
                int len;
                while ((len = is.read(buffer)) > 0) {
                    gos.write(buffer, 0, len);
                    logOutput.append(new String(buffer, 0, len)); // guardar para log
                }
            }

            int exitCode = process.waitFor();
            log.info("Exit code: {}", exitCode);

            if (exitCode == 0) {
                log.info("Backup generado correctamente: {}", backupFile);
            } else {
                log.error("Error al generar backup. Código: {}", exitCode);
                log.error("Salida completa del comando:\n{}", logOutput);
                throw BackupException.error();
            }

        } catch (Exception e) {
            log.error("Ha sucedido un error al realizar el backup: {}", e.getMessage(), e);
            throw BackupException.error();
        }
    }
}
