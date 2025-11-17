import { useState, useCallback } from "react";
import axios from "axios";

/**
 * Hook personalizado para la gestión administrativa de tiendas.
 * Utiliza los endpoints /api/admin/tiendas para CRUD (Lectura y Modificación de estado).
 */
export function useTiendas() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    // Ajustamos la URL base al prefijo administrativo que definimos previamente.
    const baseUrl = "http://localhost:8080/tiendas";

    // --- MÉTODOS DE LECTURA (GET) ---

    /**
     * Obtiene una lista paginada y opcionalmente filtrada de tiendas.
     * GET /api/admin/tiendas?page=...&size=...&nombre=...&activo=...
     * @param {number} page - Número de página (por defecto 0).
     * @param {number} size - Tamaño de la página (por defecto 10).
     * @param {Object} [filters={}] - Objeto con filtros opcionales (e.g., { nombre: "Tienda X", activo: true }).
     */
    const obtenerTiendasPaginadas = useCallback(async (page = 0, size = 10, filters = {}) => {
        setLoading(true);
        setError(null);

        const params = { 
            page, 
            size,
            ...filters // Incluye filtros opcionales como 'nombre', 'activo', 'verificado'
        };

        try {
            const response = await axios.get(
                baseUrl,
                { params, withCredentials: true }
            );
            // La respuesta contiene la estructura Page de Spring Data
            return { success: true, data: response.data };
        } catch (err) {
            const message = err.response?.data?.message || "Error al obtener la lista de tiendas";
            setError(message);
            return { success: false, error: message };
        } finally {
            setLoading(false);
        }
    }, [baseUrl]);

    /**
     * Obtiene la información detallada de una tienda por su ID.
     * GET /api/admin/tiendas/{idUsuario}
     * @param {number} idUsuario - ID de la tienda (usuario) a buscar.
     */
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

    // --- MÉTODOS DE GESTIÓN DE ESTADO (PUT) ---

    /**
     * Actualiza el estado 'activo' de una tienda (activar o desactivar).
     * PUT /api/admin/tiendas/{idUsuario}/estado
     * @param {number} idUsuario - ID de la tienda.
     * @param {boolean} nuevoEstadoActivo - Nuevo estado booleano (true o false).
     */
    const actualizarEstado = useCallback(async (idUsuario, nuevoEstadoActivo) => {
        setLoading(true);
        setError(null);

        // El cuerpo debe coincidir con el EstadoDTO del backend: { "activo": boolean }
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

    /**
     * Actualiza el estado 'verificado' de una tienda.
     * PUT /api/admin/tiendas/{idUsuario}/verificacion
     * @param {number} idUsuario - ID de la tienda.
     * @param {boolean} nuevoEstadoVerificado - Nuevo estado booleano (true o false).
     */
    const actualizarVerificacion = useCallback(async (idUsuario, nuevoEstadoVerificado) => {
        setLoading(true);
        setError(null);

        // El cuerpo debe coincidir con el VerificacionDTO del backend: { "verificado": boolean }
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