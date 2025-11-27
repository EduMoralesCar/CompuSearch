import { useState } from "react";
import { Button } from "react-bootstrap";
import { BsArrowBarLeft, BsList } from "react-icons/bs";
import DashboardSidebarLayout from "../components/auxiliar/DashboardSidebarLayout";
import "../css/AsidebarPerfil.css"

import GestionCategorias from "../components/perfilEmpleado/content/GestionCategorias";
import GestionEtiquetas from "../components/perfilEmpleado/content/GestionEtiquetas";
import GestionUsuarios from "../components/perfilEmpleado/content/GestionUsuarios";
import GestionSolicitudes from "../components/perfilEmpleado/content/GestionSolicitudes";
import GestionIncidencias from "../components/perfilEmpleado/content/GestionIncidencias";
import GestionPlanes from "../components/perfilEmpleado/content/GestionPlanes";
import GestionEmpleados from "../components/perfilEmpleado/content/GestionEmpleados";
import GestionTiendas from "../components/perfilEmpleado/content/GestionTiendas";
import EmpleadoDashboard from "../components/perfilEmpleado/content/EmpleadoDashboard";
import GestionReportesEmpleado from "../components/perfilEmpleado/content/GestionReportesEmpleado";

import { useAuthStatus } from "../../../hooks/useAuthStatus";

const PerfilEmpleado = () => {
    const [vistaActual, setVistaActual] = useState("dashboard");
    const [sidebarAbierto, setSidebarAbierto] = useState(true);

    const { idUsuario } = useAuthStatus();

    const EMPLOYEE_NAV_ITEMS = [
        { eventKey: "dashboard", label: "Dashboard", icon: "bi bi-speedometer2" },
        { eventKey: "categorias", label: "Categorías", icon: "bi bi-grid-3x3-gap-fill" },
        { eventKey: "etiquetas", label: "Etiquetas", icon: "bi bi-tags-fill" },
        { eventKey: "usuarios", label: "Usuarios", icon: "bi bi-people-fill" },
        { eventKey: "tiendas", label: "Tiendas", icon: "bi bi-shop-window" },
        { eventKey: "empleados", label: "Empleados", icon: "bi bi-person-badge" },
        { eventKey: "solicitudes", label: "Solicitudes", icon: "bi bi-envelope-fill" },
        { eventKey: "incidencias", label: "Incidencias", icon: "bi bi-exclamation-triangle-fill" },
        { eventKey: "planes", label: "Planes", icon: "bi bi-card-checklist" },
        { eventKey: "reportes", label: "Reportes", icon: "bi bi-folder2-open" },
    ];

    const renderizarVista = () => {
        switch (vistaActual) {
            case "dashboard":
                return <EmpleadoDashboard />;
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
            case "reportes":
                return <GestionReportesEmpleado />
            default:
                return <EmpleadoDashboard />;
        }
    };

    return (
        <div className={`dashboard-layout ${sidebarAbierto ? "sidebar-open" : "sidebar-closed"}`}>

            {sidebarAbierto && (
                <div
                    className="dashboard-backdrop d-lg-none"
                    onClick={() => setSidebarAbierto(false)}
                ></div>
            )}

            <aside className={`sidebar ${sidebarAbierto ? "abierto" : "cerrado"}`}>
                <DashboardSidebarLayout
                    navItems={EMPLOYEE_NAV_ITEMS}
                    setVistaActual={setVistaActual}
                    vistaActual={vistaActual}
                    setSidebarAbierto={setSidebarAbierto}
                    headerTitle="Panel Administrador"
                />
            </aside>

            <main className="dashboard-content">

                <Button
                    variant="outline-primary"
                    onClick={() => setSidebarAbierto(!sidebarAbierto)}
                    className="mb-3 d-lg-none"
                >
                    {sidebarAbierto ? <BsArrowBarLeft /> : <BsList />}
                </Button>
                <h2 className="mb-3 d-lg-none">Panel de Administrador</h2>


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


                <div className="vista-gestion-container">
                    {renderizarVista()}
                </div>
            </main>
        </div>
    );
};

export default PerfilEmpleado;
