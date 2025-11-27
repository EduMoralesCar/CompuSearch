import { Nav } from "react-bootstrap";
import Logo from "../../../../assets/logo/logo.webp";
import { NavLink } from "react-router-dom";
import BotonLogout from "./BotonLogout";

const DashboardSidebarLayout = ({
    navItems,
    setVistaActual,
    vistaActual,
    setSidebarAbierto,
    headerTitle
}) => {

    const handleSelect = (vista) => {
        setVistaActual(vista);

        if (window.innerWidth < 992) {
            setSidebarAbierto(false);
        }
    };

    return (
        <div className="d-flex flex-column justify-content-between h-100 p-3 text-dark">

            <div>
                <NavLink className="navbar-brand d-flex align-items-center" to="/">
                    <img src={Logo} alt="Logo" height="50" className="mb-4" />
                </NavLink>

                {headerTitle && <h5 className="text-white mb-4">{headerTitle}</h5>}

                <Nav
                    variant="pills"
                    className="flex-column gap-2"
                    activeKey={vistaActual}
                    onSelect={handleSelect}
                >
                    {navItems.map((item) => (
                        <Nav.Item key={item.eventKey}>
                            <Nav.Link eventKey={item.eventKey}>
                                {item.icon && <i className={`${item.icon} me-2`}></i>}
                                {item.label}
                            </Nav.Link>
                        </Nav.Item>
                    ))}
                </Nav>
            </div>

            <div className="mt-auto pt-3 border-top p-3">
                <BotonLogout
                    className="w-100 sidebar-logout-btn"
                    variant="outline-secondary"
                />
            </div>
        </div>
    );
};

export default DashboardSidebarLayout;