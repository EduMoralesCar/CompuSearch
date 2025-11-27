import { useState, useCallback } from 'react';
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/plan';

export function usePlanes() {
    const [planes, setPlanes] = useState([]);
    const [planDetalle, setPlanDetalle] = useState(null);
    const [totalPages, setTotalPages] = useState(0);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleError = (err, defaultMessage) => {
        const errorMessage = err.response?.data?.message || defaultMessage;
        console.error("Error en la solicitud:", err);
        setError(errorMessage);
        return errorMessage; // Retorna solo el mensaje para que la función llamadora decida qué devolver.
    };
    
    const clearError = useCallback(() => setError(null), []);

    const obtenerTodosLosPlanes = useCallback(async (page = 0, size = 10, nombre = '', incluirInactivos = false) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.get(API_BASE_URL, {
                params: { page, size, nombre, incluirInactivos },
                withCredentials: true
            });
            const data = response.data; 
            setPlanes(data.content || []);
            setTotalPages(data.totalPages || 0);
            return data.content || [];
        } catch (err) {
            handleError(err, "Error al obtener la lista de planes.");
            return [];
        } finally {
            setLoading(false);
        }
    }, []);

    const obtenerPlanPorId = useCallback(async (id) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.get(`${API_BASE_URL}/${id}`, {
                withCredentials: true
            });
            setPlanDetalle(response.data);
            return response.data;
        } catch (err) {
            handleError(err, `Error al obtener el plan con ID: ${id}`);
            setPlanDetalle(null);
            return null;
        } finally {
            setLoading(false);
        }
    }, []);

    const crearPlan = async (planData) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.post(API_BASE_URL, planData, {
                withCredentials: true
            });
            
            // Éxito: Retorna un objeto con success: true
            return { success: true, data: response.data }; 

        } catch (err) {
            // Error: Retorna un objeto con success: false y el mensaje de error
            const errorMessage = handleError(err, "Error al crear el nuevo plan.");
            return { success: false, error: errorMessage };

        } finally {
            setLoading(false);
        }
    };

    const actualizarPlan = async (id, planData) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.put(`${API_BASE_URL}/${id}`, planData, {
                withCredentials: true
            });

            if (planDetalle && planDetalle.idPlan === id) {
                setPlanDetalle(response.data);
            }
            
            return { success: true, data: response.data };

        } catch (err) {
            const errorMessage = handleError(err, `Error al actualizar el plan con ID: ${id}`);
            return { success: false, error: errorMessage };
            
        } finally {
            setLoading(false);
        }
    };
    
    const actualizarEstadoActivo = async (id, activo) => {
        setLoading(true);
        setError(null);
        try {
            await axios.patch(`${API_BASE_URL}/${id}/estado`, null, {
                params: { activo },
                withCredentials: true
            });

            setPlanes(prevPlanes => 
                prevPlanes.map(p => 
                    p.idPlan === id ? { ...p, activo } : p
                )
            );

            if (planDetalle && planDetalle.idPlan === id) {
                setPlanDetalle(prevDetalle => ({ ...prevDetalle, activo }));
            }
            
            return { success: true }; 

        } catch (err) {
            const errorMessage = handleError(err, `Error al cambiar el estado activo del plan ID: ${id}`);
            return { success: false, error: errorMessage };
            
        } finally {
            setLoading(false);
        }
    };


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