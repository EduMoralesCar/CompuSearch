package com.universidad.compusearch.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.universidad.compusearch.config.initializer.*;

@Configuration
public class DataInitializer {

    private final SuscripcionInitializer suscripcionInitializer;

    DataInitializer(SuscripcionInitializer suscripcionInitializer) {
        this.suscripcionInitializer = suscripcionInitializer;
    }

    @Bean
    CommandLineRunner initData(
            EtiquetaInitializer etiquetaInitializer,
            CategoriaInitializer categoriaInitializer,
            UsuarioInitializer usuarioInitializer,
            AtributoInitializer atributoInitializer,
            ProductoInitializer productoInitializer,
            ProductoTiendaInitializer productoTiendaInitializer,
            PlanInitializer planInitializer,
            SuscripcionInitializer SuscripcionInitializer) {

        return args -> {
            etiquetaInitializer.init();
            categoriaInitializer.init();
            usuarioInitializer.init();
            atributoInitializer.init();
            productoInitializer.init();
            productoTiendaInitializer.init();
            planInitializer.init();
            suscripcionInitializer.init();
        };
    }
}
