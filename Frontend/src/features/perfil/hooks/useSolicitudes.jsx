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
            setRespuesta(data.content || []);
            setTotalPages(data.totalPages || 0);
        } catch (err) {
            setError(err.response?.data?.message || "Error al obtener las solicitudes del usuario");
        } finally {
            setLoading(false);
        }
    };

    const obtenerTodasSolicitudes = async (page = 0, size = 10) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.get(`http://localhost:8080/solicitud`, {
                params: { page, size },
                withCredentials: true
            });
            const data = response.data;
            setRespuesta(data.content || []);
            setTotalPages(data.totalPages || 0);
        } catch (err) {
            setError(err.response?.data?.message || "Error al obtener todas las solicitudes");
        } finally {
            setLoading(false);
        }
    };

    const actualizarEstadoSolicitud = async (idSolicitud, nuevoEstado, idEmpleado) => {
        try {
            await axios.put(
                `http://localhost:8080/solicitud/${idSolicitud}/estado`,
                null,
                {
                    params: { nuevoEstado, idEmpleado },
                    withCredentials: true
                }
            );

            setRespuesta((prev) =>
                prev.map((solicitud) =>
                    solicitud.idSolicitud === idSolicitud
                        ? { ...solicitud, estado: nuevoEstado }
                        : solicitud
                )
            );

        } catch (err) {
            setError(err.response?.data?.message || "Error al actualizar el estado de la solicitud");
        }
    };

    return {
        respuesta,
        totalPages,
        loading,
        error,
        obtenerSolicitudes,
        obtenerTodasSolicitudes,
        actualizarEstadoSolicitud
    };
}
