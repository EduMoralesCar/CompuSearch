package com.universidad.compusearch.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.universidad.compusearch.dto.ProductoAtributoRequest;
import com.universidad.compusearch.entity.Atributo;
import com.universidad.compusearch.entity.Producto;
import com.universidad.compusearch.entity.ProductoAtributo;
import com.universidad.compusearch.repository.AtributoRepository;
import com.universidad.compusearch.util.ValidationText;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoAtributoService {

    private final AtributoRepository atributoRepository;

    public List<ProductoAtributo> convertirEnPA(List<ProductoAtributoRequest> atributos, Producto producto) {
        return atributos.stream()
                .map(attr -> {

                    Atributo encontrado = buscarPorNombre(attr.getNombre());

                    if (encontrado == null) {
                        log.info("Atributo '{}' no existe en BD. Se ignora.", attr.getNombre());
                        return null;
                    }

                    boolean parecido = encontrado.getNombre().equalsIgnoreCase(attr.getNombre())
                            || ValidationText.esNombreParecido(
                                    attr.getNombre(),
                                    encontrado.getNombre());

                    if (parecido) {
                        log.info(
                                "Atributo '{}' es parecido a '{}'. Se usará el atributo de BD.",
                                attr.getNombre(),
                                encontrado.getNombre());
                    }

                    ProductoAtributo pa = new ProductoAtributo();
                    pa.setProducto(producto);
                    pa.setAtributo(encontrado);
                    pa.setValor(attr.getValor());

                    return pa;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Atributo buscarPorNombre(String nombre) {
        return atributoRepository.findByNombre(nombre).orElse(null);
    }
}