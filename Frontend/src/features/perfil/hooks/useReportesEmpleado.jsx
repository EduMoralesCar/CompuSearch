import { useState } from "react";
import axios from "axios";

const useReportesEmpleado = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const descargarReporte = async (endpoint, params = {}, nombreArchivo = "reporte.xlsx") => {
        setLoading(true);
        setError(null);

        try {
            const response = await axios.get(`http://localhost:8080/reportes/empleado${endpoint}`, {
                params,
                responseType: "blob", withCredentials: true
            });

            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement("a");
            link.href = url;
            link.setAttribute("download", nombreArchivo);
            document.body.appendChild(link);
            link.click();
            link.remove();
            window.URL.revokeObjectURL(url);

            setLoading(false);
            return true;
        } catch (err) {
            console.error("Error descargando reporte:", err);
            setError(err); 
            setLoading(false);
            return false;
        }
    };

    const exportTiendasDesdeFecha = (fechaInicioStr) => {
        return descargarReporte(
            "/tiendas/desde-fecha",
            { fechaInicio: fechaInicioStr },
            "Tiendas_Desde_Fecha.xlsx"
        );
    };

    const exportTopTiendasPorProductos = (n) => {
        return descargarReporte(
            "/tiendas/top-productos",
            { n: n },
            "Top_Tiendas_Productos.xlsx"
        );
    };

    const exportTopTiendasPorVisitas = (n) => {
        return descargarReporte(
            "/tiendas/top-visitas",
            { n: n },
            "Top_Tiendas_Visitas.xlsx"
        );
    };

    const exportUsuariosDesdeFecha = (fechaInicioStr) => {
        return descargarReporte(
            "/usuarios/desde-fecha",
            { fechaInicio: fechaInicioStr },
            "Usuarios_Desde_Fecha.xlsx"
        );
    };
    const exportUsuariosActivosInactivos = () => {
        return descargarReporte(
            "/usuarios/activos-inactivos",
            {}, 
            "Usuarios_Activos_Inactivos.xlsx"
        );
    };

    return { 
        descargarReporte,
        exportTiendasDesdeFecha,
        exportTopTiendasPorProductos,
        exportTopTiendasPorVisitas,
        exportUsuariosDesdeFecha,
        exportUsuariosActivosInactivos,
        loading, 
        error 
    };
};

export default useReportesEmpleado;