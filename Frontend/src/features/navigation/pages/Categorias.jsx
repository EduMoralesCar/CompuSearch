import React from "react";
import { Link } from "react-router-dom";
import CategoriaCard from "../components/CategoriaCard";
import useCategorias from "../hooks/useCategorias";
import bannerCategorias from "../../../assets/banners/banner_categorias.jpg"

export default function Categorias() {
    const { categorias, loading, error } = useCategorias();

    return (
        <section>
            <header className="bg-primary text-white py-5"
                style={{
                    backgroundImage: `url(${bannerCategorias})`,
                    backgroundSize: "cover",
                    backgroundPosition: "center",
                }}>
                <div className="container text-center">
                    <h1 className="display-6 fw-bold text-uppercase">
                        Categorías de Componentes
                    </h1>
                    <p className="lead mb-0 fw-italic">
                        Explora y compara precios de todos los componentes de computadora en un
                        solo lugar.
                    </p>
                </div>
            </header>

            <main className="container my-5">

                <h4 className="mb-4 fw-bold text-uppercase border-bottom pb-2">
                    Todas las Categorias
                </h4>

                {loading && <p>Cargando categorías...</p>}
                {error && <p className="text-danger">Error al cargar los datos</p>}

                <div className="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-4">
                    {categorias.map((c) => (
                        <div className="col" key={c.idCategoria}>
                            <CategoriaCard {...c} />
                        </div>
                    ))}
                </div>
            </main>
        </section>
    );
}
