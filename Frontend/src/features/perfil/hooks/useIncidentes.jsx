import { useState } from "react";
import axios from "axios";

export function useIncidentes() {
    const [respuesta, setRespuesta] = useState([]);
    const [totalPages, setTotalPages] = useState(0);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const obtenerIncidentes = async (idUsuario, page = 0, size = 5) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.get(
                `http://localhost:8080/incidentes/${idUsuario}`,
                {
                    params: { page, size },
                    withCredentials: true
                }
            );
            const data = response.data;
            setRespuesta(data.content || []); // content del page
            setTotalPages(data.totalPages || 0);
        } catch (err) {
            setError(err.response?.data?.message || "Error al obtener los incidentes");
        } finally {
            setLoading(false);
        }
    };

    return { respuesta, loading, error, obtenerIncidentes, totalPages };
}
