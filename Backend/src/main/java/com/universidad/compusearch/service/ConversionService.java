package com.universidad.compusearch.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importación necesaria

import com.universidad.compusearch.entity.EstadoSuscripcion;
import com.universidad.compusearch.entity.Plan;
import com.universidad.compusearch.entity.SolicitudTienda;
import com.universidad.compusearch.entity.Suscripcion;
import com.universidad.compusearch.entity.Tienda;
import com.universidad.compusearch.entity.TipoUsuario;
import com.universidad.compusearch.entity.Usuario;
import com.universidad.compusearch.exception.PlanException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConversionService {

    private final TiendaService tiendaService;
    private final SuscripcionService suscripcionService;
    private final PlanService planService;
    private final UsuarioService usuarioService;
    
    // Convertir un usuario en tienda
    public void convertirUsuarioEnTienda(SolicitudTienda solicitudTienda) {
        Usuario usuario = solicitudTienda.getUsuario();

        if (tiendaService.usuarioConTienda(usuario.getIdUsuario())) {
            log.info("El usuario ya es un usuario TIENDA");
            return;
        }

        actualizarRolUsuario(usuario);
        crearTiendaYSuscripcion(solicitudTienda);

        log.info("Usuario {} convertido exitosamente.", usuario.getUsername());
    }

    // Actualizar el rol de USUARIO a TIENDA
    @Transactional
    public void actualizarRolUsuario(Usuario usuario) {
        log.info("Actualizando rol de Usuario {}", usuario.getIdUsuario());


        usuario.setTipoUsuario(TipoUsuario.TIENDA);

        usuarioService.guardarUsuario(usuario);

        log.info("Actualizacion completada.");
    }

    // Crear tienda y suscripcion
    @Transactional
    public void crearTiendaYSuscripcion(SolicitudTienda solicitudTienda) {
        log.info("Creando Tienda y Suscripción.");

        Usuario usuario = solicitudTienda.getUsuario();
        Map<String, Object> datos = solicitudTienda.getDatosFormulario();

        Usuario usuarioActualizado = usuarioService.buscarPorId(usuario.getIdUsuario());

        Tienda tienda = new Tienda();
        tienda.setIdUsuario(usuarioActualizado.getIdUsuario());
        tienda.setUsername(usuarioActualizado.getUsername());
        tienda.setEmail(usuarioActualizado.getEmail());
        tienda.setContrasena(usuarioActualizado.getContrasena());
        tienda.setActivo(usuarioActualizado.isActivo());
        tienda.setTipoUsuario(TipoUsuario.TIENDA);
        tienda.setFechaRegistro(usuarioActualizado.getFechaRegistro());

        tienda.setNombre((String) datos.getOrDefault("nombreTienda", "Tienda sin nombre"));
        tienda.setTelefono((String) datos.getOrDefault("telefono", "Sin telefono"));
        tienda.setDireccion((String) datos.getOrDefault("direccion", "Sin direccion"));
        tienda.setDescripcion((String) datos.getOrDefault("descripcion", "Sin descripción"));
        tienda.setUrlPagina((String) datos.getOrDefault("sitioWeb", "Sin sitio web"));
        tienda.setVerificado(false);

        tiendaService.insertarTiendaDirectamente(tienda);

        Plan planGratuito = planService.obtenerPorNombre("Básico (Gratis)");

        if (planGratuito == null) {
            log.error("El plan GRATUITO no se encontró. Fallo de integridad de datos.");

            throw PlanException.notFound();
        }

        Suscripcion suscripcion = new Suscripcion();
        LocalDateTime ahora = LocalDateTime.now();
        suscripcion.setTienda(tienda);
        suscripcion.setPlan(planGratuito);
        suscripcion.setFechaInicio(ahora);

        if (planGratuito.getDuracionMeses() > 0) {
            suscripcion.setFechaFin(ahora.plusMonths(planGratuito.getDuracionMeses()));
        } else {
            suscripcion.setFechaFin(null);
        }
        suscripcion.setEstado(EstadoSuscripcion.ACTIVA);
        suscripcionService.guardarSuscripcion(suscripcion);

        log.info("Tienda creada y suscrita.");
    }
}