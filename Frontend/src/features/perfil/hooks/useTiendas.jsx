import { useState, useCallback } from "react";
import axios from "axios";

export function useTiendas() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const baseUrl = "http://localhost:8080/tiendas";

    // Función genérica para llamadas a API
    const callApi = useCallback(async (apiCall, errorMsg = "Error en la operación") => {
        setLoading(true);
        setError(null);

        try {
            const response = await apiCall();
            return { success: true, data: response.data };
        } catch (err) {
            const message = err.response?.data?.message || errorMsg;
            setError(message);
            return { success: false, error: message };
        } finally {
            setLoading(false);
        }
    }, []);

    // ---- Funciones del hook ----
    const obtenerTiendasPaginadas = useCallback(
        (page = 0, size = 10, filters = {}) => 
            callApi(
                () => axios.get(baseUrl, { params: { page, size, ...filters }, withCredentials: true }),
                "Error al obtener la lista de tiendas"
            ),
        [baseUrl, callApi]
    );

    const obtenerTiendaPorId = useCallback(
        (idUsuario, empleado) =>
            callApi(
                () => axios.get(
                    empleado ? `${baseUrl}/empleado/${idUsuario}` : `${baseUrl}/tienda/${idUsuario}`,
                    { withCredentials: true }
                ),
                "Error al obtener información de la tienda"
            ),
        [baseUrl, callApi]
    );

    const actualizarDatosTienda = useCallback(
        (idUsuario, datosNuevos) =>
            callApi(
                () => axios.put(`${baseUrl}/actualizar/${idUsuario}`, datosNuevos, { withCredentials: true }),
                "Error al actualizar los datos de la tienda"
            ),
        [baseUrl, callApi]
    );

    const actualizarEstado = useCallback(
        (idUsuario, nuevoEstadoActivo) =>
            callApi(
                () => axios.put(`${baseUrl}/${idUsuario}/estado`, { activo: nuevoEstadoActivo }, { withCredentials: true }),
                `Error al ${nuevoEstadoActivo ? 'activar' : 'desactivar'} la tienda`
            ),
        [baseUrl, callApi]
    );

    const actualizarVerificacion = useCallback(
        (idUsuario, nuevoEstadoVerificado) =>
            callApi(
                () => axios.put(`${baseUrl}/${idUsuario}/verificacion`, { verificado: nuevoEstadoVerificado }, { withCredentials: true }),
                "Error al cambiar el estado de verificación"
            ),
        [baseUrl, callApi]
    );

    const actualizarLogo = useCallback(
        (idUsuario, file) => {
            const formData = new FormData();
            formData.append("logo", file);
            return callApi(
                () => axios.put(`${baseUrl}/${idUsuario}/logo`, formData, { headers: { "Content-Type": "multipart/form-data" }, withCredentials: true }),
                "Error al actualizar el logo"
            );
        },
        [baseUrl, callApi]
    );

    const actualizarApi = useCallback(
        (idTienda, api) => callApi(
            () => axios.put(`${baseUrl}/${idTienda}/api`, api, { headers: { "Content-Type": "text/plain" }, withCredentials: true }),
            "Error al actualizar la API"
        ),
        [baseUrl, callApi]
    );

    const obtenerApi = useCallback(
        (idTienda) => callApi(
            () => axios.get(`${baseUrl}/${idTienda}/api`, { withCredentials: true }),
            "Error al obtener la API"
        ),
        [baseUrl, callApi]
    );

    const probarApi = useCallback(
        (idTienda) => callApi(
            () => axios.put(`${baseUrl}/${idTienda}/probar`, {}, { withCredentials: true }),
            "Error al probar la API"
        ),
        [baseUrl, callApi]
    );

    const obtenerProductosAdmin = useCallback(
        (idTienda, page = 0, size = 12, categoria = null, sort = "precio,asc") => 
            callApi(
                () => axios.get(`http://localhost:8080/componentes/${idTienda}`, { params: { page, size, categoria: categoria || undefined, sort }, withCredentials: true }),
                "Error al obtener productos para el administrador de la tienda"
            ),
        [callApi]
    );

    const cambiarHabilitadoProducto = useCallback(
        (idProductoTienda, habilitado) =>
            callApi(
                () => axios.patch(`http://localhost:8080/componentes/${idProductoTienda}/habilitado`, null, { params: { habilitado }, withCredentials: true }),
                "Error al cambiar habilitado"
            ),
        [callApi]
    );

    const obtenerProductosDesdeApi = useCallback(
        (idTienda) =>
            callApi(
                () => axios.post(`http://localhost:8080/api/${idTienda}/actualizar`, null, { withCredentials: true }),
                "Error al actualizar productos desde la API"
            ),
        [callApi]
    );

    const ObtenerTiendaDashboard = useCallback(
        (idTienda) =>
            callApi(
                () => axios.get(`http://localhost:8080/tiendas/dashboard/${idTienda}`, { withCredentials: true }),
                "Error al obtener dashboard de la tienda"
            ),
        [callApi]
    );

    return {
        ObtenerTiendaDashboard,
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
