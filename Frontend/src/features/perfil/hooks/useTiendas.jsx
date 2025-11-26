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

    const obtenerTiendaPorId = useCallback(async (idUsuario, empleado) => {
        setLoading(true);
        setError(null);

        const url = empleado
            ? `${baseUrl}/empleado/${idUsuario}`
            : `${baseUrl}/tienda/${idUsuario}`;

        try {
            const response = await axios.get(url, { withCredentials: true });
            return { success: true, data: response.data };
        } catch (err) {
            const message = err.response?.data?.message || "Error al obtener información de la tienda";
            setError(message);
            return { success: false, error: message };
        } finally {
            setLoading(false);
        }
    }, [baseUrl]);


    const actualizarDatosTienda = useCallback(async (idUsuario, datosNuevos) => {
        setLoading(true);
        setError(null);

        try {
            const response = await axios.put(
                `${baseUrl}/actualizar/${idUsuario}`,
                datosNuevos,
                { withCredentials: true }
            );
            return { success: true, data: response.message };
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

    const actualizarLogo = useCallback(async (idUsuario, file) => {
        setLoading(true);
        setError(null);

        const formData = new FormData();
        formData.append("logo", file);

        try {
            const response = await axios.put(
                `${baseUrl}/${idUsuario}/logo`,
                formData,
                {
                    headers: { "Content-Type": "multipart/form-data" },
                    withCredentials: true,
                }
            );
            return { success: true, data: response.message };
        } catch (err) {
            const message = err.response?.data?.message || "Error al actualizar el logo.";
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

    const actualizarApi = useCallback(async (idTienda, api) => {
        setLoading(true);
        setError(null);

        try {
            const response = await axios.put(
                `${baseUrl}/${idTienda}/api`,
                api,
                {
                    headers: { "Content-Type": "text/plain" },
                    withCredentials: true
                }
            );
            return { success: true, data: response.data };
        } catch (err) {
            const message = err.response?.data?.message || "Error al actualizar la API.";
            setError(message);
            return { success: false, error: message };
        } finally {
            setLoading(false);
        }
    }, [baseUrl]);

    const obtenerApi = useCallback(async (idTienda) => {
        setLoading(true);
        setError(null);

        try {
            const response = await axios.get(
                `${baseUrl}/${idTienda}/api`,
                { withCredentials: true }
            );
            return { success: true, data: response.data };
        } catch (err) {
            const message = err.response?.data?.message || "Error al obtener la API.";
            setError(message);
            return { success: false, error: message };
        } finally {
            setLoading(false);
        }
    }, [baseUrl]);

    const probarApi = useCallback(async (idTienda) => {
        setLoading(true);
        setError(null);

        try {
            const response = await axios.put(
                `${baseUrl}/${idTienda}/probar`,
                {},
                { withCredentials: true }
            );
            return { success: true, data: response.data };
        } catch (err) {
            const message = err.response?.data?.message || "Error al probar la API.";
            setError(message);
            return { success: false, error: message };
        } finally {
            setLoading(false);
        }
    }, [baseUrl]);

    const obtenerProductosAdmin = async (
        idTienda,
        page = 0,
        size = 12,
        categoria = null,
        sort = "precio,asc"
    ) => {
        setLoading(true);
        setError(null);

        try {
            const response = await axios.get(
                `http://localhost:8080/componentes/${idTienda}`,
                {
                    params: {
                        page,
                        size,
                        categoria: categoria || undefined,
                        sort
                    },
                    withCredentials: true
                }
            );

            return { success: true, data: response.data };

        } catch (err) {
            const message = err.response?.data?.message ||
                "Error al obtener productos para el administrador tienda.";

            setError(message);
            return { success: false, error: message };

        } finally {
            setLoading(false);
        }
    };


    const cambiarHabilitadoProducto = async (idProductoTienda, habilitado) => {
        setLoading(true);
        setError(null);

        try {
            const response = await axios.patch(
                `http://localhost:8080/componentes/${idProductoTienda}/habilitado`,
                null,
                {
                    params: { habilitado },
                    withCredentials: true
                }
            );

            return { success: true, data: response.data };
        } catch (err) {
            const message = err.response?.data?.message
                || "Error al cambiar habilitado.";
            setError(message);
            return { success: false, error: message };
        } finally {
            setLoading(false);
        }
    };

    const obtenerProductosDesdeApi = async (idTienda) => {
        setLoading(true);
        setError(null);

        try {
            const response = await axios.post(
                `http://localhost:8080/api/${idTienda}/actualizar`,
                null,
                {
                    withCredentials: true
                }
            );

            return { success: true, data: response.data };
        } catch (err) {
            const message = err.response?.data?.message
                || "Error al actualizar productos desde la api";
            setError(message);
            return { success: false, error: message };
        } finally {
            setLoading(false);
        }
    };

    return {
        obtenerProductosDesdeApi,
        obtenerProductosAdmin,
        cambiarHabilitadoProducto,
        obtenerTiendasPaginadas,
        obtenerTiendaPorId,
        actualizarEstado,
        actualizarVerificacion,
        actualizarDatosTienda,
        actualizarLogo,
        actualizarApi,
        obtenerApi,
        probarApi,
        loading,
        error
    };
}