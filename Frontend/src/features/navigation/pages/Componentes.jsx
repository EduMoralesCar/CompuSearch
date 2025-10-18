import React, { useState, useEffect } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";

import { categoriasMap } from "../utils/categoriasMap";
import FiltrosSidebar from "../components/FiltroSidebar";
import ProductoTiendaCard from "../components/ProductoTiendaCard";
import Paginacion from "../components/Paginacion";

import useFiltros from "../hooks/useFiltros";
import useFiltrosAdicionales from "../hooks/useFiltrosAdicionales";
import useProductosTiendas from "../hooks/useProductoTienda";

const Componentes = () => {
  const [categoria, setCategoria] = useState("Todas");
  const [precioMax, setPrecioMax] = useState(0);
  const [precioMin, setPrecioMin] = useState(0);
  const [marca, setMarca] = useState("Todas");
  const [tienda, setTienda] = useState("Todas");
  const [disponibilidad, setDisponibilidad] = useState("Todas");
  const [page, setPage] = useState(0);
  const [filtrosExtra, setFiltrosExtra] = useState({});
  const [categoriaDesdeUrl, setCategoriaDesdeUrl] = useState(false);
  const [filtrosPorDefecto, setFiltrosPorDefecto] = useState(true);

  const [filtrosAplicados, setFiltrosAplicados] = useState({
    categoria: "",
    nombreTienda: "",
    precioMax: 0,
    precioMin: 0,
    disponible: "",
    marca: "",
    page: 0,
  });

  const [searchParams, setSearchParams] = useSearchParams();
  const searchQuery = searchParams.get("search") || "";
  const navigate = useNavigate();

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
      !filtrosExtraActivos;

    setFiltrosPorDefecto(esPorDefecto);
  }, [categoria, marca, tienda, disponibilidad, precioMin, precioMax, filtrosExtra, rangoPrecio]);


  useEffect(() => {
    const categoriaParam = searchParams.get("categorias");
    if (categoriaParam) {
      setCategoria(categoriaParam);
      setCategoriaDesdeUrl(true);
    }
  }, [searchParams]);

  useEffect(() => {
    if (categoriaDesdeUrl && categoria && categoria !== "Todas") {
      const nuevos = {
        categoria: categoriasMap[categoria] || categoria,
        nombreTienda: tienda !== "Todas" ? tienda : "",
        precioMax,
        precioMin,
        disponible: disponibilidad,
        marca,
        page: 0,
        ...Object.entries(filtrosExtra)
          .filter(([, valor]) => valor !== "Todas" && valor !== "")
          .reduce((acc, [clave, valor]) => ({ ...acc, [clave]: valor }), {}),
      };

      setFiltrosAplicados(nuevos);
      setPage(0);
      setCategoriaDesdeUrl(false);
    }
  }, [categoriaDesdeUrl, categoria, tienda, precioMax, precioMin, disponibilidad, marca, filtrosExtra]);

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
    if (rangoPrecio) {
      setPrecioMax(rangoPrecio.precioMax);
      setPrecioMin(rangoPrecio.precioMin);
    }
  }, [rangoPrecio]);

  const aplicarFiltros = () => {
    const filtrosLimpios = {};
    Object.entries(filtrosExtra).forEach(([clave, valor]) => {
      if (valor && valor !== "Todas") filtrosLimpios[clave] = valor;
    });

    const nuevos = {
      categoria: categoriasMap[categoria],
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
    const defaultMax = rangoPrecio?.precioMax ?? 0;
    const defaultMin = rangoPrecio?.precioMin ?? 0;

    setCategoria("Todas");
    setPrecioMax(defaultMax);
    setPrecioMin(defaultMin);
    setMarca("Todas");
    setTienda("Todas");
    setDisponibilidad("Todas");
    setFiltrosExtra({});
    setPage(0);

    setFiltrosAplicados({
      categoria: "Todas",
      nombreTienda: "",
      precioMax: defaultMax,
      precioMin: defaultMin,
      disponible: "Todas",
      marca: "Todas",
      page: 0,
    });

    setSearchParams(new URLSearchParams());
    navigate("/componentes", { replace: true });
  };

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
          <h3>Listado de productos</h3>

          {loadingProductos && <p>Cargando productos...</p>}
          {errorProductos && (
            <p className="text-danger">Error al cargar productos</p>
          )}
          {!loadingProductos && productos.length === 0 && (
            <p>No se encontraron productos</p>
          )}

          {productos.map((producto, i) => (
            <ProductoTiendaCard key={i} producto={producto} />
          ))}

          <Paginacion page={page} totalPages={totalPages} onPageChange={setPage} />
        </div>
      </div>
    </div>
  );
};

export default Componentes;
