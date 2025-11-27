import { useState, useCallback } from "react";
import axios from "axios";

const BASE_URL = "http://localhost:8080/incidentes";

export function useIncidentes() {
    const [respuesta, setRespuesta] = useState([]);
    const [totalPages, setTotalPages] = useState(0);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleError = useCallback((err, defaultMessage) => {
        const message = err.response?.data?.message || defaultMessage;
        console.error("Error en la solicitud:", err);
        setError(message);
        return message;
    }, []);

    const obtenerIncidentes = useCallback(async (idUsuario, page = 0, size = 5) => {
        setLoading(true);
        setError(null);
        try {
            const { data } = await axios.get(`${BASE_URL}/${idUsuario}`, {
                params: { page, size },
                withCredentials: true,
            });
            setRespuesta(data.content || []);
            setTotalPages(data.totalPages || 0);
            return data;
        } catch (err) {
            handleError(err, "Error al obtener los incidentes del usuario");
            return { content: [], totalPages: 0 };
        } finally {
            setLoading(false);
        }
    }, [handleError]);

    const obtenerTodosIncidentes = useCallback(async (page = 0, size = 10) => {
        setLoading(true);
        setError(null);
        try {
            const { data } = await axios.get(BASE_URL, {
                params: { page, size },
                withCredentials: true,
            });
            setRespuesta(data.content || []);
            setTotalPages(data.totalPages || 0);
            return data;
        } catch (err) {
            handleError(err, "Error al obtener todos los incidentes");
            return { content: [], totalPages: 0 };
        } finally {
            setLoading(false);
        }
    }, [handleError]);

    const eliminarIncidente = useCallback(async (id) => {
        try {
            await axios.delete(`${BASE_URL}/${id}`, { withCredentials: true });
            setRespuesta(prev => prev.filter(inc => inc.idIncidente !== id));
            return { success: true };
        } catch (err) {
            return { success: false, error: handleError(err, "Error al eliminar el incidente") };
        }
    }, [handleError]);

    const actualizarRevisado = useCallback(async (id, revisado) => {
        try {
            await axios.put(`${BASE_URL}/${id}/revisado`, null, {
                params: { revisado },
                withCredentials: true,
            });
            setRespuesta(prev => prev.map(inc => inc.idIncidente === id ? { ...inc, revisado } : inc));
            return { success: true };
        } catch (err) {
            return { success: false, error: handleError(err, "Error al actualizar el estado revisado del incidente") };
        }
    }, [handleError]);

    return {
        respuesta,
        totalPages,
        loading,
        error,
        obtenerIncidentes,
        obtenerTodosIncidentes,
        eliminarIncidente,
        actualizarRevisado,
    };
}
