import React, { useRef } from "react";
import { Link } from "react-router-dom";
import {
    Carousel,
    Card,
    Button,
    Container,
    Row,
    Col,
} from "react-bootstrap";

// Imágenes del carrusel (pueden seguir importándose si están en src)
import banner1 from "../../../assets/banners/banner_home_1.webp";
import banner2 from "../../../assets/banners/banner_home_2.jpg";

// Íconos desde src (puedes moverlos a public si prefieres)
import searchIcon from "../../../assets/icon/search.png";
import compareIcon from "../../../assets/icon/compare.png";
import buildIcon from "../../../assets/icon/build.png";
import powerIcon from "../../../assets/icon/power.png";

// Categorías desde public/assets/categorias
const categorias = [
    { name: "Procesador", img: "/assets/categorias/procesador.jpg" },
    { name: "Placa Madre", img: "/assets/categorias/placa_madre.jpg" },
    { name: "Memoria RAM", img: "/assets/categorias/memoria_ram.jpg" },
    { name: "Almacenamiento", img: "/assets/categorias/almacenamiento.jpg" },
    { name: "Tarjeta Gráfica", img: "/assets/categorias/tarjeta_video.jpg" },
    { name: "Fuente de Poder", img: "/assets/categorias/fuente_poder.jpg" },
    { name: "Refrigeración CPU", img: "/assets/categorias/refrigeracion_cpu.jpg" },
];

const items = [
    { img: searchIcon, title: "Explora" },
    { img: compareIcon, title: "Compara" },
    { img: buildIcon, title: "Arma" },
    { img: powerIcon, title: "Potencia" },
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

    return (
        <div>
            {/* Carrusel principal */}
            <Carousel fade>
                <Carousel.Item>
                    <img className="d-block w-100" src={banner1} alt="First slide" />
                    <Carousel.Caption className="bg-dark bg-opacity-50 p-3 rounded">
                        <h3 className="text-white">Bienvenido a CompuSearch</h3>
                        <p className="text-white">Encuentra los mejores componentes para tu PC</p>
                    </Carousel.Caption>
                </Carousel.Item>
                <Carousel.Item>
                    <img className="d-block w-100" src={banner2} alt="Second slide" />
                </Carousel.Item>
            </Carousel>

            {/* Categorías */}
            <Container fluid className="position-relative py-5">
                <Row className="text-center mb-4">
                    <Col>
                        <h4 className="fw-bold">CATEGORÍAS POPULARES</h4>
                    </Col>
                </Row>

                {/* Flecha izquierda */}
                <Button
                    variant="outline-secondary"
                    className="position-absolute top-50 translate-middle-y shadow rounded-circle"
                    style={{ left: "10px", zIndex: 3 }}
                    onClick={() => scroll("left")}
                >
                    <i className="bi bi-chevron-left fs-4"></i>
                </Button>

                {/* Contenedor cards */}
                <div
                    ref={scrollRef}
                    className="d-flex overflow-auto gap-3 px-3"
                >
                    {categorias.map((cat) => (
                        <Link
                            key={cat.name}
                            to={`/componentes?categoria=${encodeURIComponent(cat.name)}`}
                            style={{ textDecoration: "none", color: "inherit" }}
                        >
                            <Card className="shadow-sm border-0" style={{ minWidth: "200px" }}>
                                <Card.Img src={cat.img} alt={cat.name} style={{ height: "150px", objectFit: "cover" }} />
                                <Card.Body className="text-center bg-light">
                                    <Card.Text className="fw-semibold">{cat.name}</Card.Text>
                                </Card.Body>
                            </Card>
                        </Link>
                    ))}

                </div>

                {/* Flecha derecha */}
                <Button
                    variant="outline-secondary"
                    className="position-absolute top-50 translate-middle-y shadow rounded-circle"
                    style={{ right: "10px", zIndex: 3 }}
                    onClick={() => scroll("right")}
                >
                    <i className="bi bi-chevron-right fs-4"></i>
                </Button>
            </Container>

            {/* Sección ¿Por qué usar CompuSearch? */}
            <div className="bg-dark text-white py-5">
                <Container>
                    <h3 className="text-center mb-4">¿Por qué usar CompuSearch?</h3>

                    <div className="bg-primary text-white p-4 rounded">
                        <Row className="justify-content-center text-center">
                            {items.map((item, idx) => (
                                <Col key={idx} xs={6} md={3} className="mb-4">
                                    <div className="d-flex flex-column align-items-center">
                                        <img src={item.img} alt={item.title} className="mb-2" style={{ width: "50px", height: "50px" }} />
                                        <div className="bg-white rounded-circle mb-2" style={{ width: "10px", height: "10px" }}></div>
                                        <h6 className="fw-bold text-white">{item.title}</h6>
                                    </div>
                                </Col>
                            ))}
                        </Row>

                        <div className="text-center mt-4">
                            <h5 className="fw-bold">CON MEJORES PRECIOS DEL MERCADO</h5>
                        </div>
                    </div>

                    {/* CTA Armado */}
                    <Row className="justify-content-center align-items-center text-center mt-5">
                        <Col xs={12} md="auto">
                            <h4 className="fw-bold mb-3 mb-md-0">¿Armando una PC desde cero?</h4>
                        </Col>
                        <Col xs={12} md="auto">
                            <Button
                                as={Link}
                                to="/builds"
                                variant="success"
                                className="fw-bold px-4 py-2"
                            >
                                Comienza a armar tu PC
                            </Button>
                        </Col>
                    </Row>
                </Container>
            </div>
        </div>
    );
};

export default Home;
