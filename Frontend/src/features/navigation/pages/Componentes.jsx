import React, { useState, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { Spinner, Alert } from "react-bootstrap";

import { categoriasMap } from "../utils/categoriasMap";
import FiltrosSidebar from "../components/FiltroSidebar";
import ProductoTiendaCard from "../components/ProductoTiendaCard";
import Paginacion from "../components/Paginacion";

import useFiltros from "../hooks/useFiltros";
import useFiltrosAdicionales from "../hooks/useFiltrosAdicionales";
import useProductosTiendas from "../hooks/useProductoTienda";

const Componentes = () => {
  // --- Estados para el control visual de los filtros ---
  const [categoria, setCategoria] = useState("Todas");
  const [precioMax, setPrecioMax] = useState(0);
  const [precioMin, setPrecioMin] = useState(0);
  const [marca, setMarca] = useState("Todas");
  const [tienda, setTienda] = useState("Todas");
  const [disponibilidad, setDisponibilidad] = useState("Todas");
  const [page, setPage] = useState(0);
  const [filtrosExtra, setFiltrosExtra] = useState({});

  // --- Filtros aplicados (enviados al hook de búsqueda) ---
  const [filtrosAplicados, setFiltrosAplicados] = useState({
    categoria: "Todas",
    nombreTienda: "",
    precioMax: 0,
    precioMin: 0,
    disponible: "Todas",
    marca: "Todas",
    page: 0,
  });

  // --- Estados de control ---
  const [filtrosPorDefecto, setFiltrosPorDefecto] = useState(true);
  const [isInitialLoad, setIsInitialLoad] = useState(true);

  const [searchParams, setSearchParams] = useSearchParams();
  const searchQuery = searchParams.get("search") || "";

  // --- Hooks de datos ---
  const { filtroCategoria, filtroMarca, rangoPrecio, filtroTienda, loading, error } =
    useFiltros(categoria);

  const { valoresAtributos, loading: loadingAdicionales } =
    useFiltrosAdicionales(categoria);

  const { productos, totalPages, loading: loadingProductos, error: errorProductos } =
    useProductosTiendas({
      ...filtrosAplicados,
      nombreProducto: searchQuery,
      page,
    });

  // --- Ciclo de Vida ---
  useEffect(() => {
    window.scrollTo({ top: 0, behavior: "smooth" });
  }, [page]);

  useEffect(() => {
    const categoriaParam = searchParams.get("categoria") || "Todas";
    const marcaParam = searchParams.get("marca") || "Todas";
    const tiendaParam = searchParams.get("tienda") || "Todas";
    const disponibilidadParam = searchParams.get("disponibilidad") || "Todas";
    const pageParam = parseInt(searchParams.get("page") || "0", 10);
    const precioMinParam = searchParams.get("precioMin");
    const precioMaxParam = searchParams.get("precioMax");

    setCategoria(categoriaParam);
    setMarca(marcaParam);
    setTienda(tiendaParam);
    setDisponibilidad(disponibilidadParam);
    setPage(pageParam);

    if (precioMinParam !== null) setPrecioMin(parseInt(precioMinParam, 10));
    if (precioMaxParam !== null) setPrecioMax(parseInt(precioMaxParam, 10));
  }, []);

  useEffect(() => {
    if (rangoPrecio) {
      if (!searchParams.has("precioMin") && !searchParams.has("precioMax")) {
        setPrecioMax(rangoPrecio.precioMax);
        setPrecioMin(rangoPrecio.precioMin);
      } else {
        const precioMinParam = parseInt(searchParams.get("precioMin"), 10);
        const precioMaxParam = parseInt(searchParams.get("precioMax"), 10);
        if (!isNaN(precioMinParam)) setPrecioMin(precioMinParam);
        if (!isNaN(precioMaxParam)) setPrecioMax(precioMaxParam);
      }
    }
  }, [rangoPrecio, searchParams]);

  useEffect(() => {
    if (Object.keys(valoresAtributos).length > 0) {
      const nuevosFiltros = {};
      Object.keys(valoresAtributos).forEach((atributo) => {
        const valorParam = searchParams.get(atributo);
        nuevosFiltros[atributo] = valorParam || "Todas";
      });
      setFiltrosExtra(nuevosFiltros);
    }
  }, [valoresAtributos, searchParams]);

  useEffect(() => {
    const filtrosExtraActivos = Object.values(filtrosExtra).some(
      (valor) => valor !== "Todas" && valor !== ""
    );

    const esPorDefecto =
      categoria === "Todas" &&
      marca === "Todas" &&
      tienda === "Todas" &&
      disponibilidad === "Todas" &&
      precioMin === (rangoPrecio?.precioMin ?? 0) &&
      precioMax === (rangoPrecio?.precioMax ?? 0) &&
      !filtrosExtraActivos &&
      searchQuery === "";

    setFiltrosPorDefecto(esPorDefecto);
  }, [
    categoria,
    marca,
    tienda,
    disponibilidad,
    precioMin,
    precioMax,
    filtrosExtra,
    rangoPrecio,
    searchQuery,
  ]);

  useEffect(() => {
    if (isInitialLoad && !loading && !loadingAdicionales) {
      const filtrosLimpios = {};
      Object.entries(filtrosExtra).forEach(([clave, valor]) => {
        if (valor && valor !== "Todas") filtrosLimpios[clave] = valor;
      });

      const nuevos = {
        categoria: categoriasMap[categoria] || categoria,
        nombreTienda: tienda !== "Todas" ? tienda : "",
        precioMax,
        precioMin,
        disponible: disponibilidad,
        marca,
        page,
        ...filtrosLimpios,
      };

      setFiltrosAplicados(nuevos);
      setIsInitialLoad(false);
    }
  }, [
    isInitialLoad,
    loading,
    loadingAdicionales,
    categoria,
    marca,
    tienda,
    disponibilidad,
    precioMin,
    precioMax,
    filtrosExtra,
    page,
  ]);

  // --- Funciones de Filtros ---
  const aplicarFiltros = () => {
    const filtrosLimpios = {};
    Object.entries(filtrosExtra).forEach(([clave, valor]) => {
      if (valor && valor !== "Todas") filtrosLimpios[clave] = valor;
    });

    const nuevos = {
      categoria: categoriasMap[categoria] || categoria,
      nombreTienda: tienda !== "Todas" ? tienda : "",
      precioMax,
      precioMin,
      disponible: disponibilidad,
      marca,
      page: 0,
      ...filtrosLimpios,
    };

    setFiltrosAplicados(nuevos);
    setPage(0);

    const params = new URLSearchParams();
    params.set("categoria", categoria);
    if (marca !== "Todas") params.set("marca", marca);
    if (tienda !== "Todas") params.set("tienda", tienda);
    if (disponibilidad !== "Todas") params.set("disponibilidad", disponibilidad);
    params.set("precioMin", precioMin.toString());
    params.set("precioMax", precioMax.toString());
    params.set("page", "0");
    if (searchQuery) params.set("search", searchQuery);
    Object.entries(filtrosLimpios).forEach(([clave, valor]) => {
      params.set(clave, valor);
    });
    setSearchParams(params);
  };

  const resetearFiltros = () => {
    setCategoria("Todas");
    setMarca("Todas");
    setTienda("Todas");
    setDisponibilidad("Todas");
    setPage(0);
    setPrecioMax(0);
    setPrecioMin(0);

    const resetFiltrosExtraVisual = Object.fromEntries(
      Object.keys(filtrosExtra).map((key) => [key, "Todas"])
    );
    setFiltrosExtra(resetFiltrosExtraVisual);

    setFiltrosAplicados({
      categoria: "Todas",
      nombreTienda: "",
      precioMax: 0,
      precioMin: 0,
      disponible: "Todas",
      marca: "Todas",
      page: 0,
    });

    setSearchParams(new URLSearchParams());
  };

  // --- Renderizado ---
  return (
    <div className="container mt-4 mb-5">
      <div className="row">
        {/* --- Filtros Laterales --- */}
        <div className="col-md-3 mb-4 mb-md-0">
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
            valoresAtributos={valoresAtributos}
            filtrosExtra={filtrosExtra}
            setFiltrosExtra={setFiltrosExtra}
            loadingAdicionales={loadingAdicionales}
            filtrosPorDefecto={filtrosPorDefecto}
          />
        </div>

        {/* --- Listado de Productos --- */}
        <div className="col-md-9">
          <div className="d-flex justify-content-between align-items-center mb-3">
            <h3 className="fw-bold mb-0">Listado de productos</h3>
            <span className="badge bg-secondary-subtle text-dark">
              {productos.length} resultados
            </span>
          </div>

          {loadingProductos && (
            <div className="text-center py-5">
              <Spinner animation="border" variant="primary" />
              <p className="mt-3">Cargando productos...</p>
            </div>
          )}

          {errorProductos && (
            <Alert variant="danger" className="text-center">
              Error al cargar productos. Intenta nuevamente.
            </Alert>
          )}

          {!loadingProductos && productos.length === 0 && (
            <Alert variant="info" className="text-center">
              No se encontraron productos con los filtros seleccionados.
            </Alert>
          )}

          <div className="row g-4">
            {productos.map((producto, i) => (
              <div key={i} className="col-12 col-md-6 col-lg-4">
                <ProductoTiendaCard producto={producto} />
              </div>
            ))}
          </div>

          {!loadingProductos && totalPages > 1 && (
            <div className="mt-4">
              <Paginacion page={page} totalPages={totalPages} onPageChange={setPage} />
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Componentes;
