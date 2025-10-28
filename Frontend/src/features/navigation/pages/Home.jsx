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

// Imágenes del carrusel
import banner1 from "../../../assets/banners/banner_home_1.webp";
import banner2 from "../../../assets/banners/banner_home_2.jpg";

// Íconos
import searchIcon from "../../../assets/icon/search.png";
import compareIcon from "../../../assets/icon/compare.png";
import buildIcon from "../../../assets/icon/build.png";
import powerIcon from "../../../assets/icon/power.png";

// Categorías
const categorias = [
    { name: "Procesador", img: "/assets/categorias/procesador.jpg" },
    { name: "Placa Madre", img: "/assets/categorias/placa_madre.jpg" },
    { name: "Memoria RAM", img: "/assets/categorias/memoria_ram.jpg" },
    { name: "Almacenamiento", img: "/assets/categorias/almacenamiento.jpg" },
    { name: "Tarjeta de Video", img: "/assets/categorias/tarjeta_video.jpg" },
    { name: "Fuente de Poder", img: "/assets/categorias/fuente_poder.jpg" },
    { name: "Refrigeración CPU", img: "/assets/categorias/refrigeracion_cpu.jpg" },
];

// Items de “Cómo funciona”
const items = [
    { img: searchIcon, title: "Explora" },
    { img: compareIcon, title: "Compara" },
    { img: buildIcon, title: "Arma" },
    { img: powerIcon, title: "Potencia" },
];

// 🎨 Estilos Modernos
const timelineStyles = `
/* ============================
   ESTILOS BASE
============================ */
body {
  font-family: "Poppins", sans-serif;
}

.card {
  border-radius: 16px;
  transition: all 0.3s ease-in-out;
}
.card:hover {
  transform: translateY(-6px);
  box-shadow: 0 8px 20px rgba(0,0,0,0.15);
}

.btn {
  border-radius: 50px !important;
  transition: all 0.3s ease;
}
.btn:hover {
  transform: scale(1.05);
}

/* ============================
   CARRUSEL PRINCIPAL
============================ */
.carousel-caption {
  background: rgba(0, 0, 0, 0.45);
  border-radius: 12px;
  backdrop-filter: blur(4px);
}

/* ============================
   CATEGORÍAS
============================ */
.categorias-container {
  background: #f8f9fa;
  border-radius: 16px;
  padding: 40px 20px;
}
.card img {
  border-top-left-radius: 16px;
  border-top-right-radius: 16px;
}
.card-body {
  background: #ffffff;
  border-bottom-left-radius: 16px;
  border-bottom-right-radius: 16px;
}
.card-body:hover {
  background: #e9f2ff;
}

/* ============================
   TIMELINE
============================ */
.timeline-node {
  width: 110px;
  height: 110px;
  background: linear-gradient(135deg, #0d6efd, #3f9cff);
  border: 3px solid #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
  position: relative;
  box-shadow: 0 0 12px rgba(13, 110, 253, 0.4);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.timeline-node:hover {
  transform: scale(1.1);
  box-shadow: 0 0 20px rgba(13, 110, 253, 0.8);
}
.timeline-node img {
  width: 55px;
  height: auto;
  filter: brightness(0) invert(1);
}

/* --- Horizontal (Desktop) --- */
.timeline-container-h {
  position: relative;
  padding: 40px 0;
}
.timeline-line-h {
  position: absolute;
  top: 65px;
  left: 12%;
  right: 12%;
  height: 5px;
  background: linear-gradient(to right, #0d6efd, #3f9cff);
  border-radius: 3px;
  z-index: 1;
}
.timeline-container-h h6 {
  margin-top: 1rem;
  color: #f8f9fa;
}

/* --- Vertical (Mobile) --- */
.timeline-container-v {
  position: relative;
  padding: 2rem 0;
}
.timeline-line-v {
  position: absolute;
  top: 50px;
  bottom: 40px;
  left: 50%;
  transform: translateX(-50%);
  width: 4px;
  background: linear-gradient(to bottom, #0d6efd, #3f9cff);
  z-index: 1;
  border-radius: 3px;
}
.timeline-item-v {
  text-align: center; 
  margin-bottom: 2rem;
  position: relative;
  z-index: 2;
}
.timeline-item-v h6 {
  margin-top: 0.75rem;
  font-weight: 600;
  letter-spacing: 0.3px;
}

/* ============================
   CTA FINAL
============================ */
.cta-section {
  background: linear-gradient(135deg, #0d6efd, #198754);
  color: white;
  padding: 2.5rem 1rem;
  border-radius: 20px;
  box-shadow: 0 0 25px rgba(0,0,0,0.25);
}
.cta-section h4 {
  color: #fff;
}
`;

const Home = () => {
    const scrollRef = useRef(null);

    const scroll = (direction) => {
        if (!scrollRef.current) return;

        const { scrollLeft, scrollWidth, clientWidth } = scrollRef.current;
        const row = scrollRef.current.children[0];
        let scrollAmount = 260;

        if (row && row.children[1]) {
            const firstItem = row.children[0];
            const secondItem = row.children[1];
            scrollAmount = secondItem.offsetLeft - firstItem.offsetLeft;
        }

        if (direction === "left") {
            if (scrollLeft === 0) {
                scrollRef.current.scrollTo({ left: scrollWidth - clientWidth, behavior: "smooth" });
            } else {
                scrollRef.current.scrollBy({ left: -scrollAmount, behavior: "smooth" });
            }
        } else {
            const maxScrollLeft = scrollWidth - clientWidth;
            if (scrollLeft >= maxScrollLeft - 10) {
                scrollRef.current.scrollTo({ left: 0, behavior: "smooth" });
            } else {
                scrollRef.current.scrollBy({ left: scrollAmount, behavior: "smooth" });
            }
        }
    };

    return (
        <div>
            <style>{timelineStyles}</style>

            {/* 🖼 Carrusel principal */}
            <Carousel fade>
                <Carousel.Item>
                    <img className="d-block w-100" src={banner1} alt="First slide" />
                    <Carousel.Caption>
                        <h3 className="text-white fs-4 fs-md-2">Bienvenido a CompuSearch</h3>
                        <p className="text-white d-none d-md-block">
                            Encuentra los mejores componentes para tu PC
                        </p>
                    </Carousel.Caption>
                </Carousel.Item>
                <Carousel.Item>
                    <img className="d-block w-100" src={banner2} alt="Second slide" />
                </Carousel.Item>
            </Carousel>

            {/* 💻 Categorías Populares */}
            <Container fluid className="position-relative py-5 categorias-container">
                <Row className="text-center mb-4">
                    <Col>
                        <h4 className="fw-bold text-uppercase text-primary">
                            Categorías Populares
                        </h4>
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

                {/* Contenedor scrollable */}
                <div
                    ref={scrollRef}
                    className="overflow-x-auto"
                    style={{ scrollbarWidth: "none", msOverflowStyle: "none" }}
                >
                    <Row className="flex-nowrap gx-3 px-3">
                        {categorias.map((cat) => (
                            <Col xs={6} md={4} lg={3} key={cat.name}>
                                <Link
                                    to={`/componentes?categoria=${encodeURIComponent(cat.name)}`}
                                    style={{ textDecoration: "none", color: "inherit" }}
                                >
                                    <Card className="shadow-sm border-0 h-100">
                                        <Card.Img
                                            src={cat.img}
                                            alt={cat.name}
                                            style={{ height: "160px", objectFit: "cover" }}
                                        />
                                        <Card.Body className="text-center bg-light">
                                            <Card.Text className="fw-semibold">
                                                {cat.name}
                                            </Card.Text>
                                        </Card.Body>
                                    </Card>
                                </Link>
                            </Col>
                        ))}
                    </Row>
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

            {/* ⚙️ Cómo Funciona */}
            <div
                className="py-5"
                style={{
                    background: "linear-gradient(135deg, #0d6efd 10%, #1c1c1c 90%)",
                    color: "white",
                }}
            >
                <Container>
                    <h3 className="text-center mb-5 fw-bold">¿CÓMO FUNCIONA COMPUSEARCH?</h3>

                    {/* Timeline Horizontal */}
                    <div className="d-none d-md-block timeline-container-h">
                        <div className="timeline-line-h"></div>
                        <Row>
                            {items.map((item, idx) => (
                                <Col md={3} key={idx} className="text-center">
                                    <div className="timeline-node">
                                        <img src={item.img} alt={item.title} />
                                    </div>
                                    <h6 className="fw-bold h5 text-white mt-3">{item.title}</h6>
                                </Col>
                            ))}
                        </Row>
                    </div>

                    {/* Timeline Vertical */}
                    <div className="d-md-none timeline-container-v">
                        <div className="timeline-line-v"></div>
                        {items.map((item, idx) => (
                            <div key={idx} className="timeline-item-v">
                                <div className="timeline-node">
                                    <img src={item.img} alt={item.title} />
                                </div>
                                <h6 className="fw-bold text-white">{item.title}</h6>
                            </div>
                        ))}
                    </div>

                    {/* Frase destacada */}
                    <div className="text-center mt-5">
                        <div className="bg-primary d-inline-block p-3 rounded shadow">
                            <h5 className="fw-bold text-white m-0">
                                CON MEJORES PRECIOS DEL MERCADO
                            </h5>
                        </div>
                    </div>

                    {/* CTA Final */}
                    <div className="cta-section text-center mt-5">
                        <h4 className="fw-bold mb-3">¿Armando una PC desde cero?</h4>
                        <Button
                            as={Link}
                            to="/builds"
                            variant="light"
                            className="fw-bold px-4 py-2"
                        >
                            Comienza a armar tu PC
                        </Button>
                    </div>
                </Container>
            </div>
        </div>
    );
};

export default Home;
