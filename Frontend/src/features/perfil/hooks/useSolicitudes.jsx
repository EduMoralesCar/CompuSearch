import { useState, useCallback } from "react";
import axios from "axios";

export function useSolicitudes() {
    const [respuesta, setRespuesta] = useState([]);
    const [totalPages, setTotalPages] = useState(0);
    const [number, setNumber] = useState(0);
    const [totalElements, setTotalElements] = useState(0);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    // Helper para centralizar requests
    const handleRequest = useCallback(async (requestFn, errorMsg) => {
        setLoading(true);
        setError(null);
        try {
            const response = await requestFn();
            return { success: true, data: response.data };
        } catch (err) {
            const message = err.response?.data?.message || errorMsg;
            setError(message);
            return { success: false, error: message };
        } finally {
            setLoading(false);
        }
    }, []);

    const obtenerSolicitudes = useCallback((idUsuario, page = 0, size = 5) => {
        return handleRequest(
            () => axios.get(`http://localhost:8080/solicitud/${idUsuario}`, {
                params: { page, size },
                withCredentials: true
            }),
            "Error al obtener las solicitudes del usuario"
        ).then((result) => {
            if (result.success) {
                const data = result.data;
                setRespuesta(data.content || []);
                setTotalPages(data.totalPages || 0);
                setNumber(data.number || 0);
                setTotalElements(data.totalElements || 0);
            }
            return result;
        });
    }, [handleRequest]);

    const obtenerTodasSolicitudes = useCallback((page = 0, size = 10) => {
        return handleRequest(
            () => axios.get(`http://localhost:8080/solicitud`, {
                params: { page, size },
                withCredentials: true
            }),
            "Error al obtener todas las solicitudes"
        ).then((result) => {
            if (result.success) {
                const data = result.data;
                setRespuesta(data.content || []);
                setTotalPages(data.totalPages || 0);
                setNumber(data.number || 0);
                setTotalElements(data.totalElements || 0);
            }
            return result;
        });
    }, [handleRequest]);

    const actualizarEstadoSolicitud = useCallback(async (idSolicitud, nuevoEstado, idEmpleado) => {
        const result = await handleRequest(
            () => axios.put(
                `http://localhost:8080/solicitud/${idSolicitud}/estado`,
                null,
                { params: { nuevoEstado, idEmpleado }, withCredentials: true }
            ),
            "Error al actualizar el estado de la solicitud"
        );

        if (result.success) {
            setRespuesta((prev) =>
                prev.map((solicitud) =>
                    solicitud.idSolicitudTienda === idSolicitud
                        ? { ...solicitud, estado: nuevoEstado }
                        : solicitud
                )
            );
        } else {
            throw new Error(result.error); // Para mantener el comportamiento anterior
        }

        return result;
    }, [handleRequest]);

    return {
        respuesta,
        totalPages,
        number,
        totalElements,
        loading,
        error,
        obtenerSolicitudes,
        obtenerTodasSolicitudes,
        actualizarEstadoSolicitud
    };
}
