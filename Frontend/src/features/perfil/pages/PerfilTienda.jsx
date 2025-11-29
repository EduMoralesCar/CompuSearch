import { useState } from "react";
import { Button } from "react-bootstrap";
import { BsArrowBarLeft, BsList } from "react-icons/bs";
import DashboardSidebarLayout from "../components/auxiliar/DashboardSidebarLayout";
import "../css/AsidebarPerfil.css";

import InformacionTienda from "../components/perfilTienda/content/InformacionTienda";
import MisProductos from "../components/perfilTienda/content/MisProductos";
import ObtenerPlan from "../components/perfilTienda/content/ObtenerPlan";
import ConexionAPI from "../components/perfilTienda/content/ConexionAPI";
import HistorialPagos from "../components/perfilTienda/content/HistorialPagos";
import TiendaDashboard from "../components/perfilTienda/content/TiendaDashboard";
import ReportesTienda from "../components/perfilTienda/content/ReportesTienda";

import { useAuthStatus } from "../../../hooks/useAuthStatus";

const STORE_NAV_ITEMS = [
    { eventKey: "dashboard", label: "Dashboard", icon: "bi bi-speedometer2" },
    { eventKey: "informacion", label: "Información", icon: "bi bi-info-circle-fill" },
    { eventKey: "productos", label: "Productos", icon: "bi bi-box-seam" },
    { eventKey: "planes", label: "Planes", icon: "bi bi-card-list" },
    { eventKey: "pagos", label: "Pagos", icon: "bi bi-credit-card-fill" },
    { eventKey: "conexion", label: "API", icon: "bi bi-code-slash" },
    { eventKey: "reportes", label: "Reportes", icon: "bi bi-folder2-open" },
];

const VISTAS_COMPONENTES = (idTienda) => ({
    dashboard: <TiendaDashboard idTienda={idTienda} />,
    informacion: <InformacionTienda idTienda={idTienda} />,
    productos: <MisProductos idTienda={idTienda} />,
    planes: <ObtenerPlan idTienda={idTienda}/>,
    pagos: <HistorialPagos idTienda={idTienda}/>,
    conexion: <ConexionAPI idTienda={idTienda} />,
    reportes: <ReportesTienda idTienda={idTienda} />,
});

const PerfilTienda = () => {
    const [vistaActual, setVistaActual] = useState("dashboard");
    const [sidebarAbierto, setSidebarAbierto] = useState(true);

    const { idUsuario } = useAuthStatus();
    const vistas = VISTAS_COMPONENTES(idUsuario);

    const toggleSidebar = () => setSidebarAbierto((prev) => !prev);

    const BotonSidebar = (
        <Button variant="outline-primary" onClick={toggleSidebar} className="me-3">
            {sidebarAbierto ? <BsArrowBarLeft /> : <BsList />}
        </Button>
    );

    return (
        <div className={`dashboard-layout ${sidebarAbierto ? "sidebar-open" : "sidebar-closed"}`}>
            {sidebarAbierto && (
                <div
                    className="dashboard-backdrop d-lg-none"
                    onClick={toggleSidebar}
                ></div>
            )}

            <aside className={`sidebar ${sidebarAbierto ? "abierto" : "cerrado"}`}>
                <DashboardSidebarLayout
                    navItems={STORE_NAV_ITEMS}
                    setVistaActual={setVistaActual}
                    vistaActual={vistaActual}
                    setSidebarAbierto={setSidebarAbierto}
                    headerTitle="Panel Tienda"
                />
            </aside>

            <main className="dashboard-content">
                <div className="mb-3 d-lg-none">
                    {BotonSidebar}
                    <h2>Panel de Tienda</h2>
                </div>

                <div className="d-none d-lg-flex align-items-center mb-3">
                    {BotonSidebar}
                    <h2 className="mb-0">Panel de Tienda</h2>
                </div>

                <div className="vista-gestion-container">
                    {vistas[vistaActual] || vistas["dashboard"]}
                </div>
            </main>
        </div>
    );
};

export default PerfilTienda;
