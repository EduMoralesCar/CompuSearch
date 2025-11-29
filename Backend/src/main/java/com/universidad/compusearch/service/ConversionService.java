package com.universidad.compusearch.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.universidad.compusearch.entity.SolicitudTienda;
import com.universidad.compusearch.entity.Tienda;
import com.universidad.compusearch.entity.TipoUsuario;
import com.universidad.compusearch.entity.Usuario;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConversionService {

    private final TiendaService tiendaService;
    private final EntityManager entityManager;

    // Convertir un usuario en tienda
    @Transactional
    public void convertirUsuarioEnTienda(SolicitudTienda solicitudTienda) {
        Usuario usuario = solicitudTienda.getUsuario();

        if (tiendaService.usuarioConTienda(usuario.getIdUsuario())) {
            log.info("El usuario ya es un usuario TIENDA");
            return;
        }

        if (entityManager.contains(usuario)) {
            entityManager.detach(usuario);
        }

        Tienda tienda = crearTienda(solicitudTienda);

        log.info("Usuario {} convertido exitosamente.", tienda.getUsername());
    }

    public Tienda crearTienda(SolicitudTienda solicitudTienda) {
        log.info("Creando Tienda.");

        Usuario usuarioBase = solicitudTienda.getUsuario();
        Map<String, Object> datos = solicitudTienda.getDatosFormulario();

        Tienda tienda = new Tienda();
        tienda.setIdUsuario(usuarioBase.getIdUsuario());
        tienda.setUsername(usuarioBase.getUsername());
        tienda.setEmail(usuarioBase.getEmail());
        tienda.setContrasena(usuarioBase.getContrasena());
        tienda.setActivo(usuarioBase.isActivo());
        tienda.setTipoUsuario(TipoUsuario.TIENDA);
        tienda.setFechaRegistro(usuarioBase.getFechaRegistro());

        tienda.setNombre((String) datos.getOrDefault("nombreTienda", "Tienda sin nombre"));
        tienda.setTelefono((String) datos.getOrDefault("telefono", "Sin telefono"));
        tienda.setDireccion((String) datos.getOrDefault("direccion", "Sin direccion"));
        tienda.setDescripcion((String) datos.getOrDefault("descripcion", "Sin descripción"));
        tienda.setUrlPagina((String) datos.getOrDefault("sitioWeb", "Sin sitio web"));
        tienda.setVerificado(false);

        log.info("Tienda creada. Lista para persistir.");

        return tiendaService.insertarTiendaDirectamente(tienda);
    }
}