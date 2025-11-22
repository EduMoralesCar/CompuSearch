import { useState, useCallback } from 'react';
import axios from 'axios';

// URL base de tu controlador de Spring Boot
const API_BASE_URL = 'http://localhost:8080/plan';

/**
 * Hook personalizado de React para manejar las operaciones CRUD y paginadas 
 * de los planes de suscripción, interactuando con la API REST.
 * @returns {object} Un objeto con estados (loading, error, planes, totalPages) 
 * y las funciones para interactuar con la API.
 */
export function usePlanes() {
    const [planes, setPlanes] = useState([]);
    const [planDetalle, setPlanDetalle] = useState(null);
    const [totalPages, setTotalPages] = useState(0);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    // FUNCIÓN DE MANEJO DE ERRORES CORREGIDA PARA SOLO FIJAR EL ESTADO
    const handleError = (err, defaultMessage) => {
        const errorMessage = err.response?.data?.message || defaultMessage;
        console.error("Error en la solicitud:", err);
        setError(errorMessage);
        return errorMessage; // Retorna solo el mensaje para que la función llamadora decida qué devolver.
    };
    
    // Función de ayuda para limpiar el error
    const clearError = useCallback(() => setError(null), []);

    // ... (obtenerTodosLosPlanes y obtenerPlanPorId se mantienen)

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


    // =========================================================
    // MODIFICACIÓN 1: CREAR PLAN (Retorna {success: bool, error: str})
    // =========================================================
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

    // =========================================================
    // MODIFICACIÓN 2: ACTUALIZAR PLAN (Retorna {success: bool, error: str})
    // =========================================================
    const actualizarPlan = async (id, planData) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.put(`${API_BASE_URL}/${id}`, planData, {
                withCredentials: true
            });

            // Si el plan detallado actual es el que se actualizó, se refresca su estado
            if (planDetalle && planDetalle.idPlan === id) {
                setPlanDetalle(response.data);
            }
            
            // Éxito: Retorna un objeto con success: true
            return { success: true, data: response.data };

        } catch (err) {
            // Error: Retorna un objeto con success: false y el mensaje de error
            const errorMessage = handleError(err, `Error al actualizar el plan con ID: ${id}`);
            return { success: false, error: errorMessage };
            
        } finally {
            setLoading(false);
        }
    };
    
    // =========================================================
    // MODIFICACIÓN 3: ACTUALIZAR ESTADO ACTIVO (Retorna {success: bool, error: str})
    // =========================================================
    const actualizarEstadoActivo = async (id, activo) => {
        setLoading(true);
        setError(null);
        try {
            await axios.patch(`${API_BASE_URL}/${id}/estado`, null, {
                params: { activo },
                withCredentials: true
            });

            // Actualiza inmediatamente la lista de planes si el cambio fue exitoso
            setPlanes(prevPlanes => 
                prevPlanes.map(p => 
                    p.idPlan === id ? { ...p, activo } : p
                )
            );

            // También actualiza el detalle si es el plan que se está visualizando
            if (planDetalle && planDetalle.idPlan === id) {
                setPlanDetalle(prevDetalle => ({ ...prevDetalle, activo }));
            }
            
            // Éxito: Retorna un objeto con success: true
            return { success: true }; 

        } catch (err) {
            // Error: Retorna un objeto con success: false y el mensaje de error
            const errorMessage = handleError(err, `Error al cambiar el estado activo del plan ID: ${id}`);
            return { success: false, error: errorMessage };
            
        } finally {
            setLoading(false);
        }
    };


    return {
        // Estados
        planes,
        planDetalle,
        totalPages,
        loading,
        error,

        // Funciones de la API
        obtenerTodosLosPlanes,
        obtenerPlanPorId,
        crearPlan,
        actualizarPlan,
        actualizarEstadoActivo,
        // Helper para limpiar errores si la UI lo requiere
        clearError, 
    };
}