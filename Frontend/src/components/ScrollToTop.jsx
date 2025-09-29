import { useEffect } from "react";
import { useLocation } from "react-router-dom";

const ScrollToTop = () => {
    // Obtiene el objeto de ubicación actual, que cambia con cada ruta.
    const { pathname } = useLocation();

    useEffect(() => {
        // esta función desplaza la ventana a la parte superior izquierda.
        window.scrollTo(0, 0);
    }, [pathname]); // El efecto se vuelve a ejecutar cada vez que 'pathname' cambia.

    return null;
};

export default ScrollToTop;