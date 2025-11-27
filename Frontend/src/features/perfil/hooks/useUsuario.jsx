import { useState, useCallback } from "react";
import axios from "axios";

export function useUsuario() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const baseUrl = "http://localhost:8080/usuario";

    const handleRequest = useCallback(async (requestFn, defaultErrorMsg) => {
        setLoading(true);
        setError(null);
        try {
            const response = await requestFn();
            return { success: true, data: response.data };
        } catch (err) {
            const message = err.response?.data?.message || defaultErrorMsg;
            setError(message);
            return { success: false, error: message };
        } finally {
            setLoading(false);
        }
    }, []);

    const obtenerUsuarioPorId = useCallback(
        (idUsuario) =>
            handleRequest(
                () => axios.get(`${baseUrl}/${idUsuario}`, { withCredentials: true }),
                "Error al obtener información del usuario"
            ),
        [baseUrl, handleRequest]
    );

    const obtenerUsuariosPaginados = useCallback(
        (page = 0, size = 10, username = "") => {
            const params = { page, size, ...(username && { username }) };
            return handleRequest(
                () => axios.get(baseUrl, { params, withCredentials: true }),
                "Error al obtener la lista de usuarios"
            );
        },
        [baseUrl, handleRequest]
    );

    const actualizarInfo = useCallback(
        (idUsuario, infoData) =>
            handleRequest(
                () => axios.put(`${baseUrl}/${idUsuario}`, infoData, { withCredentials: true }),
                "Error al actualizar información"
            ),
        [baseUrl, handleRequest]
    );

    const actualizarPassword = useCallback(
        (idUsuario, passwordData) =>
            handleRequest(
                () => axios.put(`${baseUrl}/password/${idUsuario}`, passwordData, { withCredentials: true }),
                "Error al actualizar la contraseña"
            ),
        [baseUrl, handleRequest]
    );

    const actualizarEstadoActivo = useCallback(
        (idUsuario, nuevoEstadoActivo) =>
            handleRequest(
                () => axios.patch(`${baseUrl}/${idUsuario}/activo`, null, { 
                    params: { activo: nuevoEstadoActivo }, 
                    withCredentials: true 
                }),
                "Error al actualizar el estado activo"
            ),
        [baseUrl, handleRequest]
    );

    return {
        obtenerUsuarioPorId,
        obtenerUsuariosPaginados,
        actualizarInfo,
        actualizarPassword,
        actualizarEstadoActivo,
        loading,
        error
    };
}
