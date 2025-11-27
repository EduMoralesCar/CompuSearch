import { useState, useCallback, useMemo } from 'react';
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/empleado';

const useEmpleados = () => {
    const [empleados, setEmpleados] = useState([]);
    const [empleadoDetalle, setEmpleadoDetalle] = useState(null);
    const [totalPages, setTotalPages] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    // Helper genérico para llamadas a API
    const callApi = useCallback(async (apiCall, defaultErrorMessage) => {
        setIsLoading(true);
        setError(null);
        try {
            const response = await apiCall();
            return { success: true, data: response.data };
        } catch (err) {
            const errorMessage = err.response?.data?.message || err.message || defaultErrorMessage;
            console.error("Error en la solicitud:", err);
            setError(errorMessage);
            return { success: false, error: errorMessage };
        } finally {
            setIsLoading(false);
        }
    }, []);

    const clearError = useCallback(() => setError(null), []);

    const getEmpleados = useCallback((page = 0, size = 10, username = '') => 
        callApi(
            () => axios.get(API_BASE_URL, { params: { page, size, username }, withCredentials: true }),
            "Error al obtener la lista de empleados."
        ).then(result => {
            if (result.success) {
                setEmpleados(result.data.content || []);
                setTotalPages(result.data.totalPages || 0);
            }
            return result;
        }),
        [callApi]
    );

    const getEmpleadoById = useCallback((id) => 
        callApi(
            () => axios.get(`${API_BASE_URL}/${id}`, { withCredentials: true }),
            `Error al obtener el empleado con ID: ${id}`
        ).then(result => {
            setEmpleadoDetalle(result.success ? result.data : null);
            return result;
        }),
        [callApi]
    );

    const createEmpleado = useCallback((empleadoData) =>
        callApi(() => axios.post(API_BASE_URL, empleadoData, { withCredentials: true }), "Error al crear el empleado."),
        [callApi]
    );

    const updateEmpleado = useCallback((id, empleadoData) =>
        callApi(() => axios.put(`${API_BASE_URL}/${id}`, empleadoData, { withCredentials: true }), `Error al actualizar el empleado con ID: ${id}`)
            .then(result => {
                if (result.success && empleadoDetalle?.idUsuario === id) {
                    setEmpleadoDetalle(result.data);
                }
                return result;
            }),
        [callApi, empleadoDetalle]
    );

    const updateEmpleadoActivo = useCallback((id, activo) =>
        callApi(() => axios.patch(`${API_BASE_URL}/${id}/activo`, null, { params: { activo }, withCredentials: true }),
            `Error al cambiar el estado activo del empleado ID: ${id}`)
        .then(result => {
            if (result.success) {
                setEmpleados(prev => prev.map(e => e.idUsuario === id ? { ...e, activo } : e));
                if (empleadoDetalle?.idUsuario === id) {
                    setEmpleadoDetalle(prev => ({ ...prev, activo }));
                }
            }
            return result;
        }),
        [callApi, empleadoDetalle]
    );

    const obtenerEmpleadoDashboard = useCallback(() =>
        callApi(() => axios.get(`${API_BASE_URL}/dashboard`, { withCredentials: true }),
            "Error al obtener el dashboard del empleado"),
        [callApi]
    );

    return useMemo(() => ({
        empleados,
        empleadoDetalle,
        totalPages,
        isLoading,
        error,
        getEmpleados,
        getEmpleadoById,
        createEmpleado,
        updateEmpleado,
        updateEmpleadoActivo,
        obtenerEmpleadoDashboard,
        clearError
    }), [
        empleados,
        empleadoDetalle,
        totalPages,
        isLoading,
        error,
        getEmpleados,
        getEmpleadoById,
        createEmpleado,
        updateEmpleado,
        updateEmpleadoActivo,
        obtenerEmpleadoDashboard,
        clearError
    ]);
};

export default useEmpleados;
