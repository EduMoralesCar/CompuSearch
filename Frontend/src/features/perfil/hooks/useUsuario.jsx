import { useState, useCallback } from "react";
import axios from "axios";

export function useUsuario() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const baseUrl = "http://localhost:8080/usuario";

    const obtenerUsuarioPorId = useCallback(async (idUsuario) => {
        setLoading(true);
        setError(null);

        try {
            const response = await axios.get(
                `${baseUrl}/${idUsuario}`,
                { withCredentials: true }
            );
            return { success: true, data: response.data };
        } catch (err) {
            const message = err.response?.data?.message || "Error al obtener información del usuario";
            setError(message);
            return { success: false, error: message };
        } finally {
            setLoading(false);
        }
    }, [baseUrl]);

    const obtenerUsuariosPaginados = useCallback(async (page = 0, size = 10, username = "") => {
        setLoading(true);
        setError(null);

        const params = { page, size };
        if (username) {
            params.username = username;
        }

        try {
            const response = await axios.get(
                baseUrl,
                { params, withCredentials: true }
            );
            return { success: true, data: response.data };
        } catch (err) {
            const message = err.response?.data?.message || "Error al obtener la lista de usuarios";
            setError(message);
            return { success: false, error: message };
        } finally {
            setLoading(false);
        }
    }, [baseUrl]);

    const actualizarInfo = useCallback(async (idUsuario, infoData) => {
        setLoading(true);
        setError(null);

        try {
            const response = await axios.put(
                `${baseUrl}/${idUsuario}`,
                infoData,
                { withCredentials: true }
            );
            return { success: true, data: response.data };
        } catch (err) {
            const message = err.response?.data?.message || "Error al actualizar información";
            setError(message);
            return { success: false, error: message };
        } finally {
            setLoading(false);
        }
    }, [baseUrl]);

    const actualizarPassword = useCallback(async (idUsuario, passwordData) => {
        setLoading(true);
        setError(null);

        try {
            const response = await axios.put(
                `${baseUrl}/password/${idUsuario}`,
                passwordData,
                { withCredentials: true }
            );
            return { success: true, data: response.data };
        } catch (err) {
            const message = err.response?.data?.message || "Error al actualizar la contraseña";
            setError(message);
            return { success: false, error: message };
        } finally {
            setLoading(false);
        }
    }, [baseUrl]);

    const actualizarEstadoActivo = useCallback(async (idUsuario, nuevoEstadoActivo) => {
        setLoading(true);
        setError(null);

        const params = { activo: nuevoEstadoActivo };

        try {
            const response = await axios.patch(
                `${baseUrl}/${idUsuario}/activo`,
                null,
                { params, withCredentials: true }
            );
            return { success: true, data: response.data };
        } catch (err) {
            const message = err.response?.data?.message || "Error al actualizar el estado activo";
            setError(message);
            return { success: false, error: message };
        } finally {
            setLoading(false);
        }
    }, [baseUrl]);

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