import React, { useRef } from "react";
import { Link } from "react-router-dom";
import { Carousel, Card, Button } from "react-bootstrap";
import "./home.css";

import banner1 from "../assets/banner1.webp";
import banner2 from "../assets/banner2.jpg";

import searchIcon from "../assets/icons/search.png";
import compareIcon from "../assets/icons/compare.png";
import buildIcon from "../assets/icons/build.png";
import powerIcon from "../assets/icons/power.png";

import cpuImg from "../assets/categorias/cpu.webp";
import ramImg from "../assets/categorias/ram.png";
import ssdImg from "../assets/categorias/ssd.png";
import gpuImg from "../assets/categorias/gpu.png";
import psuImg from "../assets/categorias/psu.avif";
import motherboardImg from "../assets/categorias/motherboard.png";
import coolerImg from "../assets/categorias/cooler.webp";

const categorias = [
  { name: "CPU", img: cpuImg },
  { name: "Motherboard", img: motherboardImg },
  { name: "RAM", img: ramImg },
  { name: "SSD/HDD", img: ssdImg },
  { name: "GPU", img: gpuImg },
  { name: "Fuente de Poder", img: psuImg },
  { name: "Disipador", img: coolerImg },
];

const Home = () => {
  const scrollRef = useRef(null);

  const scroll = (direction) => {
    if (!scrollRef.current) return;
    const scrollAmount = 260;
    scrollRef.current.scrollBy({
      left: direction === "left" ? -scrollAmount : scrollAmount,
      behavior: "smooth",
    });
  };

  const items = [
    { img: searchIcon, title: "Explora" },
    { img: compareIcon, title: "Compara" },
    { img: buildIcon, title: "Arma" },
    { img: powerIcon, title: "Potencia" },
  ];

  return (
    <div>
      {/* Carrusel principal */}
      <Carousel fade>
        <Carousel.Item>
          <img className="d-block w-100" src={banner1} alt="First slide" />
        </Carousel.Item>
        <Carousel.Item>
          <img className="d-block w-100" src={banner2} alt="Second slide" />
        </Carousel.Item>
      </Carousel>

      {/* Categorías */}
      <div className="categorias-section position-relative">
        <div className="text-center">
          <h4 className="categorias-title mb-4">CATEGORÍAS POPULARES</h4>
        </div>

        {/* Flecha izquierda */}
        <Button
          variant="light"
          className="scroll-btn position-absolute top-50 start-0 translate-middle-y shadow"
          onClick={() => scroll("left")}
          style={{ zIndex: 3 }}
        >
          <i className="bi bi-chevron-left"></i>
        </Button>

        {/* Contenedor cards */}
        <div
          ref={scrollRef}
          className="categorias-container d-flex overflow-auto gap-3"
        >
          {categorias.map((cat, idx) => (
            <Card key={idx} className="categoria-card shadow-sm">
              <div className="categoria-img-container">
                <img src={cat.img} alt={cat.name} className="categoria-img" />
                <div className="categoria-overlay" />
                <div className="categoria-text">
                  <span>{cat.name}</span>
                </div>
              </div>
            </Card>
          ))}
        </div>

        {/* Flecha derecha */}
        <Button
          variant="light"
          className="scroll-btn position-absolute top-50 end-0 translate-middle-y shadow"
          onClick={() => scroll("right")}
          style={{ zIndex: 3 }}
        >
          <i className="bi bi-chevron-right"></i>
        </Button>
      </div>

      {/* Sección ¿Por qué usar CompuSearch? */}
      <div className="timeline-section bg-dark text-white py-4">
        <h3 className="text-center mt-2 mb-4">¿Por qué usar CompuSearch?</h3>

        <div className="container timeline-container bg-primary text-white p-4 rounded">
          <div className="timeline d-flex justify-content-between position-relative">
            {items.map((item, idx) => (
              <div key={idx} className="timeline-item text-center flex-fill">
                <img
                  src={item.img}
                  alt={item.title}
                  className="timeline-icon"
                />
                <div className="timeline-line"></div>
                <div className="timeline-dot"></div>
                <h6 className="mt-0 h4 fw-bold">{item.title}</h6>
              </div>
            ))}
          </div>

          <div className="text-center mt-4">
            <h5 className="fw-bold">CON MEJORES PRECIOS DEL MERCADO</h5>
          </div>
        </div>

        {/* Sección ¿Armando una PC desde cero? */}
        <div className="d-flex flex-column flex-md-row justify-content-center align-items-center text-center mt-4 mb-3 gap-3 gap-md-5">
          <h4 className="mb-3 mb-md-0 fw-bold">¿Armando una PC desde cero?</h4>
          <Button
            as={Link}
            to="/builds"
            className="custom-build-btn fw-bold px-4 py-2"
          >
            Comienza a armar tu PC
          </Button>
        </div>
      </div>
    </div>
  );
};

export default Home;
