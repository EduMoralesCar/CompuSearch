import React, { useEffect, useState } from "react";
import { BrowserRouter } from "react-router-dom";
import AppRoute from "./routes/AppRoute";
import { MarginTop } from "./utils/MarginTop";
import AuthToast from "./components/auth/AuthToast";
import ScrollToTop from "./utils/ScrollToTop";
import ModalBienvenida from "./features/navigation/components/auxiliar/ModalBienvenida";

import "./css/App.css"

const App = () => {
    const [showWelcomeModal, setShowWelcomeModal] = useState(false);

    useEffect(() => {
        MarginTop();
        window.addEventListener("resize", MarginTop);

        // Verificar si ya se mostró el modal en esta sesión
        const hasSeenModalThisSession = sessionStorage.getItem("hasSeenWelcomeModal");
        if (!hasSeenModalThisSession) {
            setShowWelcomeModal(true);
        }

        return () => window.removeEventListener("resize", MarginTop);
    }, []);

    const handleCloseWelcomeModal = () => {
        setShowWelcomeModal(false);
        sessionStorage.setItem("hasSeenWelcomeModal", "true");
    };

    return (
        <BrowserRouter>
            <ScrollToTop />
            <AuthToast />
            <ModalBienvenida show={showWelcomeModal} handleClose={handleCloseWelcomeModal} />
            <AppRoute />
        </BrowserRouter>
    );
};

export default App;
