import React, { useState } from "react";
import useTiendas from "../hooks/useTienda";
import useEtiquetas from "../hooks/useEtiquetas";
import TiendaFilters from "../components/TiendaFilters";
import TiendaCard from "../components/TiendaCard";
import bannerTienda from "../../../assets/banners/banner_tiendas.jpg";

export default function Tiendas() {
    const { tiendas, loading, error } = useTiendas();
    const { etiquetas, loading: loadingEtiquetas, error: errorEtiquetas } = useEtiquetas();

    const [etiquetaFilter, setEtiquetaFilter] = useState("");
    const [searchTerm, setSearchTerm] = useState("");

    const filteredShops = tiendas.filter((shop) => {
        const matchesSearch = searchTerm
            ? shop.nombre.toLowerCase().includes(searchTerm.toLowerCase())
            : true;

        const matchesEtiqueta = etiquetaFilter
            ? shop.etiquetas.some((e) => e.nombre === etiquetaFilter)
            : true;

        return matchesSearch && matchesEtiqueta;
    });

    return (
        <section>
            <header
                className="bg-primary text-white py-5 shadow-sm"
                style={{
                    backgroundImage: `url(${bannerTienda})`,
                    backgroundSize: "cover",
                    backgroundPosition: "center",
                }}
            >
                <div className="container text-center">
                    <h1 className="display-6 fw-bold mb-2">Tiendas Asociadas</h1>
                    <p className="lead mb-0 opacity-75">
                        Descubre nuestras tiendas colaboradoras, ofertas y promociones exclusivas.
                    </p>
                </div>
            </header>

            <main className="container my-5">
                <TiendaFilters
                    searchTerm={searchTerm}
                    setSearchTerm={setSearchTerm}
                    etiquetaFilter={etiquetaFilter}
                    setEtiquetaFilter={setEtiquetaFilter}
                    etiquetasDisponibles={etiquetas}
                />

                {(loading || loadingEtiquetas) && <p className="text-center">Cargando tiendas y etiquetas...</p>}
                {(error || errorEtiquetas) && (
                    <p className="text-danger text-center">Error al cargar datos</p>
                )}

                {!loading && !error && (
                    <>
                        <div className="d-flex justify-content-between align-items-center mb-4">
                            <h4 className="fw-bold mb-0">Tiendas</h4>
                            <span className="badge bg-secondary-subtle text-dark px-3 py-2">
                                {filteredShops.length} tiendas
                            </span>
                        </div>

                        <div className="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-4 mb-5">
                            {filteredShops.map((s, index) => (
                                <div className="col" key={index}>
                                    <TiendaCard {...s} />
                                </div>
                            ))}
                        </div>
                    </>
                )}
            </main>
        </section>
    );
}
