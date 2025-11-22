package com.universidad.compusearch.util;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

/**
 * Clase utilitaria para la carga de imágenes desde el classpath del proyecto.
 * 
 * <p>Proporciona un método estático para obtener el contenido binario (byte array)
 * de una imagen almacenada en los recursos de la aplicación (por ejemplo, en
 * {@code src/main/resources}).</p>
 * 
 * <p>Esta clase no debe ser instanciada.</p>
 * 
 */
public class CargarImagen {

    /**
     * Carga una imagen ubicada en el classpath del proyecto y devuelve su contenido
     * como un arreglo de bytes.
     *
     * <p>El método busca el recurso usando la ruta relativa proporcionada, y si la imagen
     * no existe, retorna {@code null}. En caso contrario, convierte su flujo de entrada
     * en un arreglo de bytes.</p>
     *
     * @param rutaRelativa ruta relativa dentro del classpath donde se encuentra la imagen 
     *                     (por ejemplo, {@code "static/img/logo.png"}).
     * @return un arreglo de bytes con el contenido de la imagen, o {@code null} si el archivo no existe.
     * @throws IOException si ocurre un error al intentar acceder o leer el recurso.
     */
    public static byte[] cargarImagen(String rutaRelativa) throws IOException {
        ClassPathResource imgFile = new ClassPathResource(rutaRelativa);
        if (!imgFile.exists())
            return null;
        return StreamUtils.copyToByteArray(imgFile.getInputStream());
    }
}
