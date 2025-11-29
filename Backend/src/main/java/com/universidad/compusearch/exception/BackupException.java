package com.universidad.compusearch.exception;

public class BackupException extends CustomException {

    public BackupException(String message) {
        super(message);
    }

    public static BackupException error() {
        return new BackupException(
                "Error al generar el backup");
    }
}
