import React, { useEffect } from "react";
import { BrowserRouter, useLocation } from "react-router-dom";
import Header from "./components/header/Header";
import AppRoute from "./routes/AppRoute";
import Footer from "./components/Footer/Footer";
import { applyHeaderOffset } from "./utils/layout";
import AuthToast from "./components/AuthToast";
import ScrollToTop from "./utils/ScrollToTop";

import "./App.css";

const AppContent = () => {
  const location = useLocation(); // usamos useLocation para saber la ruta
  const showLayout = location.pathname !== "/perfil/empleado"; // Condición: NO mostrar si la ruta es /perfil/empleado

  // Este useEffect ahora maneja la limpieza
  useEffect(() => {
    // Solo ejecutamos la lógica si el layout debe mostrarse
    if (showLayout) {
      applyHeaderOffset();
      window.addEventListener("resize", applyHeaderOffset);

      return () => {
        window.removeEventListener("resize", applyHeaderOffset);

        // Reseteamos el margen cuando el layout desaparece
        const main = document.querySelector("main");
        if (main) {
          main.style.marginTop = "0px";
        }
      };
    }
  }, [showLayout]);

  return (
    <>
      <ScrollToTop />

      {showLayout && <Header />}

      <AuthToast />
      <main>
        <AppRoute />
      </main>

      {showLayout && <Footer />}
    </>
  );
};

const App = () => {
  return (
    <BrowserRouter>
      <AppContent />
    </BrowserRouter>
  );
};

export default App;
