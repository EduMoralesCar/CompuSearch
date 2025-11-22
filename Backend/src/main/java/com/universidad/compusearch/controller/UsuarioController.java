package com.universidad.compusearch.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.universidad.compusearch.dto.MessageResponse;
import com.universidad.compusearch.dto.PasswordResetRequest;
import com.universidad.compusearch.dto.UsuarioInfoResponse;
import com.universidad.compusearch.dto.UsuarioResponse;
import com.universidad.compusearch.service.UsuarioService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
@Slf4j
public class UsuarioController {
    private final UsuarioService usuarioService;

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioInfoResponse> obtenerUsuarioPorId(@PathVariable Long id) {
        log.info("Solicitud para traer informacion de usuario con id: {}", id);
        UsuarioInfoResponse info = usuarioService.buscarInfoUsuario(id);
        return ResponseEntity.ok(info);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> actualizarInfoPersonal(
            @PathVariable Long id,
            @RequestBody Map<String, String> cambios) {

        log.info("Solicitud para actualizar información personal de usuario con id: {}", id);

        usuarioService.actualizarInfoPersonal(id, cambios);

        return ResponseEntity.ok(new MessageResponse("Informacion actualizada"));
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<MessageResponse> actualizarPassword(
            @PathVariable Long id,
            @RequestBody PasswordResetRequest cambios) {

        log.info("Solicitud para cambiar de contraseña para el usuario con id: {}", id);

        usuarioService.actualizarPassword(id, cambios);

        return ResponseEntity.ok(new MessageResponse("Contraseña actualizada"));
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioResponse>> obtenerUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username) {

        log.info("Solicitud para obtener usuarios - página: {}, tamaño: {}, username: {}",
                page, size, username);

        Page<UsuarioResponse> usuarios = usuarioService.obtenerUsuariosPaginados(page, size, username);

        return ResponseEntity.ok(usuarios);
    }

    @PatchMapping("/{id}/activo")
    public ResponseEntity<MessageResponse> actualizarEstadoActivo(
            @PathVariable Long id,
            @RequestParam boolean activo) {

        log.info("Solicitud para actualizar estado 'activo' a {} para el usuario con id: {}", activo, id);

        usuarioService.actualizarActivo(id, activo);

        return ResponseEntity.ok(new MessageResponse("Estado activo actualizado"));
    }
}
