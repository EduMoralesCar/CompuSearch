import React from "react";
import useCategorias from "../hooks/useCategorias";
import BannerCarousel from "../components/home/carousels/BannerCarousel";
import CategoriasCarousel from "../components/home/carousels/CategoriasCarousel";
import TimelineSection from "../components/home/sections/TimelineSection";
import CTASection from "../components/home/sections/CTASection";
import searchIcon from "../../../assets/icons/search.png";
import compareIcon from "../../../assets/icons/compare.png";
import buildIcon from "../../../assets/icons/build.png";
import powerIcon from "../../../assets/icons/power.png";

const timelineStyles = `
    .timeline-node {
        width: 100px;
        height: 100px;
        background-color: #0d6efd;
        border: 3px solid white;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 0 auto;
        position: relative;
        z-index: 2; /* Por encima de la línea */
    }
    .timeline-node img {
        width: 60px;
        height: auto;
        filter: brightness(0) invert(1); /* Pone los iconos en blanco */
    }

    /* --- Timeline Horizontal (Desktop) --- */
    .timeline-container-h {
        position: relative;
        padding-top: 10px;
        padding-bottom: 10px;
    }
    .timeline-line-h {
        position: absolute;
        top: 60px;
        left: 15%;
        right: 15%;
        height: 4px;
        background-color: #0d6efd;
        z-index: 1; /* Por debajo de los nodos */
    }

    /* --- Timeline Vertical (Mobile) --- */
    .timeline-container-v {
        position: relative;
    }
    .timeline-line-v {
        position: absolute;
        top: 40px;
        bottom: 40px;
        left: 50%; /* Centra la línea horizontalmente */
        transform: translateX(-50%);
        width: 4px;
        background-color: #0d6efd;
        z-index: 1;
    }
    .timeline-item-v {
        text-align: center; 
        margin-bottom: 1.5rem;
        position: relative;
        z-index: 2;
    }
    .timeline-item-v .timeline-node {
        margin: 0 auto; /* Centra el nodo */
        flex-shrink: 0;
    }
    .timeline-item-v h6 {
        margin-left: 0;
        margin-top: 0.5rem; /* Espacio entre nodo y texto */
    }
`; 

const items = [
    { img: searchIcon, title: "Explora" },
    { img: compareIcon, title: "Compara" },
    { img: buildIcon, title: "Arma" },
    { img: powerIcon, title: "Potencia" },
];

const Home = () => {
    const { categorias, loading } = useCategorias();

    return (
        <div>
            <BannerCarousel />
            <CategoriasCarousel categorias={categorias} loading={loading} />
            <TimelineSection items={items} timelineStyles={timelineStyles} />
            <CTASection />
        </div>
    );
};

export default Home;
