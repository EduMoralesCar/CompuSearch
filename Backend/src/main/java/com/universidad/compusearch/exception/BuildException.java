package com.universidad.compusearch.exception;

public class BuildException extends CustomException {

    public BuildException(String message) {
        super(message);
    }

    public static BuildException notFound() {
        return new BuildException(
                "Build no encontrada");
    }
}
