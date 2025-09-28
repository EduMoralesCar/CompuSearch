import React from "react";
import logo from "../assets/logo.png";

const Footer = () => {
  return (
    <footer className="bg-primary text-white pt-4">
      <div className="container mt-2">
        <div className="row text-center">
          {/* Logo */}
          <div className="col-md-6 col-lg-3 mb-4 d-flex flex-column align-items-center">
            <img src={logo} alt="Logo" height="50" className="mb-4" />
            <p>
              CompuSearch es una plataforma web peruana especializada en la
              comparación de componentes de PC, creada por estudiantes de la
              Universidad Tecnológica del Perú.
            </p>
          </div>

          {/* Sobre */}
          <div className="col-md-6 col-lg-3 mb-3">
            <h6 className="fw-bold">SOBRE COMPUSEARCH</h6>
            <ul className="list-unstyled">
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Acerca de nosotros
                </a>
              </li>
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Cómo funciona
                </a>
              </li>
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Preguntas frecuentes
                </a>
              </li>
            </ul>
            <h6 className="fw-bold mt-4">TIENDAS</h6>
            <ul className="list-unstyled">
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Computer Shop
                </a>
              </li>
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Importaciones Impacto
                </a>
              </li>
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Ver todas
                </a>
              </li>
            </ul>
          </div>

          {/* Categorías */}
          <div className="col-md-6 col-lg-3 mb-3">
            <h6 className="fw-bold">CATEGORÍAS</h6>
            <ul className="list-unstyled">
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Procesador
                </a>
              </li>
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Tarjetas gráficas
                </a>
              </li>
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Almacenamiento
                </a>
              </li>
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Fuente de poder
                </a>
              </li>
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Ver todas
                </a>
              </li>
            </ul>
          </div>

          {/* Legal y Redes */}
          <div className="col-md-6 col-lg-3 mb-3">
            <h6 className="fw-bold">LEGAL</h6>
            <ul className="list-unstyled">
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Términos de uso
                </a>
              </li>
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Políticas de Privacidad
                </a>
              </li>
            </ul>

            <h6 className="fw-bold mt-4">SÍGUENOS</h6>
            <div className="d-flex justify-content-center gap-3">
              <a href="#" className="text-white fs-5">
                <i className="bi bi-twitter-x"></i>
              </a>
              <a href="#" className="text-white fs-5">
                <i className="bi bi-instagram"></i>
              </a>
              <a href="#" className="text-white fs-5">
                <i className="bi bi-youtube"></i>
              </a>
            </div>
          </div>
        </div>
      </div>

      {/* Barra inferior */}
      <div className="bg-dark text-center py-3 mt-0">
        <small>© 2025 Grupo CompuSearch - Todos los derechos reservados</small>
      </div>
    </footer>
  );
};

export default Footer;
