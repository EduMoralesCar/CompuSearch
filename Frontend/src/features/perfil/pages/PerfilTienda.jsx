import { useState } from "react";
import { Button } from "react-bootstrap";
import { BsArrowBarLeft, BsList } from "react-icons/bs";
import DashboardSidebarLayout from "../components/auxiliar/DashboardSidebarLayout";
import "../css/AsidebarPerfil.css"
import InformacionTienda from "../components/perfilTienda/InformacionTienda";
import MisProductos from "../components/perfilTienda/MisProductos";
import ObtenerPlan from "../components/perfilTienda/ObtenerPlan";
import ConexionAPI from "../components/perfilTienda/ConexionAPI";
import HistorialPagos from "../components/perfilTienda/HistorialPagos";

const PerfilTienda = () => {
    const [vistaActual, setVistaActual] = useState("dashboard");
    const [sidebarAbierto, setSidebarAbierto] = useState(true);

    const STORE_NAV_ITEMS = [
        { eventKey: "informacion", label: "Información", icon: "bi bi-info-circle-fill" },
        { eventKey: "productos", label: "Productos", icon: "bi bi-box-seam" },
        { eventKey: "planes", label: "Planes", icon: "bi bi-card-list" },
        { eventKey: "pagos", label: "Pagos", icon: "bi bi-credit-card-fill" },
        { eventKey: "conexion", label: "API", icon: "bi bi-code-slash" },
    ];

    const renderizarVista = () => {
        switch (vistaActual) {
            case "informacion":
                return <InformacionTienda />
            case "productos":
                return <MisProductos />
            case "planes":
                return <ObtenerPlan />
            case "pagos":
                return <HistorialPagos />
            case "conexion":
                return <ConexionAPI />
            default:
                return <InformacionTienda />
        }
    };

    return (
        <div className="dashboard-layout">

            {sidebarAbierto && (
                <div
                    className="dashboard-backdrop d-lg-none"
                    onClick={() => setSidebarAbierto(false)}
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

                <Button
                    variant="outline-primary"
                    onClick={() => setSidebarAbierto(!sidebarAbierto)}
                    className="mb-3 d-lg-none"
                >
                    {sidebarAbierto ? <BsArrowBarLeft /> : <BsList />}
                </Button>
                <h2 className="mb-3 d-lg-none">Panel de Tienda</h2>

                <div className="d-none d-lg-flex align-items-center mb-3">
                    <Button
                        variant="outline-primary"
                        onClick={() => setSidebarAbierto(!sidebarAbierto)}
                        className="me-3"
                    >
                        {sidebarAbierto ? <BsArrowBarLeft /> : <BsList />}
                    </Button>
                    <h2 className="mb-0">Panel de Tienda</h2>
                </div>


                <div className="vista-gestion-container">
                    {renderizarVista()}
                </div>
            </main>
        </div>
    );
};

export default PerfilTienda;