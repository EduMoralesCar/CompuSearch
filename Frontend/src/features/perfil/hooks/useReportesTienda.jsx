import { useState } from "react";
import axios from "axios";

const useReportesTienda = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const descargarArchivo = (blob, nombre) => {
        const url = window.URL.createObjectURL(new Blob([blob]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", `${nombre}.xlsx`);
        document.body.appendChild(link);
        link.click();
        link.remove();
    };

    const ejecutarDescarga = async (url, nombreArchivo) => {
        setLoading(true);
        setError(null);

        try {
            const response = await axios.get(url, {
                responseType: "arraybuffer",
                withCredentials: true
            });

            descargarArchivo(response.data, nombreArchivo);
        } catch (err) {
            setError(err.response?.data?.message || "Error descargando reporte");
        } finally {
            setLoading(false);
        }
    };

    const obtenerCatalogo = (idTienda) => {
        return ejecutarDescarga(
            `http://localhost:8080/reportes/tiendas/${idTienda}/catalogo`,
            "catalogo_productos"
        );
    };

    const obtenerStockBajo = (idTienda) => {
        return ejecutarDescarga(
            `http://localhost:8080/reportes/tiendas/${idTienda}/stock-bajo`,
            "productos_bajo_stock"
        );
    };

    const obtenerMetricas = (idTienda) => {
        return ejecutarDescarga(
            `http://localhost:8080/reportes/tiendas/${idTienda}/metricas`,
            "metricas_productos"
        );
    };

    return {
        loading,
        error,
        obtenerCatalogo,
        obtenerStockBajo,
        obtenerMetricas
    };
};

export default useReportesTienda;
