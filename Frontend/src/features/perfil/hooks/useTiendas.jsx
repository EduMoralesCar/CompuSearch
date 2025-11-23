import { useState, useCallback } from "react";
import axios from "axios";

export function useTiendas() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const baseUrl = "http://localhost:8080/tiendas";

    const obtenerTiendasPaginadas = useCallback(async (page = 0, size = 10, filters = {}) => {
        setLoading(true);
        setError(null);

        const params = { 
            page, 
            size,
            ...filters
        };

        try {
            const response = await axios.get(
                baseUrl,
                { params, withCredentials: true }
            );
            console.log(response.data)
            return { success: true, data: response.data };
        } catch (err) {
            const message = err.response?.data?.message || "Error al obtener la lista de tiendas";
            setError(message);
            return { success: false, error: message };
        } finally {
            setLoading(false);
        }
    }, [baseUrl]);

    const obtenerTiendaPorId = useCallback(async (idUsuario) => {
        setLoading(true);
        setError(null);

        try {
            const response = await axios.get(
                `${baseUrl}/${idUsuario}`,
                { withCredentials: true }
            );
            return { success: true, data: response.data };
        } catch (err) {
            const message = err.response?.data?.message || "Error al obtener información de la tienda";
            setError(message);
            return { success: false, error: message };
        } finally {
            setLoading(false);
        }
    }, [baseUrl]);

    const actualizarEstado = useCallback(async (idUsuario, nuevoEstadoActivo) => {
        setLoading(true);
        setError(null);

        const estadoDTO = { activo: nuevoEstadoActivo };

        try {
            const response = await axios.put(
                `${baseUrl}/${idUsuario}/estado`,
                estadoDTO,
                { withCredentials: true }
            );
            return { success: true, data: response.data };
        } catch (err) {
            const message = err.response?.data?.message || `Error al ${nuevoEstadoActivo ? 'activar' : 'desactivar'} la tienda.`;
            setError(message);
            return { success: false, error: message };
        } finally {
            setLoading(false);
        }
    }, [baseUrl]);

    const actualizarVerificacion = useCallback(async (idUsuario, nuevoEstadoVerificado) => {
        setLoading(true);
        setError(null);

        const verificacionDTO = { verificado: nuevoEstadoVerificado };

        try {
            const response = await axios.put(
                `${baseUrl}/${idUsuario}/verificacion`,
                verificacionDTO,
                { withCredentials: true }
            );
            return { success: true, data: response.data };
        } catch (err) {
            const message = err.response?.data?.message || `Error al cambiar el estado de verificación.`;
            setError(message);
            return { success: false, error: message };
        } finally {
            setLoading(false);
        }
    }, [baseUrl]);


    return {
        obtenerTiendasPaginadas,
        obtenerTiendaPorId,
        actualizarEstado,
        actualizarVerificacion,
        loading,
        error
    };
}