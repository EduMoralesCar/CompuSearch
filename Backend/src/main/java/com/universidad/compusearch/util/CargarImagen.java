package com.universidad.compusearch.util;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

public class CargarImagen {

    // Cargar imagen desde su ruta
    public static byte[] cargarImagen(String rutaRelativa) throws IOException {
        ClassPathResource imgFile = new ClassPathResource(rutaRelativa);
        if (!imgFile.exists())
            return null;
        return StreamUtils.copyToByteArray(imgFile.getInputStream());
    }
}
