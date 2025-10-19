import { BrowserRouter, Routes, Route } from "react-router-dom";
import RedirectIfAuthenticated from "../components/RedirectIfAuthenticated";
import RequireAnonimo from "../components/RequireAnonimo";
import RequireTipoUsuario from "../components/RequireTipoUsuario";

// Auth
import Login from "../features/auth/pages/Login";
import Registro from "../features/auth/pages/Registro";
import ForgotPassword from "../features/auth/pages/ForgotPassword";
import ResetPassword from "../features/auth/pages/ResetPassword";

// Navegación
import Home from "../features/navigation/pages/Home";
import Categorias from "../features/navigation/pages/Categorias";
import Builds from "../features/navigation/pages/Builds";
import Componentes from "../features/navigation/pages/Componentes";
import TiendasAfiliadas from "../features/navigation/pages/Tiendas";
import ProductoDetalle from "../features/navigation/pages/ProductoDetalle";

// Perfil
import PerfilUsuario from "../features/perfil/pages/PerfilUsuario";
import PerfilEmpleado from "../features/perfil/pages/PerfilEmpleado";
import PerfilTienda from "../features/perfil/pages/PerfilTienda";

// Auxiliar
import NotFound from "../features/auxiliar/pages/NotFound";

const AppRoute = () => {
    return (
        <Routes>
            {/* Auth */}
            <Route
                path="/login"
                element={
                    <RedirectIfAuthenticated>
                        <Login />
                    </RedirectIfAuthenticated>
                }
            />
            <Route
                path="/registro"
                element={
                    <RedirectIfAuthenticated>
                        <Registro />
                    </RedirectIfAuthenticated>
                }
            />
            <Route
                path="/forgot-password"
                element={
                    <RequireAnonimo>
                        <ForgotPassword />
                    </RequireAnonimo>
                }
            />
            <Route
                path="/reset-password"
                element={
                    <RequireAnonimo>
                        <ResetPassword />
                    </RequireAnonimo>
                }
            />

            {/* Navegación */}
            <Route path="/" element={<Home />} />
            <Route path="/categorias" element={<Categorias />} />
            <Route path="/builds" element={<Builds />} />
            <Route path="/componentes" element={<Componentes />} />
            <Route path="/tiendas" element={<TiendasAfiliadas />} />
            <Route path="/producto/:slug" element={<ProductoDetalle />} />

            {/* Perfil */}
            <Route
                path="/perfil/usuario"
                element={
                    <RequireTipoUsuario tiposPermitidos={["USUARIO"]}>
                        <PerfilUsuario />
                    </RequireTipoUsuario>
                }
            />
            <Route
                path="/perfil/empleado"
                element={
                    <RequireTipoUsuario tiposPermitidos={["EMPLEADO"]}>
                        <PerfilEmpleado />
                    </RequireTipoUsuario>
                }
            />
            <Route
                path="/perfil/tienda"
                element={
                    <RequireTipoUsuario tiposPermitidos={["TIENDA"]}>
                        <PerfilTienda />
                    </RequireTipoUsuario>
                }
            />

            {/* Auxiliar */}
            <Route path="*" element={<NotFound />} />
        </Routes>
    );
};

export default AppRoute;
