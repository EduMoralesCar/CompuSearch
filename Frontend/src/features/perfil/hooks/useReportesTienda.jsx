import { useState, useCallback } from "react";
import axios from "axios";

const BASE_URL = "http://localhost:8080/reportes/tiendas";

const useReportesTienda = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const descargarArchivo = useCallback((blob, nombre) => {
        const url = window.URL.createObjectURL(new Blob([blob]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", `${nombre}.xlsx`);
        document.body.appendChild(link);
        link.click();
        link.remove();
        window.URL.revokeObjectURL(url);
    }, []);

    const ejecutarDescarga = useCallback(async (endpoint, nombreArchivo) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.get(`${BASE_URL}${endpoint}`, {
                responseType: "arraybuffer",
                withCredentials: true
            });
            descargarArchivo(response.data, nombreArchivo);
        } catch (err) {
            setError(err.response?.data?.message || "Error descargando reporte");
        } finally {
            setLoading(false);
        }
    }, [descargarArchivo]);

    const obtenerCatalogo = useCallback((idTienda) => ejecutarDescarga(`/${idTienda}/catalogo`, "catalogo_productos"), [ejecutarDescarga]);
    const obtenerStockBajo = useCallback((idTienda) => ejecutarDescarga(`/${idTienda}/stock-bajo`, "productos_bajo_stock"), [ejecutarDescarga]);
    const obtenerMetricas = useCallback((idTienda) => ejecutarDescarga(`/${idTienda}/metricas`, "metricas_productos"), [ejecutarDescarga]);

    return {
        loading,
        error,
        obtenerCatalogo,
        obtenerStockBajo,
        obtenerMetricas
    };
};

export default useReportesTienda;
