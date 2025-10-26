import { useState } from "react";
import axios from "axios";

export function useSolicitudes() {
    const [respuesta, setRespuesta] = useState([]);
    const [totalPages, setTotalPages] = useState(0);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const obtenerSolicitudes = async (idUsuario, page = 0, size = 5) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.get(
                `http://localhost:8080/solicitud/${idUsuario}`,
                {
                    params: { page, size },
                    withCredentials: true
                }
            );
            const data = response.data;
            console.log(data);
            setRespuesta(data.content || []);
            setTotalPages(data.totalPages || 0);
        } catch (err) {
            setError(err.response?.data?.message || "Error al obtener los incidentes");
        } finally {
            setLoading(false);
        }
    };

    return { respuesta, loading, error, obtenerSolicitudes, totalPages };
}
