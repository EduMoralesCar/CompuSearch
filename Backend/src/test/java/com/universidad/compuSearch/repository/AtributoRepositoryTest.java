package com.universidad.compusearch.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.universidad.compusearch.entity.Atributo;
import com.universidad.compusearch.entity.Categoria;
import com.universidad.compusearch.entity.Producto;
import com.universidad.compusearch.entity.ProductoAtributo;
import com.universidad.compusearch.entity.ProductoTienda;
import com.universidad.compusearch.entity.Tienda;
import com.universidad.compusearch.entity.TipoUsuario;

@DataJpaTest
@ActiveProfiles("test")
class AtributoRepositoryTest {

    @Autowired
    private AtributoRepository atributoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private TiendaRepository tiendaRepository;
    @Autowired
    private ProductoTiendaRepository productoTiendaRepository;
    @Autowired
    private ProductoAtributoRepository productoAtributoRepository;

    private Atributo atributo;
    private Producto producto;
    private Tienda tienda;

    @BeforeEach
    void setUp() {
        // Crear categoría
        Categoria categoria = new Categoria();
        categoria.setNombre("Tarjetas Gráficas");
        categoria.setDescripcion("Componentes para procesamiento de video y renderizado.");
        categoria.setNombreImagen("gpu.jpg");
        categoriaRepository.save(categoria);

        // Crear atributo
        atributo = new Atributo();
        atributo.setNombre("Memoria VRAM");
        atributoRepository.save(atributo);

        // Crear producto
        producto = new Producto();
        producto.setNombre("NVIDIA RTX 4070 Ti");
        producto.setMarca("NVIDIA");
        producto.setModelo("4070 Ti");
        producto.setDescripcion("Tarjeta gráfica de alto rendimiento para gaming 4K.");
        producto.setCategoria(categoria);
        productoRepository.save(producto);

        // Crear tienda
        tienda = new Tienda();
        tienda.setUsername("hardwaremax");
        tienda.setEmail("ventas@hardwaremax.pe");
        tienda.setContrasena("12345678");
        tienda.setActivo(true);
        tienda.setTipoUsuario(TipoUsuario.TIENDA);
        tienda.setNombre("Hardware Max");
        tienda.setTelefono("+51 955 555 555");
        tienda.setDireccion("Av. Wilson 345, Lima");
        tienda.setDescripcion("Especialistas en componentes de PC gamer.");
        tienda.setUrlPagina("https://hardwaremax.pe");
        tienda.setVerificado(true);
        tiendaRepository.save(tienda);

        // Crear relación producto-tienda
        ProductoTienda productoTienda = new ProductoTienda();
        productoTienda.setProducto(producto);
        productoTienda.setTienda(tienda);
        productoTienda.setPrecio(BigDecimal.valueOf(3999.99));
        productoTienda.setStock(8);
        productoTienda.setHabilitado(true);
        productoTienda.setUrlImagen("https://hardwaremax.pe/img/rtx4070ti.jpg");
        productoTienda.setUrlProducto("https://hardwaremax.pe/producto/rtx-4070ti");
        productoTiendaRepository.save(productoTienda);

        // Crear atributos de producto
        ProductoAtributo pa1 = new ProductoAtributo();
        pa1.setProducto(producto);
        pa1.setAtributo(atributo);
        pa1.setValor("12 GB GDDR6X");
        productoAtributoRepository.save(pa1);

        ProductoAtributo pa2 = new ProductoAtributo();
        pa2.setProducto(producto);
        pa2.setAtributo(atributo);
        pa2.setValor("16 GB GDDR6");
        productoAtributoRepository.save(pa2);
    }

    @Test
    void testFindByNombre() {
        Optional<Atributo> resultado = atributoRepository.findByNombre("Memoria VRAM");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Memoria VRAM");
    }

    @Test
    void testFindDistinctValoresByAtributo() {
        List<String> valores = atributoRepository.findDistinctValoresByAtributo("Memoria VRAM");

        assertThat(valores).isNotEmpty();
        assertThat(valores).containsExactlyInAnyOrder("12 GB GDDR6X", "16 GB GDDR6");
    }
}
