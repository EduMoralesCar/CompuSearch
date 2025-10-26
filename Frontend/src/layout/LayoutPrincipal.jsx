import { Outlet } from "react-router-dom";
import Header from "../components/header/Header";
import Footer from "../components/footer/Footer";
import { useEffect } from "react";
import { MarginTop } from "../utils/MarginTop";
import "../css/LayoutPrincipal.css"

const LayoutPrincipal = () => {
    useEffect(() => {
        MarginTop();
        window.addEventListener("resize", MarginTop);
        return () => window.removeEventListener("resize", MarginTop);
    }, []);

    return (
        <>
            <Header />
            <main className="layout-principal">
                <Outlet />
            </main>
            <Footer />
        </>
    );
};

export default LayoutPrincipal;
