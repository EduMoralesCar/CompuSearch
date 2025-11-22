import React from "react";
import { useCategorias } from "../hooks/useCategorias";
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
        border: 4px solid white;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 0 auto;
        position: relative;
        z-index: 10;
    }
    .timeline-node img {
        width: 60px;
        height: auto;
        filter: brightness(0) invert(1);
    }

    /* --- Timeline Horizontal (Desktop) --- */
    .timeline-container-h {
        position: relative;
        padding-top: 10px;
        padding-bottom: 10px;
    }
    
    .timeline-container-h .row {
        position: relative;
    }
    
    /* Líneas entre círculos usando pseudo-elementos en las columnas */
    .timeline-container-h .col-md-3:not(:last-child)::after {
        content: '';
        position: absolute;
        top: 50px;
        left: calc(50% + 50px);
        width: calc(100% - 100px);
        height: 3px;
        background: linear-gradient(to right, #0d6efd 0%, #0d6efd 50%, transparent 50%, transparent 100%);
        background-size: 20px 3px;
        background-repeat: repeat-x;
        z-index: 1;
    }

    /* --- Timeline Vertical (Mobile) --- */
    .timeline-container-v {
        position: relative;
        padding-left: 0;
    }
    
    .timeline-item-v {
        text-align: center; 
        margin-bottom: 3rem;
        position: relative;
        z-index: 2;
    }
    
    .timeline-item-v:not(:last-child)::after {
        content: '';
        position: absolute;
        top: calc(50px + 50px);
        left: 50%;
        transform: translateX(-50%);
        width: 3px;
        height: calc(100% + 1rem);
        background: linear-gradient(to bottom, #0d6efd 0%, #0d6efd 50%, transparent 50%, transparent 100%);
        background-size: 3px 20px;
        background-repeat: repeat-y;
        z-index: 1;
    }
    
    .timeline-item-v .timeline-node {
        margin: 0 auto;
        flex-shrink: 0;
    }
    
    .timeline-item-v h6 {
        margin-left: 0;
        margin-top: 0.5rem;
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
