package com.universidad.compusearch.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.dto.MessageResponse;
import com.universidad.compusearch.dto.UsuarioInfoResponse;
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
        @RequestBody Map<String, String> cambios) {
            log.info("Solicitud para cambiar de contraseña para el usuario con id: {}", id);

            usuarioService.actualizarPassword(id, cambios); 

            return ResponseEntity.ok(new MessageResponse("Contraseña actualizada"));
        }
}
