import React, { useEffect, useState } from "react";
import { BrowserRouter as Router, Routes, Route, useLocation } from "react-router-dom";
import Header from "./components/Header";
import Footer from "./components/Footer";
import ScrollToTop from "./components/ScrollToTop";

import Home from "./pages/Home";
import Componentes from "./pages/Componentes";
import Tiendas from "./pages/Tiendas";
import Categorias from "./pages/Categorias";
import Builds from "./pages/Builds";
import Login from "./pages/Login";
import Registro from "./pages/Registro";
import Perfil from "./pages/Perfil";
import ForgotPassword from "./pages/ForgotPassword";
import ResetPassword from "./pages/ResetPassword";
import NotFound from "./pages/NotFound";

import PrivateRoute from "./components/PrivateRoute";
import Admin from "./pages/Admin";
import AdminTienda from "./pages/AdminTienda";


// Componente AppLayout: Define la estructura condicional
const AppLayout = ({ headerHeight, isAdminRoute }) => {
    
    return (
        <div className="d-flex flex-column min-vh-100">
            {/* Renderizar Header solo si no es una ruta de administración */}
            {!isAdminRoute && <Header />}

            <main 
                className="flex-grow-1" 
                // Aplicar padding solo si el Header está presente
                style={{ paddingTop: !isAdminRoute ? headerHeight : 0 }}
            >
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/componentes" element={<Componentes />} />
                    <Route path="/tiendas" element={<Tiendas />} />
                    <Route path="/categorias" element={<Categorias />} />
                    <Route path="/builds" element={<Builds />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/registro" element={<Registro />} />
                    <Route path="/perfil" element={<Perfil />} />
                    <Route path="/forgot-password" element={<ForgotPassword />} />
                    <Route path="/reset-password" element={<ResetPassword />} />

                    {/* Rutas solo para EMPLEADO */}
                    <Route element={<PrivateRoute allowedRoles={["EMPLEADO"]} />}>
                        <Route path="/admin" element={<Admin />} />
                    </Route>

                    {/* Rutas solo para TIENDA */}
                    <Route element={<PrivateRoute allowedRoles={["TIENDA"]} />}>
                        <Route path="/admin-tienda" element={<AdminTienda />} />
                    </Route>

                    {/* Ruta de comodín */}
                    <Route path="*" element={<NotFound />} />
                </Routes>
            </main>

            {/* Renderizar Footer solo si NO es una ruta de administración */}
            {!isAdminRoute && <Footer />}
        </div>
    );
};


// Componente AppContent (Contiene los Hooks)
const AppContent = () => {
    // useLocation ahora se llama DENTRO del Router
    const location = useLocation(); 
    // Uso startsWith para cubrir /admin y /admin-tienda
    const isAdminRoute = location.pathname.startsWith("/admin"); 
    
    const [headerHeight, setHeaderHeight] = useState(0);

    // Lógica de cálculo de altura del header (se recalcula al cambiar de ruta)
    useEffect(() => {
        const header = document.querySelector("header");

        if (header) {
            // Inicializa la altura inmediatamente al renderizar el Header
            setHeaderHeight(header.offsetHeight);

            const resizeObserver = new ResizeObserver((entries) => {
                for (let entry of entries) {
                    setHeaderHeight(entry.contentRect.height);
                }
            });

            resizeObserver.observe(header);

            return () => resizeObserver.disconnect();
        } else {
            // Si el Header no se está renderizando (rutas admin), la altura es 0
            setHeaderHeight(0);
        }

    }, [isAdminRoute]); 

    return (
        <AppLayout headerHeight={headerHeight} isAdminRoute={isAdminRoute} />
    );
}


// Componente Principal Appr
const App = () => {
  return (
    <Router>
        <ScrollToTop />
        {/* Renderiza el componente que contiene toda la lógica de hooks */}
        <AppContent /> 
    </Router>
  );
};

export default App;