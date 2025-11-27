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
                    <FooterColumn>
                        <FooterLogo />
                    </FooterColumn>

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

                    <FooterColumn>
                        <FooterSocial />
                    </FooterColumn>
                </div>
            </div>

            <FooterBottom />
        </footer>
    );
};

const FooterColumn = ({ children }) => (
    <div className="col-12 col-md-6 col-lg-3 mb-4 d-flex flex-column align-items-center text-center">
        {children}
    </div>
);

export default Footer;
