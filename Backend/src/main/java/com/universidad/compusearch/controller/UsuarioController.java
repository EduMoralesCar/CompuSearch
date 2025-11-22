package com.universidad.compusearch.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.universidad.compusearch.dto.MessageResponse;
import com.universidad.compusearch.dto.UsuarioInfoResponse;
import com.universidad.compusearch.service.UsuarioService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para manejar operaciones relacionadas con usuarios.
 *
 * <p>
 * Proporciona endpoints para consultar información de usuario y actualizar
 * información personal o contraseña.
 * </p>
 *
 * <p>
 * Base URL: <b>/usuario</b>
 * </p>
 */
@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
@Slf4j
public class UsuarioController {
    private final UsuarioService usuarioService;

    /**
     * Obtiene la información de un usuario por su ID.
     *
     * @param id ID del usuario
     * @return {@link UsuarioInfoResponse} con los datos del usuario
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioInfoResponse> obtenerUsuarioPorId(@PathVariable Long id) {
        log.info("Solicitud para traer informacion de usuario con id: {}", id);
        UsuarioInfoResponse info = usuarioService.buscarInfoUsuario(id);
        return ResponseEntity.ok(info);
    }

    /**
     * Actualiza la información personal de un usuario.
     *
     * @param id      ID del usuario
     * @param cambios Mapa con los campos y valores a actualizar
     * @return Mensaje indicando que la información fue actualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> actualizarInfoPersonal(
            @PathVariable Long id,
            @RequestBody Map<String, String> cambios) {

        log.info("Solicitud para actualizar información personal de usuario con id: {}", id);

        usuarioService.actualizarInfoPersonal(id, cambios);

        return ResponseEntity.ok(new MessageResponse("Informacion actualizada"));
    }

    /**
     * Actualiza la contraseña de un usuario.
     *
     * @param id      ID del usuario
     * @param cambios Mapa con los campos: "currentPassword", "newPassword",
     *                "confirmPassword"
     * @return Mensaje indicando que la contraseña fue actualizada
     */
    @PutMapping("/password/{id}")
    public ResponseEntity<MessageResponse> actualizarPassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> cambios) {

        log.info("Solicitud para cambiar de contraseña para el usuario con id: {}", id);

        usuarioService.actualizarPassword(id, cambios);

        return ResponseEntity.ok(new MessageResponse("Contraseña actualizada"));
    }

    /**
     * Obtiene usuarios paginados filtrados por tipo de usuario.
     *
     * @param tipo tipo de usuario (USUARIO, EMPLEADO, ADMIN)
     * @param page número de página (default: 0)
     * @param size tamaño de página (default: 10)
     * @return Página de usuarios del tipo especificado
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<org.springframework.data.domain.Page<com.universidad.compusearch.entity.Usuario>> obtenerUsuariosPorTipo(
            @PathVariable String tipo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Solicitud para obtener usuarios de tipo: {} (página: {}, tamaño: {})", tipo, page, size);
        org.springframework.data.domain.Page<com.universidad.compusearch.entity.Usuario> usuarios = usuarioService
                .obtenerUsuariosPorTipo(tipo, page, size);
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Cambia el estado (habilitado/deshabilitado) de un usuario.
     *
     * @param id     ID del usuario
     * @param estado Mapa con la clave "habilitado" (true/false)
     * @return Mensaje de confirmación
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<MessageResponse> cambiarEstadoUsuario(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> estado) {

        log.info("Solicitud para cambiar estado del usuario {}", id);
        boolean habilitado = estado.getOrDefault("habilitado", true);
        usuarioService.cambiarEstadoUsuario(id, habilitado);

        return ResponseEntity.ok(new MessageResponse(
                habilitado ? "Usuario habilitado" : "Usuario deshabilitado"));
    }

    /**
     * Elimina un usuario del sistema.
     *
     * @param id ID del usuario a eliminar
     * @return Mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> eliminarUsuario(@PathVariable Long id) {
        log.warn("Solicitud para eliminar usuario {}", id);
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok(new MessageResponse("Usuario eliminado"));
    }
}
