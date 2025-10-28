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
      transition: box-shadow 0.3s ease;
      border-radius: 0.5rem;
      border: 1px solid #eee; 
      overflow: hidden;
      display: flex;
      flex-direction: column;
    }

    
    .category-card-border::after {
      content: '';
      position: absolute;
      bottom: 0;
      left: 0;
      width: 100%;
      height: 4px; 
      background-color: var(--bs-primary, #0d6efd); 
      
      transform: scaleX(0); 
      transform-origin: center; 
      transition: transform 0.3s ease-out;
    }

    .category-card-border:hover {
     
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08) !important;
    }

    .category-card-border:hover::after {
      transform: scaleX(1);
    }

    .category-card-border .card-img {
      height: 120px;
      object-fit: cover;
      border-bottom: 1px solid #eee;
      flex-shrink: 0; 
    }

    .category-card-border .card-body {
      flex-grow: 1;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      padding: 0.75rem;
    }

    .category-card-border .card-text {
      font-weight: 600;
      color: #333;
      margin-bottom: 0;
      font-size: 0.9rem;
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
              className="overflow-x-auto" 
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
                                <Col xs={6} md={3} lg={2} key={cat.nombre} className="d-flex">
                                    <Link
                                        to={`/componentes?categoria=${encodeURIComponent(cat.nombre)}`}
                                        style={{ textDecoration: "none", color: "inherit" }}
                                        className="w-100"
                                    >
                                        <Card className="shadow-sm category-card-border h-100">
                                            <Card.Img
                                                variant="top"
                                                src={`/assets/categorias/${cat.nombreImagen}`}
                                                alt={cat.nombre}
                                            />
                                            <Card.Body className="text-center">
                                                <Card.Text>
                                                    {cat.nombre}
                                                </Card.Text>
                                            </Card.Body>
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

