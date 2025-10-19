package com.universidad.compusearch.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.universidad.compusearch.config.initializer.*;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            EtiquetaInitializer etiquetaInitializer,
            CategoriaInitializer categoriaInitializer,
            UsuarioInitializer usuarioInitializer,
            AtributoInitializer atributoInitializer,
            ProductoInitializer productoInitializer,
            ProductoTiendaInitializer productoTiendaInitializer) {

        return args -> {
            etiquetaInitializer.init();
            categoriaInitializer.init();
            usuarioInitializer.init();
            atributoInitializer.init();
            productoInitializer.init();
            productoTiendaInitializer.init();
        };
    }
}
