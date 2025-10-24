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

// Definición de categorías
const categorias = [
    { name: "Procesador", img: "/assets/categorias/procesador.jpg" },
    { name: "Placa Madre", img: "/assets/categorias/placa_madre.jpg" },
    { name: "Memoria RAM", img: "/assets/categorias/memoria_ram.jpg" },
    { name: "Almacenamiento", img: "/assets/categorias/almacenamiento.jpg" },
    { name: "Tarjeta de Video", img: "/assets/categorias/tarjeta_video.jpg" },
    { name: "Fuente de Poder", img: "/assets/categorias/fuente_poder.jpg" },
    { name: "Refrigeración CPU", img: "/assets/categorias/refrigeracion_cpu.jpg" },
];

// Items para la sección "cómo funciona"
const items = [
    { img: searchIcon, title: "Explora" },
    { img: compareIcon, title: "Compara" },
    { img: buildIcon, title: "Arma" },
    { img: powerIcon, title: "Potencia" },
];

// Estilos CSS para la timeline (horizontal y vertical)
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


const Home = () => {
    // Ref para controlar el scroll de las categorías
    const scrollRef = useRef(null);

    // Función para mover el scroll con lógica de "loop infinito"
    const scroll = (direction) => {
        if (!scrollRef.current) return;

        const { scrollLeft, scrollWidth, clientWidth } = scrollRef.current;
        
        // Calcula el ancho de una columna dinámicamente
        let scrollAmount = 0;
        const row = scrollRef.current.children[0]; // El <Row>
        if (row && row.children[1]) {
            const firstItem = row.children[0];
            const secondItem = row.children[1];
            scrollAmount = secondItem.offsetLeft - firstItem.offsetLeft;
        } else if (row && row.children[0]) {
            scrollAmount = row.children[0].clientWidth;
        } else {
            scrollAmount = 260; // Fallback
        }


        if (direction === "left") {
            // Si está en el inicio, salta al final
            if (scrollLeft === 0) {
                scrollRef.current.scrollTo({
                    left: scrollWidth - clientWidth, // Salta al final
                    behavior: "smooth",
                });
            } else {
                scrollRef.current.scrollBy({ left: -scrollAmount, behavior: "smooth" });
            }
        } else {
            const maxScrollLeft = scrollWidth - clientWidth;
            
            // Comprueba si ya estamos en el final (con un buffer de 10px por si acaso)
            if (scrollLeft >= maxScrollLeft - 10) { 
                // Si sí, salta al inicio
                scrollRef.current.scrollTo({
                    left: 0,
                    behavior: "smooth",
                });
            } else {
                // Si no, solo avanza.scrollBy se detendrá automáticamente al llegar al final.
                scrollRef.current.scrollBy({ left: scrollAmount, behavior: "smooth" });
            }
        }
    };

    return (
        <div>
            {/* Inyecta los estilos CSS de la timeline en el componente */}
            <style>{timelineStyles}</style>

            {/* Carrusel principal */}
            <Carousel fade>
                <Carousel.Item>
                    <img className="d-block w-100" src={banner1} alt="First slide" />
                    <Carousel.Caption className="bg-dark bg-opacity-50 p-2 p-md-3 rounded">
                        {/* Clases responsivas para el texto del carrusel */}
                        <h3 className="text-white fs-5 fs-md-3">Bienvenido a CompuSearch</h3>
                        <p className="text-white d-none d-md-block">Encuentra los mejores componentes para tu PC</p>
                    </Carousel.Caption>
                </Carousel.Item>
                <Carousel.Item>
                    <img className="d-block w-100" src={banner2} alt="Second slide" />
                </Carousel.Item>
            </Carousel>

            {/* Categorías Populares (Scroll horizontal responsivo) */}
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

                {/* Contenedor de cards con scroll horizontal */}
                <div
                    ref={scrollRef}
                    className="overflow-x-auto"
                    style={{ scrollbarWidth: "none", msOverflowStyle: "none" }} // Oculta la barra de scroll
                >
                    {/* flex-nowrap evita que las columnas salten de línea */}
                    <Row className="flex-nowrap gx-3 px-3">
                        {categorias.map((cat) => (
                            // Columnas responsivas: 2 en móvil, 3 en tablet, 4 en desktop
                            <Col xs={6} md={4} lg={3} key={cat.name}>
                                <Link
                                    to={`/componentes?categoria=${encodeURIComponent(cat.name)}`}
                                    style={{ textDecoration: "none", color: "inherit" }}
                                >
                                    {/* h-100 hace que todas las cards tengan la misma altura */}
                                    <Card className="shadow-sm border-0 h-100">
                                        <Card.Img src={cat.img} alt={cat.name} style={{ height: "150px", objectFit: "cover" }} />
                                        <Card.Body className="text-center bg-light">
                                            <Card.Text className="fw-semibold">{cat.name}</Card.Text>
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

            {/* Sección ¿Cómo funciona CompuSearch? (Timeline) */}
            <div className="bg-dark text-white py-5">
                <Container>
                    <h3 className="text-center mb-5 fw-bold">¿CÓMO FUNCIONA COMPUSEARCH?</h3>

                    {/* Timeline Horizontal (Solo en Desktop: d-none d-md-block) */}
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

                    {/* Timeline Vertical (Solo en Móvil: d-md-none) */}
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

                    {/* Frase "Mejores Precios" */}
                    <div className="text-center mt-5">
                         <div className="bg-primary d-inline-block p-3 rounded shadow">
                            <h5 className="fw-bold text-white m-0">
                                CON MEJORES PRECIOS DEL MERCADO
                            </h5>
                        </div>
                    </div>

                    {/* CTA (Call to Action) Armado de PC */}
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