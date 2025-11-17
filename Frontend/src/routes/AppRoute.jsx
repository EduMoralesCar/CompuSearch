import { Routes, Route } from "react-router-dom";
import RedirectIfAuthenticated from "../components/auth/RedirectIfAuthenticated";
import RequireAnonimo from "../components/auth/RequireAnonimo";
import RequireTipoUsuario from "../components/auth/RequireTipoUsuario";

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
import Unauthorized from "../features/auxiliar/pages/Unauthorized";

// Layouts
import LayoutPrincipal from "../layout/LayoutPrincipal";
import LayoutPerfil from "../layout/LayoutPerfil";

const AppRoute = () => {
    return (
        <Routes>
            <Route element={<LayoutPrincipal />}>
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
                <Route
                    path="/perfil/usuario"
                    element={
                        <RequireTipoUsuario tiposPermitidos={["USUARIO"]}>
                            <PerfilUsuario />
                        </RequireTipoUsuario>
                    }
                />
            </Route>

            <Route element={<LayoutPrincipal />}>
                <Route path="/" element={<Home />} />
                <Route path="/categorias" element={<Categorias />} />
                <Route path="/builds" element={<Builds />} />
                <Route path="/componentes" element={<Componentes />} />
                <Route path="/tiendas" element={<TiendasAfiliadas />} />
                <Route path="/producto/:slug" element={<ProductoDetalle />} />
            </Route>

            <Route element={<LayoutPerfil />}>
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
            </Route>

            <Route element={<LayoutPrincipal />}>
                <Route path="*" element={<NotFound />} />
                <Route path="/Unauthorized" element={<Unauthorized />}></Route>
            </Route>
        </Routes>
    );
};

export default AppRoute;
