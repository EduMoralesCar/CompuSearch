import React, { useState } from "react";
<<<<<<< HEAD
import { Link } from "react-router-dom"; // ✅ Import necesario para navegación interna
import ShopCard from "../../../components/ShopCard";

/**
 * Página - Tiendas Asociadas
 * - Filtros funcionales (categoría, valoración, búsqueda) aplicados a ShopCard.
=======
import ShopCard from "../../../components/ShopCard";

/**
 * Página "Tiendas Asociadas"
 * - Banner superior con color primario.
 * - Breadcrumb con icono alineado.
 * - Filtros funcionales (categoría, valoración, búsqueda) aplicados a ShopCard.
 * - Grid responsivo de tiendas.
 * - Solo Bootstrap 5 (sin CSS adicional).
>>>>>>> a4ccc035302dce290153561b8a84d7e1650f4558
 */

const SHOPS = [
    {
        id: "importaciones-impacto",
        name: "Importaciones Impacto",
        description:
            "Especialistas en componentes de alta gama y equipos gaming. Ofrecemos los mejores precios en hardware de última generación.",
        rating: 4,
        tags: ["Envío gratis", "Ofertas", "Devolución"],
        category: "Basica",
        shipping: "24-48h (nacional)",
        verified: true,
        img: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRDYof_6s5r66FUAWPqDkYyq_WBEZHQKA95rg&s",
        url: "https://www.impacto.com.pe/",
    },
    {
        id: "sercoplus",
        name: "Sercoplus",
        description:
            "Tu tienda de confianza para componentes de computadora. Amplio catálogo y precios competitivos con garantía extendida.",
        rating: 4,
        tags: ["Garantía", "Ofertas", "Devolución"],
        category: "Gamer",
        shipping: "Envío a todo el país",
        verified: true,
        img: "https://compudiskett.com.pe/img/clientes/1.jpg",
        url: "https://sercoplus.com/",
    },
    {
        id: "computer-shop",
        name: "Computer Shop",
        description:
            "Especialistas en memorias RAM y almacenamiento. Ofrecemos las mejores marcas con precios competitivos y tests de calidad.",
        rating: 4,
        tags: ["Testeado", "Ofertas", "Devolución"],
        category: "Gamer",
        shipping: "Entrega nacional",
        verified: true,
        img: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS__pGaeRWnX5yH0e4h_HYygEvTULzpuNISvw&s",
        url: "https://computershopperu.com/",
    },
    // TODO: Agregar nuevas tiendas
<<<<<<< HEAD
=======


>>>>>>> a4ccc035302dce290153561b8a84d7e1650f4558
];

export default function Tiendas() {
    const [categoryFilter, setCategoryFilter] = useState("");
    const [ratingFilter, setRatingFilter] = useState("");
    const [searchTerm, setSearchTerm] = useState("");

<<<<<<< HEAD

=======
    // Filtrado dinámico
>>>>>>> a4ccc035302dce290153561b8a84d7e1650f4558
    const filteredShops = SHOPS.filter((shop) => {
        const matchesCategory = categoryFilter ? shop.category === categoryFilter : true;
        const matchesRating = ratingFilter ? shop.rating >= parseInt(ratingFilter) : true;
        const matchesSearch = searchTerm
            ? shop.name.toLowerCase().includes(searchTerm.toLowerCase())
            : true;
        return matchesCategory && matchesRating && matchesSearch;
    });

    const handleResetFilters = () => {
        setCategoryFilter("");
        setRatingFilter("");
        setSearchTerm("");
    };

    return (
        <section>
<<<<<<< HEAD
            {/* Banner superior */}
=======
            {/* Banner */}
>>>>>>> a4ccc035302dce290153561b8a84d7e1650f4558
            <header id="banner1-header" className="bg-primary text-white py-5 shadow-sm">
                <div className="container text-center">
                    <h1 className="display-6 fw-bold mb-2">Tiendas Asociadas</h1>
                    <p className="lead mb-0 opacity-75">
                        Descubre nuestras tiendas colaboradoras, ofertas y promociones exclusivas.
                    </p>
                </div>
            </header>

            <main className="container my-5">
                {/* Breadcrumb */}
                <nav aria-label="breadcrumb" className="mb-4">
                    <ol className="breadcrumb bg-transparent px-0 mb-0 align-items-center">
                        <li className="breadcrumb-item text-secondary">
                            <div className="d-inline-flex align-items-center fs-5">
                                <i className="bi bi-house-door-fill text-primary me-2"></i>
<<<<<<< HEAD

                                <Link to="/" className="text-decoration-none text-secondary fw-semibold">
                                    Inicio
                                </Link>
=======
                                <a href="#" className="text-decoration-none text-secondary fw-semibold">
                                    Inicio
                                </a>
>>>>>>> a4ccc035302dce290153561b8a84d7e1650f4558
                            </div>
                        </li>
                        <li className="breadcrumb-item active fw-semibold fs-5" aria-current="page">
                            Tiendas
                        </li>
                    </ol>
                </nav>

                {/* Filtros */}
                <div className="border border-2 border-secondary-subtle rounded-4 p-4 mb-5 bg-light shadow-sm">
                    <div className="row g-3 align-items-end">
                        {/* Categoría */}
                        <div className="col-12 col-md-4">
                            <label className="form-label small fw-semibold text-secondary mb-1">Categoría</label>
                            <select
                                className="form-select form-select-sm"
                                value={categoryFilter}
                                onChange={(e) => setCategoryFilter(e.target.value)}
                            >
                                <option value="">Todas las categorías</option>
                                <option value="Gamer">Tiendas Gamer</option>
<<<<<<< HEAD
                                <option value="Basica">Tiendas de Básicos</option>
=======
                                <option value="Basica">Tiendas de Basicos</option>
>>>>>>> a4ccc035302dce290153561b8a84d7e1650f4558
                            </select>
                        </div>

                        {/* Valoración */}
                        <div className="col-12 col-md-3">
                            <label className="form-label small fw-semibold text-secondary mb-1">Valoración</label>
                            <select
                                className="form-select form-select-sm"
                                value={ratingFilter}
                                onChange={(e) => setRatingFilter(e.target.value)}
                            >
                                <option value="">Cualquier valoración</option>
                                <option value="5">5 estrellas</option>
                                <option value="4">4 estrellas o más</option>
                            </select>
                        </div>

                        {/* Buscar */}
                        <div className="col-12 col-md-4">
                            <label className="form-label small fw-semibold text-secondary mb-1">Buscar por nombre</label>
                            <div className="input-group input-group-sm">
                                <span className="input-group-text bg-white">
                                    <i className="bi bi-search text-primary"></i>
                                </span>
                                <input
                                    type="text"
                                    className="form-control"
                                    placeholder="Nombre de la tienda..."
                                    value={searchTerm}
                                    onChange={(e) => setSearchTerm(e.target.value)}
                                />
                            </div>
                        </div>

                        {/* Botón limpiar */}
                        <div className="col-12 col-md-1 d-grid">
<<<<<<< HEAD
                            <button
                                className="btn btn-outline-primary btn-sm fw-semibold"
                                onClick={handleResetFilters}
                            >
=======
                            <button className="btn btn-outline-primary btn-sm fw-semibold" onClick={handleResetFilters}>
>>>>>>> a4ccc035302dce290153561b8a84d7e1650f4558
                                Limpiar
                            </button>
                        </div>
                    </div>
                </div>

                {/* Título */}
                <div className="d-flex justify-content-between align-items-center mb-4">
<<<<<<< HEAD
                    <h4 className="fw-bold mb-0">Tiendas</h4>
=======
                    <h4 className="fw-bold mb-0">
                        Tiendas
                    </h4>
>>>>>>> a4ccc035302dce290153561b8a84d7e1650f4558
                    <span className="badge bg-secondary-subtle text-dark px-3 py-2">
                        {filteredShops.length} tiendas
                    </span>
                </div>

                {/* Grid de tiendas */}
                <div className="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-4 mb-5">
                    {filteredShops.map((s) => (
                        <div className="col" key={s.id}>
                            <ShopCard {...s} />
                        </div>
                    ))}
                </div>

                {/* Botón mostrar más */}
                <div className="text-center">
                    <button className="btn btn-outline-primary btn-sm px-4 rounded-pill fw-semibold">
                        <i className="bi bi-arrow-down-circle me-2"></i>
                        Mostrar más
                    </button>
                </div>
            </main>
        </section>
    );
}
