import React from "react";
import { Link } from "react-router-dom";
import CategoriaCard from "../components/categorias/CategoriaCard";
import useCategorias from "../hooks/useCategorias";
import bannerCategorias from "../../../assets/banners/banner_categorias.jpg"
import BannerHeader from "../components/auxliar/BannerHeader"

export default function Categorias() {
    const { categorias, loading, error } = useCategorias();

    return (
        <section>
            <BannerHeader
                title="Arma tu computadora"
                description="Crea tu PC personalizada con los mejores componentes."
                background={bannerCategorias}
            />

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
