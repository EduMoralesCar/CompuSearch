import React, { useEffect } from "react";
import { BrowserRouter } from "react-router-dom";
import Header from "./components/header/Header";
import AppRoute from "./routes/AppRoute";
import Footer from "./components/Footer/Footer";
import { applyHeaderOffset } from "./utils/layout";
import AuthToast from "./components/AuthToast";
import ScrollToTop from "./utils/ScrollToTop"

import "./App.css"

const App = () => {
    useEffect(() => {
        applyHeaderOffset();
        window.addEventListener("resize", applyHeaderOffset);
        return () => window.removeEventListener("resize", applyHeaderOffset);
    }, []);

    return (
        <BrowserRouter>
            <ScrollToTop />
            <Header />
            <AuthToast />
            <main>
                <AppRoute />
            </main>
            <Footer />
        </BrowserRouter>
    );
};

export default App;
