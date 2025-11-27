import { useState, useCallback } from "react";
import axios from "axios";

const useReportesEmpleado = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const descargarReporte = useCallback(async (endpoint, params = {}, nombreArchivo = "reporte.xlsx") => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.get(`http://localhost:8080/reportes/empleado${endpoint}`, {
                params,
                responseType: "blob",
                withCredentials: true
            });

            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement("a");
            link.href = url;
            link.setAttribute("download", nombreArchivo);
            document.body.appendChild(link);
            link.click();
            link.remove();
            window.URL.revokeObjectURL(url);

            return { success: true };
        } catch (err) {
            console.error("Error descargando reporte:", err);
            setError(err);
            return { success: false, error: err };
        } finally {
            setLoading(false);
        }
    }, []);

    const reportes = {
        exportTiendasDesdeFecha: (fechaInicio) =>
            descargarReporte("/tiendas/desde-fecha", { fechaInicio }, "Tiendas_Desde_Fecha.xlsx"),

        exportTopTiendasPorProductos: (n) =>
            descargarReporte("/tiendas/top-productos", { n }, "Top_Tiendas_Productos.xlsx"),

        exportTopTiendasPorVisitas: (n) =>
            descargarReporte("/tiendas/top-visitas", { n }, "Top_Tiendas_Visitas.xlsx"),

        exportUsuariosDesdeFecha: (fechaInicio) =>
            descargarReporte("/usuarios/desde-fecha", { fechaInicio }, "Usuarios_Desde_Fecha.xlsx"),

        exportUsuariosActivosInactivos: () =>
            descargarReporte("/usuarios/activos-inactivos", {}, "Usuarios_Activos_Inactivos.xlsx"),
    };

    return { ...reportes, descargarReporte, loading, error };
};

export default useReportesEmpleado;
