import { useState, useCallback } from "react";
import axios from "axios";

export function useUsuario() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const baseUrl = "http://localhost:8080/usuario";

    // --- MÉTODOS DE LECTURA (GET) ---

    /**
     * Obtiene la información detallada de un usuario por su ID.
     * GET /usuario/{id}
     * @param {number} idUsuario - ID del usuario a buscar.
     */
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
    }, [baseUrl]); // Dependencia estable

    /**
     * Obtiene una lista paginada y opcionalmente filtrada de usuarios.
     * GET /usuario?page=...&size=...&username=...
     * @param {number} page - Número de página (por defecto 0).
     * @param {number} size - Tamaño de la página (por defecto 10).
     * @param {string} [username] - Filtro opcional por nombre de usuario.
     */
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
            // La respuesta contiene la estructura Page de Spring Data (content, totalElements, etc.)
            return { success: true, data: response.data };
        } catch (err) {
            const message = err.response?.data?.message || "Error al obtener la lista de usuarios";
            setError(message);
            return { success: false, error: message };
        } finally {
            setLoading(false);
        }
    }, [baseUrl]); // Dependencia estable

    // --- MÉTODOS DE ACTUALIZACIÓN (PUT, PATCH) ---

    /**
     * Actualiza la información personal de un usuario.
     * PUT /usuario/{id}
     * @param {number} idUsuario - ID del usuario.
     * @param {Object} infoData - Mapa con los campos a actualizar.
     */
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
    }, [baseUrl]); // Dependencia estable

    /**
     * Actualiza la contraseña de un usuario.
     * PUT /usuario/password/{id}
     * @param {number} idUsuario - ID del usuario.
     * @param {Object} passwordData - {currentPassword, newPassword, confirmPassword}.
     */
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
    }, [baseUrl]); // Dependencia estable

    /**
     * Actualiza el estado 'activo' de un usuario.
     * PATCH /usuario/{id}/activo?activo={boolean}
     * @param {number} idUsuario - ID del usuario.
     * @param {boolean} nuevoEstadoActivo - Nuevo estado booleano (true o false).
     */
    const actualizarEstadoActivo = useCallback(async (idUsuario, nuevoEstadoActivo) => {
        setLoading(true);
        setError(null);

        const params = { activo: nuevoEstadoActivo };

        try {
            const response = await axios.patch(
                `${baseUrl}/${idUsuario}/activo`,
                null, // PATCH sin cuerpo, el valor va en RequestParam
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
    }, [baseUrl]); // Dependencia estable

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