import React, { useRef } from "react";
import { Link } from "react-router-dom";
import { Card, Button, Container, Row, Col } from "react-bootstrap";

/**
 * Componente de estilos para el efecto de Borde Animado.
 */
const CustomCarouselStylesBorder = () => (
  <style>{`
    .category-card-border {
      position: relative;
      transition: all 0.3s ease;
      border-radius: 0.75rem;
      overflow: hidden;
      border: 2px solid transparent;
      background: white;
    }

    .category-card-border:hover {
      border-color: #0d6efd;
      box-shadow: 0 8px 20px rgba(13, 110, 253, 0.2);
      transform: translateY(-4px);
    }

    .category-card-border .card-img {
      height: 160px;
      object-fit: cover;
      width: 100%;
      display: block;
    }

    .category-title-overlay {
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      background: linear-gradient(to top, rgba(13, 110, 253, 0.95) 0%, rgba(13, 110, 253, 0.85) 100%);
      padding: 0.75rem 1rem;
      transform: translateY(0);
      transition: all 0.3s ease;
    }

    .category-card-border:hover .category-title-overlay {
      background: linear-gradient(to top, rgba(13, 110, 253, 1) 0%, rgba(13, 110, 253, 0.95) 100%);
    }

    .category-title-text {
      color: white;
      font-weight: 600;
      font-size: 0.95rem;
      margin: 0;
      text-align: center;
      line-height: 1.3;
      text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
    }

    .carousel-container {
      scroll-behavior: smooth;
      scroll-snap-type: x mandatory;
      -webkit-overflow-scrolling: touch;
    }

    .carousel-item-col {
      scroll-snap-align: start;
      scroll-snap-stop: always;
    }

    @keyframes fadeInSlide {
      from {
        opacity: 0;
        transform: translateX(30px) scale(0.95);
      }
      to {
        opacity: 1;
        transform: translateX(0) scale(1);
      }
    }

    .category-card-border {
      animation: fadeInSlide 0.4s ease-out;
    }

    .carousel-container::-webkit-scrollbar {
      display: none;
    }
    .carousel-arrow-btn {
      background-color: rgba(255, 255, 255, 0.9) !important;
      backdrop-filter: blur(5px);
      border: none !important;
      transition: background-color 0.2s ease, color 0.2s ease, transform 0.2s ease;
    }

    .carousel-arrow-btn:hover {
      background-color: rgba(0, 0, 0, 0.8) !important;
      color: white !important;
      transform: scale(1.1);
    }
  `}</style>
);


const CategoriasCarousel = ({ categorias, loading }) => {
  const scrollRef = useRef(null);

  const scroll = (direction) => {
    if (!scrollRef.current) return;
    const { scrollLeft, scrollWidth, clientWidth } = scrollRef.current;

    let scrollAmount = 0;
    const row = scrollRef.current.children[0];
    if (row && row.children[1]) {
      scrollAmount = row.children[1].offsetLeft - row.children[0].offsetLeft;
    } else if (row && row.children[0]) {
      scrollAmount = row.children[0].clientWidth;
    } else {
      scrollAmount = 260;
    }

    if (direction === "left") {
      scrollRef.current.scrollLeft === 0
        ? scrollRef.current.scrollTo({ left: scrollWidth - clientWidth, behavior: "smooth" })
        : scrollRef.current.scrollBy({ left: -scrollAmount, behavior: "smooth" });
    } else {
      const maxScrollLeft = scrollWidth - clientWidth;
      scrollLeft >= maxScrollLeft - 10
        ? scrollRef.current.scrollTo({ left: 0, behavior: "smooth" })
        : scrollRef.current.scrollBy({ left: scrollAmount, behavior: "smooth" });
    }
  };

  return (
    <Container fluid className="position-relative py-5">
      <CustomCarouselStylesBorder />

      <Row className="text-center mb-4">
        <Col>
          <h4 className="fw-bold text-primary border-bottom pb-2 d-inline-block">
            CATEGORÍAS POPULARES
          </h4>
        </Col>
      </Row>

      <Button
        className="position-absolute top-50 translate-middle-y shadow rounded-circle d-flex justify-content-center align-items-center carousel-arrow-btn"
        style={{ left: "10px", zIndex: 3, width: "45px", height: "45px" }}
        onClick={() => scroll("left")}
      >
        <i className="bi bi-chevron-left fs-4"></i>
      </Button>

      <div
        ref={scrollRef}
        className="overflow-x-auto carousel-container"
        style={{
          scrollbarWidth: "none",
          msOverflowStyle: "none",
          scrollBehavior: "smooth"
        }}
      >
        {loading ? (
          <p>Cargando categorías...</p>
        ) : (
          categorias.length > 0 && (
            <Row className="flex-nowrap gx-3 px-3 py-3">
              {categorias.map((cat) => (
                <Col xs={6} md={3} lg={2} key={cat.nombre} className="d-flex carousel-item-col">
                  <Link
                    to={`/componentes?categoria=${encodeURIComponent(cat.nombre)}`}
                    style={{ textDecoration: "none", color: "inherit" }}
                    className="w-100"
                  >
                    <Card className="category-card-border h-100">
                      <Card.Img
                        src={
                          cat.nombreImagen && (cat.nombreImagen.startsWith('http://') || cat.nombreImagen.startsWith('https://'))
                            ? cat.nombreImagen
                            : `/assets/categorias/${cat.nombreImagen}`
                        }
                        alt={cat.nombre}
                      />
                      <div className="category-title-overlay">
                        <p className="category-title-text">{cat.nombre}</p>
                      </div>
                    </Card>
                  </Link>
                </Col>
              ))}
            </Row>
          )
        )}
      </div>

      <Button
        className="position-absolute top-50 translate-middle-y shadow rounded-circle d-flex justify-content-center align-items-center carousel-arrow-btn"
        style={{ right: "10px", zIndex: 3, width: "45px", height: "45px" }}
        onClick={() => scroll("right")}
      >
        <i className="bi bi-chevron-right fs-4"></i>
      </Button>
    </Container>
  );
};

export default CategoriasCarousel;

