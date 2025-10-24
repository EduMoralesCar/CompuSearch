package com.universidad.compusearch.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.entity.Categoria;
import com.universidad.compusearch.service.CategoriaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
@Slf4j
public class CategoriaController {
    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<Categoria>> obtenerTodasLasCategorias() {
        log.info("Solicitando todas las categorias");

        List<Categoria> categorias = categoriaService.obtenerTodos();

        log.info("Se retornaron {} etiquetas.", categorias.size());
        return ResponseEntity.ok(categorias);
    }
}
