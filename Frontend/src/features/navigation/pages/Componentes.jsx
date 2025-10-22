import React, { useState, useEffect } from "react";
import { useSearchParams } from "react-router-dom";

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

  // --- Estado para los filtros que se envían al hook de búsqueda ---
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
  const {
    filtroCategoria,
    filtroMarca,
    rangoPrecio,
    filtroTienda,
    loading,
    error,
  } = useFiltros(categoria);

  const { valoresAtributos, loading: loadingAdicionales } =
    useFiltrosAdicionales(categoria);

  const {
    productos,
    totalPages,
    loading: loadingProductos,
    error: errorProductos,
  } = useProductosTiendas({
    ...filtrosAplicados,
    nombreProducto: searchQuery,
    page,
  });

  // --- Efectos de Ciclo de Vida ---

  // Efecto de inicialización: Lee la URL una sola vez al montar

  // --- Efectos de Ciclo de Vida ---
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

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []); // El array vacío [] asegura que esto corra SOLO UNA VEZ

  // Setea los precios por defecto (solo si no vienen de la URL o reset)
  useEffect(() => {

    if (
      rangoPrecio &&
      precioMax === 0 &&
      precioMin === 0 &&
      !searchParams.has("precioMin") &&
      !searchParams.has("precioMax")
    ) {
      setPrecioMax(rangoPrecio.precioMax);
      setPrecioMin(rangoPrecio.precioMin);
    }
  }, [rangoPrecio, precioMax, precioMin, searchParams]);

  // Carga los filtros extra (atributos) desde la URL
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

  // Revisa si los filtros están en su estado "por defecto"
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

  // Aplica los filtros leídos de la URL *automáticamente* en la carga inicial
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

  // --- Funciones de Eventos ---

  // Aplicar Filtros (Botón)
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
      page: 0, // Resetea la página a 0 al aplicar filtros
      ...filtrosLimpios,
    };

    setFiltrosAplicados(nuevos);
    setPage(0);

    // Actualiza la URL
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

  // Resetear Filtros (Botón)
  const resetearFiltros = () => {

    const defaultPrecioMin = rangoPrecio?.precioMin ?? 0;
    const defaultPrecioMax = rangoPrecio?.precioMax ?? 1000000;

    // Resetea el estado visual
    setCategoria("Todas");
    setMarca("Todas");
    setTienda("Todas");
    setDisponibilidad("Todas");
    setPage(0);

    const resetFiltrosExtraVisual = Object.fromEntries(
      Object.keys(filtrosExtra).map((key) => [key, "Todas"])
    );
    setFiltrosExtra(resetFiltrosExtraVisual);

    // Resetea el estado de filtros aplicados
    setFiltrosAplicados({
      categoria: "Todas",
      nombreTienda: "",
      precioMax: defaultPrecioMax,
      precioMin: defaultPrecioMin,
      disponible: "Todas",
      marca: "Todas",
      page: 0,
    });


    setSearchParams(new URLSearchParams());

  };

  // --- Renderizado ---
  return (
    <div className="container mt-4">
      <div className="row">
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
            valoresAtributos={valoresAtributos}
            filtrosExtra={filtrosExtra}
            setFiltrosExtra={setFiltrosExtra}
            loadingAdicionales={loadingAdicionales}
            filtrosPorDefecto={filtrosPorDefecto}
          />
        </div>

        <div className="col-md-9">
          <h3 className="mb-3">Listado de productos</h3>

          {loadingProductos && <p>Cargando productos...</p>}
          {errorProductos && (
            <p className="text-danger">Error al cargar productos</p>
          )}
          {!loadingProductos && productos.length === 0 && (
            <p>No se encontraron productos</p>
          )}

          <div className="row">
            {productos.map((producto, i) => (
              <div key={i} className="col-12 col-md-6 col-lg-4 mb-3">
                <ProductoTiendaCard producto={producto} />
              </div>
            ))}
          </div>

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