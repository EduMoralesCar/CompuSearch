import { useState, useCallback } from "react";
import axios from "axios";

const API_BASE_URL = "http://localhost:8080/plan";

export function usePlanes() {
    const [planes, setPlanes] = useState([]);
    const [planDetalle, setPlanDetalle] = useState(null);
    const [totalPages, setTotalPages] = useState(0);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    // Manejo centralizado de errores
    const handleError = useCallback((err, defaultMessage) => {
        const message = err.response?.data?.message || defaultMessage;
        console.error("Error en la solicitud:", err);
        setError(message);
        return message;
    }, []);

    const clearError = useCallback(() => setError(null), []);

    const obtenerTodosLosPlanes = useCallback(async (page = 0, size = 10, nombre = "", incluirInactivos = false) => {
        setLoading(true);
        setError(null);
        try {
            const { data } = await axios.get(API_BASE_URL, {
                params: { page, size, nombre, incluirInactivos },
                withCredentials: true
            });

            setPlanes(data.content || []);
            setTotalPages(data.totalPages || 0);

            return data.content || [];
        } catch (err) {
            handleError(err, "Error al obtener la lista de planes.");
            return [];
        } finally {
            setLoading(false);
        }
    }, [handleError]);

    const obtenerPlanPorId = useCallback(async (id) => {
        setLoading(true);
        setError(null);
        try {
            const { data } = await axios.get(`${API_BASE_URL}/${id}`, { withCredentials: true });
            setPlanDetalle(data);
            return data;
        } catch (err) {
            handleError(err, `Error al obtener el plan con ID: ${id}`);
            setPlanDetalle(null);
            return null;
        } finally {
            setLoading(false);
        }
    }, [handleError]);

    const crearPlan = useCallback(async (planData) => {
        setLoading(true);
        setError(null);
        try {
            const { data } = await axios.post(API_BASE_URL, planData, { withCredentials: true });
            return { success: true, data };
        } catch (err) {
            return { success: false, error: handleError(err, "Error al crear el nuevo plan.") };
        } finally {
            setLoading(false);
        }
    }, [handleError]);

    const actualizarPlan = useCallback(async (id, planData) => {
        setLoading(true);
        setError(null);
        try {
            const { data } = await axios.put(`${API_BASE_URL}/${id}`, planData, { withCredentials: true });

            if (planDetalle?.idPlan === id) {
                setPlanDetalle(data);
            }

            return { success: true, data };
        } catch (err) {
            return { success: false, error: handleError(err, `Error al actualizar el plan con ID: ${id}`) };
        } finally {
            setLoading(false);
        }
    }, [handleError, planDetalle]);

    const actualizarEstadoActivo = useCallback(async (id, activo) => {
        setLoading(true);
        setError(null);
        try {
            await axios.patch(`${API_BASE_URL}/${id}/estado`, null, {
                params: { activo },
                withCredentials: true
            });

            setPlanes(prev => prev.map(p => p.idPlan === id ? { ...p, activo } : p));

            if (planDetalle?.idPlan === id) {
                setPlanDetalle(prev => ({ ...prev, activo }));
            }

            return { success: true };
        } catch (err) {
            return { success: false, error: handleError(err, `Error al cambiar el estado activo del plan ID: ${id}`) };
        } finally {
            setLoading(false);
        }
    }, [handleError, planDetalle]);

    return {
        planes,
        planDetalle,
        totalPages,
        loading,
        error,
        obtenerTodosLosPlanes,
        obtenerPlanPorId,
        crearPlan,
        actualizarPlan,
        actualizarEstadoActivo,
        clearError,
    };
}
