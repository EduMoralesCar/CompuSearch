import React from "react";
import FooterLogo from "./FooterLogo";
import FooterSection from "./FooterSection";
import FooterSocial from "./FooterSocial";
import FooterBottom from "./FooterBottom";

const Footer = () => {
    return (
        <footer className="bg-primary text-white pt-4">
            <div className="container">
                <div className="row text-center">
                    {/* Columna 1: Logo */}
                    <FooterColumn>
                        <FooterLogo />
                    </FooterColumn>

                    {/* Columna 2: Sobre + Tiendas */}
                    <FooterColumn>
                        <FooterSection
                            title="SOBRE COMPUSEARCH"
                            links={[
                                { label: "Acerca de nosotros", to: "/acerca" },
                                { label: "Cómo funciona", to: "/funciona" },
                                { label: "Preguntas frecuentes", to: "/faq" },
                            ]}
                        />
                        <FooterSection
                            title="TIENDAS"
                            links={[
                                { label: "Computer Shop", to: "/tiendas/computer-shop" },
                                { label: "Importaciones Impacto", to: "/tiendas/impacto" },
                                { label: "Ver todas", to: "/tiendas" },
                            ]}
                        />
                    </FooterColumn>

                    {/* Columna 3: Categorías */}
                    <FooterColumn>
                        <FooterSection
                            title="CATEGORÍAS"
                            links={[
                                { label: "Procesador", to: "/categorias/procesador" },
                                { label: "Tarjetas Gráfica", to: "/categorias/gpu" },
                                { label: "Almacenamiento", to: "/categorias/almacenamiento" },
                                { label: "Fuente de poder", to: "/categorias/fuente" },
                                { label: "Ver todas", to: "/categorias" },
                            ]}
                        />
                    </FooterColumn>

                    {/* Columna 4: Legal + Redes */}
                    <FooterColumn>
                        <FooterSocial />
                    </FooterColumn>
                </div>
            </div>

            <FooterBottom />
        </footer>
    );
};

// Componente auxiliar para columnas
const FooterColumn = ({ children }) => (
    <div className="col-12 col-md-6 col-lg-3 mb-4 d-flex flex-column align-items-center text-center">
        {children}
    </div>
);

export default Footer;
