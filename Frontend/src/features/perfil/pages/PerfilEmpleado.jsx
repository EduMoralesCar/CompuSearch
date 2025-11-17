import { useState } from "react";
import { Row, Col, Button } from "react-bootstrap";
import { BsArrowBarLeft, BsList } from "react-icons/bs";
import AsideDashboard from "../components/perfilEmpleado/slider/AsideDashboard";
import "../css/PerfilEmpleado.css";

import GestionCategorias from "../components/perfilEmpleado/content/GestionCategorias";
import GestionEtiquetas from "../components/perfilEmpleado/content/GestionEtiquetas";
import GestionUsuarios from "../components/perfilEmpleado/content/GestionUsuarios";
import GestionSolicitudes from "../components/perfilEmpleado/content/GestionSolicitudes";
import GestionIncidencias from "../components/perfilEmpleado/content/GestionIncidencias";
import GestionPlanes from "../components/perfilEmpleado/content/GestionPlanes";
import GestionEmpleados from "../components/perfilEmpleado/content/GestionEmpleados";

import { useAuthStatus } from "../../../hooks/useAuthStatus";
import GestionTiendas from "../components/perfilEmpleado/content/GestionTiendas";

const PerfilEmpleado = () => {
    const [vistaActual, setVistaActual] = useState("categorias");
    const [sidebarAbierto, setSidebarAbierto] = useState(true);

    const { idUsuario } = useAuthStatus();

    const renderizarVista = () => {
        switch (vistaActual) {
            case "categorias":
                return <GestionCategorias />;
            case "etiquetas":
                return <GestionEtiquetas />;
            case "usuarios":
                return <GestionUsuarios />;
            case "tiendas":
                return <GestionTiendas />
            case "empleados":
                return <GestionEmpleados />
            case "solicitudes":
                return <GestionSolicitudes idEmpleado={idUsuario} />;
            case "incidencias":
                return <GestionIncidencias />;
            case "planes":
                return <GestionPlanes />
            default:
                return <GestionCategorias />;
        }
    };

    return (
        <div className="dashboard-layout">

            {/* Se muestra si el sidebar está abierto Y estamos en vista móvil (d-lg-none) */}
            {sidebarAbierto && (
                <div
                    className="dashboard-backdrop d-lg-none"
                    onClick={() => setSidebarAbierto(false)}
                ></div>
            )}

            {/* Columna 1: Barra Lateral (Aside)*/}
            <aside className={`sidebar ${sidebarAbierto ? "abierto" : "cerrado"}`}>
                <AsideDashboard
                    setVistaActual={setVistaActual}
                    vistaActual={vistaActual}
                    // Pasamos la función para que el aside se cierre al navegar
                    setSidebarAbierto={setSidebarAbierto}
                />
            </aside>

            {/* Columna 2: Contenido Principal */}
            <main className="dashboard-content">

                {/*  SECCIÓN PARA MÓVIL */}
                <Button
                    variant="outline-primary"
                    onClick={() => setSidebarAbierto(!sidebarAbierto)}
                    className="mb-3 d-lg-none"
                >
                    {sidebarAbierto ? <BsArrowBarLeft /> : <BsList />}
                </Button>
                <h2 className="mb-3 d-lg-none">Panel de Administrador</h2>


                {/* SECCIÓN PARA DESKTOP */}
                <div className="d-none d-lg-flex align-items-center mb-3">
                    <Button
                        variant="outline-primary"
                        onClick={() => setSidebarAbierto(!sidebarAbierto)}
                        className="me-3"
                    >
                        {sidebarAbierto ? <BsArrowBarLeft /> : <BsList />}
                    </Button>

                    <h2 className="mb-0">Panel de Administrador</h2>
                </div>


                {/* Aquí se renderiza el módulo seleccionado */}
                <div className="vista-gestion-container">
                    {renderizarVista()}
                </div>
            </main>
        </div>
    );
};

export default PerfilEmpleado;
