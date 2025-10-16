import React, { useState, useEffect } from "react";
import { categoriasMap } from "../utils/categoriasMap";
import FiltrosSidebar from "../components/FiltroSidebar";
import useFiltros from "../hooks/useFiltros";
import useProductosTiendas from "../hooks/useProductoTienda";
import ProductoTiendaCard from "../components/ProductoTiendaCard";
import Paginacion from "../components/Paginacion"
import { useSearchParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";

const Componentes = () => {
  const { filtroCategoria, filtroMarca, rangoPrecio, filtroTienda, loading, error } = useFiltros();

  // Estados de edición (sidebar)
  const [categoria, setCategoria] = useState("Todas");
  const [precioMax, setPrecioMax] = useState(0);
  const [precioMin, setPrecioMin] = useState(0);
  const [marca, setMarca] = useState("Todas");
  const [tienda, setTienda] = useState("Todas");
  const [disponibilidad, setDisponibilidad] = useState("Todas");
  const [page, setPage] = useState(0);
  const [searchParams] = useSearchParams();
  const searchQuery = searchParams.get("search") || "";
  const navigate = useNavigate();


  // Estado de filtros aplicados
  const [filtrosAplicados, setFiltrosAplicados] = useState({
    categoria: "",
    nombreTienda: "",
    precioMax: 0,
    precioMin: 0,
    disponible: "",
    marca: "",
    page: 0
  });

  useEffect(() => {
    if (rangoPrecio) {
      setPrecioMax(rangoPrecio.precioMax);
      setPrecioMin(rangoPrecio.precioMin);
    }
  }, [rangoPrecio]);

  const aplicarFiltros = () => {
    console.log("Aplicando filtros:", { tienda, categoria, precioMax, precioMin, marca, disponibilidad });
    setFiltrosAplicados({


      categoria: categoriasMap[categoria],
      nombreTienda: tienda !== "Todas" ? tienda : "",
      precioMax,
      precioMin,
      disponible: disponibilidad,
      marca,
      page: 0
    });
    setPage(0);
    navigate("/componentes", { replace: true });
  };

  const resetearFiltros = () => {
    setCategoria("Todas");
    if (rangoPrecio) {
      setPrecioMax(rangoPrecio.precioMax);
      setPrecioMin(rangoPrecio.precioMin);
    }
    setMarca("Todas");
    setTienda("Todas");
    setDisponibilidad("Todas");
    setPage(0);
    navigate("/componentes", { replace: true });

    // También reseteamos los filtros aplicados
    setFiltrosAplicados({
      categoria: "",
      nombreTienda: "",
      precioMax: rangoPrecio ? rangoPrecio.precioMax : 0,
      precioMin: rangoPrecio ? rangoPrecio.precioMin : 0,
      disponible: null,
      marca: "",
      page: 0
    });
  };

  // Hook de productos SOLO usa filtrosAplicados
  const { productos, totalPages, loading: loadingProductos, error: errorProductos } = useProductosTiendas({
    ...filtrosAplicados,
    nombreProducto: searchQuery,
    page
  });

  return (
    <div className="container mt-4">
      <div className="row">
        {/* Sidebar de filtros */}
        <div className="col-md-3">
          <FiltrosSidebar
            categoria={categoria}
            setCategoria={setCategoria}
            precioMax={precioMax}
            setPrecioMax={setPrecioMax}
            marca={marca}
            setMarca={setMarca}
            tienda={tienda}
            setTienda={setTienda}
            disponibilidad={disponibilidad}
            setDisponibilidad={setDisponibilidad}
            aplicarFiltros={aplicarFiltros}
            resetearFiltros={resetearFiltros}
            filtroCategoria={filtroCategoria}
            filtroMarca={filtroMarca}
            rangoPrecio={rangoPrecio}
            filtroTienda={filtroTienda}
            loading={loading}
            error={error}
          />
        </div>

        {/* Listado de productos */}
        <div className="col-md-9">
          <h3>Listado de productos</h3>

          {loadingProductos && <p>Cargando productos...</p>}
          {errorProductos && <p>Error al cargar productos: {errorProductos.message}</p>}
          {!loadingProductos && productos.length === 0 && <p>No se encontraron productos</p>}

          {productos.map((p, i) => (
            <ProductoTiendaCard key={i} producto={p} />
          ))}

          {/* Paginación */}
          <Paginacion
            page={page}
            totalPages={totalPages}
            onPageChange={setPage}
          />
        </div>
      </div>
    </div>
  );
};

export default Componentes;
