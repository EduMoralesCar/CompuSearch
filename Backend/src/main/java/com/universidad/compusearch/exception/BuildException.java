package com.universidad.compusearch.exception;

// Excepciones de build
public class BuildException extends CustomException {

    public BuildException(String message, int status, String code) {
        super(message, status, code);
    }
    
    public static BuildException notFound() {
        return new BuildException("Build no encontrada", 404, "BUILD_NOT_FOUND");
    }
}