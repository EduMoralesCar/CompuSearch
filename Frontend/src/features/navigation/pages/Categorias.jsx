import React from "react";
<<<<<<< HEAD
import { Link } from "react-router-dom";
=======
>>>>>>> a4ccc035302dce290153561b8a84d7e1650f4558
import CategoryCard from "../../../components/CategoryCard";

/**
 * src/features/navigation/pages/Categorias.jsx
 * Página de listado de categorías.
 * Export default function Categorias para coincidir con AppRoute import.
 */

const CATEGORIES = [
    {
        id: "procesadores",
        title: "Procesadores",
        description:
            "Unidad central de procesamiento (CPU), el cerebro de la computadora que ejecuta instrucciones y procesa datos.",
        subcategories: ["Intel Core i3/5/7/9", "AMD Ryzen 3/5/7/9", "Procesadores para servidores", "Procesadores económicos"],
        img: "/src/assets/categorias/cat_cpu.jpg",
    },
    {
        id: "tarjetas-graficas",
        title: "Tarjetas Gráficas",
        description:
            "Unidades de procesamiento gráfico (GPU) que renderizan imágenes, vídeo y animaciones para su visualización.",
        subcategories: ["NVIDIA GeForce RTX", "AMD Radeon RX", "Tarjetas para gaming", "Tarjetas para trabajo"],
        img: "/src/assets/categorias/cat_gpu.jpg",
    },
    {
        id: "placas-base",
        title: "Placas Base",
        description: "La placa base conecta todos los componentes de la computadora y permite su comunicación.",
        subcategories: ["Socket AM4/AM5", "Socket LGA 1700", "Placas ATX", "Placas Micro-ATX"],
        img: "/src/assets/categorias/cat_placabase.jpg",
    },
    {
        id: "memorias-ram",
        title: "Memorias RAM",
        description:
            "Memoria de acceso aleatorio que almacena datos temporales para un acceso rápido por parte del procesador.",
        subcategories: ["DDR4", "DDR5", "RAM para gaming", "Kits de memoria"],
        img: "/src/assets/categorias/cat_ram.jpg",
    },
    {
        id: "almacenamiento",
        title: "Almacenamiento",
        description: "Dispositivos para guardar datos permanentemente, incluyendo SSD, HDD y unidades externas.",
        subcategories: ["SSD SATA", "SSD NVMe", "Discos duros HDD", "Unidades externas"],
        img: "/src/assets/categorias/cat_almacenamiento.jpg",
    },
    {
        id: "fuentes",
        title: "Fuente de Poder",
        description:
            "Convierte la energía de CA a DC y suministra energía estable a todos los componentes del sistema.",
        subcategories: ["80 Plus Bronze", "80 Plus Gold", "80 Plus Platinum", "Fuentes modulares"],
        img: "/src/assets/categorias/cat_fuentepoder.jpg",
    },
    {
        id: "gabinetes",
        title: "Gabinete",
        description: "La carcasa que alberga y protege todos los componentes internos de la computadora.",
        subcategories: ["Gabinetes ATX", "Gabinetes Micro-ATX", "Gabinetes Mini-ITX", "Gabinetes con RGB"],
        img: "/src/assets/categorias/cat_gabinete.jpg",
    },
    {
        id: "refrigeracion",
        title: "Refrigeración",
        description: "Sistemas para mantener temperaturas óptimas en los componentes, evitando el sobrecalentamiento.",
        subcategories: ["Refrigeración líquida", "Disipadores de aire", "Ventiladores", "Pasta térmica"],
        img: "/src/assets/categorias/cat_refrigeracion.jpg",
    },
    {
        id: "otros",
        title: "Otros",
        description: "Complementos y periféricos: monitor, mousepad, auriculares, estabilizador, etc.",
        subcategories: ["Monitor", "Mouse Pad", "Auriculares", "Entre otros"],
        img: "/src/assets/categorias/cat_otros.jpg",
    },
];

export default function Categorias() {
    return (
        <section>
<<<<<<< HEAD
            {/* Banner superior */}
            <header id="banner1-header" className="bg-primary text-white py-5">
                <div className="container text-center">
                    <h1 className="display-6 fw-bold text-uppercase">
                        Categorías de Componentes
                    </h1>
                    <p className="lead mb-0 fw-italic">
                        Explora y compara precios de todos los componentes de computadora en un solo lugar.
                    </p>
=======

            <header id="banner1-header" className="bg-primary text-white py-5" >
                <div className="container text-center" >
                    <h1 className="display-6 fw-bold text-uppercase">Categorías de Componentes</h1>
                    <p className="lead mb-0 fw-italic">Explora y compara precios de todos los componentes de computadora en un solo lugar.</p>
>>>>>>> a4ccc035302dce290153561b8a84d7e1650f4558
                </div>
            </header>

            <main className="container my-5">
<<<<<<< HEAD
                {/* 🧭 Breadcrumb */}
=======

>>>>>>> a4ccc035302dce290153561b8a84d7e1650f4558
                <nav aria-label="breadcrumb" className="mb-4">
                    <ol className="breadcrumb bg-transparent px-0 mb-0 align-items-center">
                        <li className="breadcrumb-item text-secondary">
                            <div className="d-inline-flex align-items-center fs-5">
                                <i className="bi bi-house-door-fill text-primary me-2"></i>
<<<<<<< HEAD
                                <Link
                                    to="/"
                                    className="text-decoration-none text-secondary fw-semibold"
                                >
                                    Inicio
                                </Link>
                            </div>
                        </li>
                        <li
                            className="breadcrumb-item active fw-semibold fs-5"
                            aria-current="page"
                        >
=======
                                <a href="./Home.jsx" className="text-decoration-none text-secondary fw-semibold">
                                    Inicio
                                </a>
                            </div>
                        </li>
                        <li className="breadcrumb-item active fw-semibold fs-5" aria-current="page">
>>>>>>> a4ccc035302dce290153561b8a84d7e1650f4558
                            Categorías
                        </li>
                    </ol>
                </nav>

<<<<<<< HEAD
                <h4 className="mb-4 fw-bold text-uppercase border-bottom pb-2">
                    Todos los componentes
                </h4>
=======
                <h4 className="mb-4 fw-bold text-uppercase border-bottom pb-2">Todos los componentes</h4>
>>>>>>> a4ccc035302dce290153561b8a84d7e1650f4558

                <div className="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-4">
                    {CATEGORIES.map((c) => (
                        <div className="col" key={c.id}>
                            <CategoryCard {...c} />
                        </div>
                    ))}
                </div>
            </main>
        </section>
    );
<<<<<<< HEAD
}
=======
}
>>>>>>> a4ccc035302dce290153561b8a84d7e1650f4558
