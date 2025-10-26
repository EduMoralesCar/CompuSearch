import React, { useRef } from "react";
import { Link } from "react-router-dom";
import { Card, Button, Container, Row, Col } from "react-bootstrap";

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
            <Row className="text-center mb-4">
                <Col>
                    <h4 className="fw-bold">CATEGORÍAS POPULARES</h4>
                </Col>
            </Row>

            <Button
                variant="outline-secondary"
                className="position-absolute top-50 translate-middle-y shadow rounded-circle"
                style={{ left: "10px", zIndex: 3 }}
                onClick={() => scroll("left")}
            >
                <i className="bi bi-chevron-left fs-4"></i>
            </Button>

            <div ref={scrollRef} className="overflow-x-auto" style={{ scrollbarWidth: "none", msOverflowStyle: "none" }}>
                {loading ? (
                    <p>Cargando categorías...</p>
                ) : (
                    categorias.length > 0 && (
                        <Row className="flex-nowrap gx-3 px-3">
                            {categorias.map((cat) => (
                                <Col xs={6} md={4} lg={3} key={cat.nombre}>
                                    <Link
                                        to={`/componentes?categoria=${encodeURIComponent(cat.nombre)}`}
                                        style={{ textDecoration: "none", color: "inherit" }}
                                    >
                                        <Card className="shadow-sm border-0 h-100">
                                            <Card.Img
                                                src={`/assets/categorias/${cat.nombreImagen}`}
                                                alt={cat.nombre}
                                                style={{ height: "150px", width: "100%", objectFit: "cover" }}
                                            />
                                            <Card.Body className="text-center bg-light">
                                                <Card.Text className="fw-semibold">{cat.nombre}</Card.Text>
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
                variant="outline-secondary"
                className="position-absolute top-50 translate-middle-y shadow rounded-circle"
                style={{ right: "10px", zIndex: 3 }}
                onClick={() => scroll("right")}
            >
                <i className="bi bi-chevron-right fs-4"></i>
            </Button>
        </Container>
    );
};

export default CategoriasCarousel;
