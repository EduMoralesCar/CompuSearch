import React from "react";

const FiltrosSidebar = ({
  categoria, setCategoria,
  precioMax, setPrecioMax,
  marca, setMarca,
  tienda, setTienda,
  disponibilidad, setDisponibilidad,
  aplicarFiltros,
  resetearFiltros,
  filtroCategoria,
  filtroTienda,
  filtroMarca,
  rangoPrecio,
  loading, // Recibimos loading
  error,   // Recibimos error
  valoresAtributos,
  filtrosExtra,
  setFiltrosExtra,
  filtrosPorDefecto
}) => {
  
  // Hacemos la desestructuración segura por si rangoPrecio es null
  const minRango = rangoPrecio?.precioMin ?? 0;
  const maxRango = rangoPrecio?.precioMax ?? 10000; // Valor por defecto

  return (
    <>
      {/* Botón visible solo en móviles (Tu código original) */}
      <div className="d-md-none mb-3">
        <button
          className="btn btn-primary w-100"
          type="button"
          data-bs-toggle="offcanvas"
          data-bs-target="#filtrosOffcanvas"
        >
          Mostrar filtros
        </button>
      </div>

      {/* Sidebar normal en escritorio */}
      <div className="d-none d-md-block">
        <FiltrosContent
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
          filtroTienda={filtroTienda}
          filtroMarca={filtroMarca}
          minRango={minRango}
          maxRango={maxRango}
          valoresAtributos={valoresAtributos}
          filtrosExtra={filtrosExtra}
          setFiltrosExtra={setFiltrosExtra}
          filtrosPorDefecto={filtrosPorDefecto}
          loading={loading} // Pasamos loading al contenido
          error={error}     // Pasamos error al contenido
        />
      </div>

      {/* Offcanvas para móviles (Tu código original) */}
      <div
        className="offcanvas offcanvas-start"
        tabIndex="-1"
        id="filtrosOffcanvas"
      >
        <div className="offcanvas-header">
          <h5 className="offcanvas-title">Filtros</h5>
          <button
            type="button"
            className="btn-close"
            data-bs-dismiss="offcanvas"
          ></button>
        </div>
        <div className="offcanvas-body">
          {/* Pasamos todas las props, incluyendo loading y error */}
          <FiltrosContent
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
            filtroTienda={filtroTienda}
            filtroMarca={filtroMarca}
            minRango={minRango}
            maxRango={maxRango}
            valoresAtributos={valoresAtributos}
            filtrosExtra={filtrosExtra}
            setFiltrosExtra={setFiltrosExtra}
            filtrosPorDefecto={filtrosPorDefecto}
            loading={loading} // Pasamos loading al contenido
            error={error}     // Pasamos error al contenido
          />
        </div>
      </div>
    </>
  );
};

// Extraemos el contenido de filtros para reutilizarlo
const FiltrosContent = ({
  categoria, setCategoria,
  precioMax, setPrecioMax,
  marca, setMarca,
  tienda, setTienda,
  disponibilidad, setDisponibilidad,
  aplicarFiltros,
  resetearFiltros,
  filtroCategoria,
  filtroTienda,
  filtroMarca,
  minRango,
  maxRango,
  valoresAtributos,
  filtrosExtra,
  setFiltrosExtra,
  filtrosPorDefecto,
  loading, // Recibimos loading
  error    // Recibimos error
}) => {

  if (loading) return <p>Cargando filtros...</p>;
  if (error) return <p className="text-danger">Error al cargar filtros</p>;

  // Si no está cargando, muestra los filtros
  return (
    <div>
      <button className="btn btn-primary w-100 mb-3" onClick={aplicarFiltros} data-bs-dismiss="offcanvas">
        Aplicar filtros
      </button>
      <button className="btn btn-outline-primary w-100 mb-3" onClick={resetearFiltros} disabled={filtrosPorDefecto}>
        Resetear filtros
      </button>

      {/* Categoría */}
      <div className="mb-3">
        <label className="form-label">Categoría:</label>
        <select className="form-select" value={categoria} onChange={(e) => setCategoria(e.target.value)}>
          <option value="Todas">Todas</option>
          {filtroCategoria.map((cat, i) => (
            <option key={i} value={cat}>{cat}</option>
          ))}
        </select>
      </div>

      {/* Tienda */}
      <div className="mb-3">
        <label className="form-label">Tienda:</label>
        <select className="form-select" value={tienda} onChange={(e) => setTienda(e.target.value)}>
          <option value="Todas">Todas</option>
          {filtroTienda.map((t, i) => (
            <option key={i} value={t}>{t}</option>
          ))}
        </select>
      </div>

      {/* Marca */}
      <div className="mb-3">
        <label className="form-label">Marca:</label>
        <select className="form-select" value={marca} onChange={(e) => setMarca(e.target.value)}>
          <option value="Todas">Todas</option>
          {filtroMarca.map((m, i) => (
            <option key={i} value={m}>{m}</option>
          ))}
        </select>
      </div>

      {/* Disponibilidad */}
      <div className="mb-3">
        <label className="form-label">Disponibilidad:</label>
        <select
          className="form-select"
          value={disponibilidad}
          onChange={(e) => setDisponibilidad(e.target.value)}
        >
          <option value="Todas">Todas</option>
          <option value="Disponible">Disponible</option>
          <option value="No disponible">No disponible</option>
        </select>
      </div>

      {/* Precio */}
      <div className="mb-3">
        <label className="form-label">
          Precio: S/. {minRango} – S/. {precioMax}
        </label>
        <input
          type="range"
          className="form-range"
          min={minRango}
          max={maxRango}
          step="10"
          value={precioMax}
          onChange={(e) => setPrecioMax(Number(e.target.value))}
          disabled={minRango === maxRango}
        />
      </div>

      {/* Filtros adicionales dinámicos */}
      {Object.entries(valoresAtributos).map(([atributo, opciones]) => (
        <div key={atributo} className="mb-3">
          <label className="form-label">{atributo}:</label>
          <select
            className="form-select"
            value={filtrosExtra[atributo] || "Todas"}
            onChange={(e) =>
              setFiltrosExtra((prev) => ({
                ...prev,
                [atributo]: e.target.value
              }))
            }
          >
            <option value="Todas">Todas</option>
            {opciones.map((opcion, idx) => (
              <option key={idx} value={opcion.value}>
                {opcion.label}
              </option>
            ))}
          </select>
        </div>
      ))}
    </div>
  );
};

export default FiltrosSidebar;