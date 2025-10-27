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

import com.universidad.compusearch.entity.Categoria;
import com.universidad.compusearch.entity.Producto;
import com.universidad.compusearch.entity.ProductoTienda;
import com.universidad.compusearch.entity.Tienda;
import com.universidad.compusearch.entity.TipoUsuario;

@DataJpaTest
@ActiveProfiles("test")
class ProductoTiendaRepositoryTest {

    @Autowired
    private ProductoTiendaRepository productoTiendaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private TiendaRepository tiendaRepository;

    private Producto producto;
    private Tienda tienda;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        // Crear categoría
        categoria = new Categoria();
        categoria.setNombre("Procesadores");
        categoria.setDescripcion("Componentes electrónicos encargados de ejecutar instrucciones del sistema.");
        categoria.setNombreImagen("procesadores.jpg");
        categoriaRepository.save(categoria);

        // Crear producto
        producto = new Producto();
        producto.setNombre("Intel Core i5-10400F");
        producto.setMarca("Intel");
        producto.setModelo("10th Gen");
        producto.setDescripcion("Procesador de 6 núcleos y 12 hilos, ideal para gaming y tareas de oficina.");
        producto.setCategoria(categoria);
        productoRepository.save(producto);

        // Crear tienda
        tienda = new Tienda();
        tienda.setUsername("techstore.pe");
        tienda.setEmail("contacto@techstore.pe");
        tienda.setContrasena("12345678");
        tienda.setActivo(true);
        tienda.setTipoUsuario(TipoUsuario.TIENDA);
        tienda.setNombre("TechStore Perú");
        tienda.setTelefono("+51 999 999 999");
        tienda.setDireccion("Av. Arequipa 1234, Lima");
        tienda.setDescripcion("Tienda especializada en hardware y accesorios de computadoras.");
        tienda.setUrlPagina("https://techstore.pe");
        tienda.setVerificado(true);
        tiendaRepository.save(tienda);

        // Crear relación producto-tienda
        ProductoTienda pt = new ProductoTienda();
        pt.setProducto(producto);
        pt.setTienda(tienda);
        pt.setPrecio(BigDecimal.valueOf(899.90));
        pt.setUrlImagen("https://techstore.pe/img/procesadores/intel-i5-10400f.jpg");
        pt.setUrlProducto("https://techstore.pe/producto/intel-core-i5-10400f");
        pt.setStock(15);
        pt.setHabilitado(true);
        productoTiendaRepository.save(pt);
    }

    @Test
    void testFindByNombreProductoAndNombreTienda() {
        Optional<ProductoTienda> resultado = productoTiendaRepository
                .findByNombreProductoAndNombreTienda("Intel Core i5-10400F", "TechStore Perú");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getProducto().getNombre()).isEqualTo("Intel Core i5-10400F");
        assertThat(resultado.get().getTienda().getNombre()).isEqualTo("TechStore Perú");
    }

    @Test
    void testObtenerRangoPrecioPorCategoria() {
        Object rango = productoTiendaRepository.obtenerRangoPrecioPorCategoria("Procesadores");
        Object[] valores = (Object[]) rango;

        assertThat(((BigDecimal) valores[0]).compareTo(BigDecimal.valueOf(899.90))).isZero();
        assertThat(((BigDecimal) valores[1]).compareTo(BigDecimal.valueOf(899.90))).isZero();
    }

    @Test
    void testFindDistinctMarcasByCategoria() {
        List<String> marcas = productoTiendaRepository.findDistinctMarcasByCategoria("Procesadores");

        assertThat(marcas).contains("Intel");
    }
}
