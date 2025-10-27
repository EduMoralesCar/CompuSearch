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
     * @param cambios Mapa con los campos: "currentPassword", "newPassword", "confirmPassword"
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
}
