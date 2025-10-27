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
            setRespuesta(data.content || []);
            setTotalPages(data.totalPages || 0);
        } catch (err) {
            setError(err.response?.data?.message || "Error al obtener los incidentes del usuario");
        } finally {
            setLoading(false);
        }
    };

    const obtenerTodosIncidentes = async (page = 0, size = 10) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.get("http://localhost:8080/incidentes", {
                params: { page, size },
                withCredentials: true
            });
            const data = response.data;
            setRespuesta(data.content || []);
            setTotalPages(data.totalPages || 0);
        } catch (err) {
            setError(err.response?.data?.message || "Error al obtener todos los incidentes");
        } finally {
            setLoading(false);
        }
    };

    const eliminarIncidente = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/incidentes/${id}`, {
                withCredentials: true
            });
            setRespuesta((prev) => prev.filter((inc) => inc.idIncidente !== id));
        } catch (err) {
            console.error("Error al eliminar el incidente:", err);
            setError(err.response?.data?.message || "Error al eliminar el incidente");
        }
    };

    const actualizarRevisado = async (id, revisado) => {
        try {
            await axios.put(
                `http://localhost:8080/incidentes/${id}/revisado`,
                null,
                {
                    params: { revisado },
                    withCredentials: true,
                }
            );

            setRespuesta((prev) =>
                prev.map((inc) =>
                    inc.idIncidente === id ? { ...inc, revisado } : inc
                )
            );
        } catch (err) {
            console.error("Error al actualizar el estado revisado:", err);
            setError(
                err.response?.data?.message ||
                "Error al actualizar el estado revisado del incidente"
            );
        }
    };

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
