import { useState, useCallback, useMemo } from 'react';
import axios from 'axios';

// --- Definiciones de Tipos (Mock, adaptar según tu DTO real) ---

/**
 * @typedef {Object} EmpleadoResponse
 * @property {number} idUsuario - El ID del usuario/empleado.
 * @property {string} username - Nombre de usuario.
 * @property {string} nombre - Nombre completo.
 * @property {string} email - Correo electrónico.
 * @property {string} telefono - Teléfono.
 * @property {boolean} activo - Estado de actividad del empleado.
 * @property {string} rol - Rol del empleado (e.g., "ADMIN", "USER").
 */

/**
 * @typedef {Omit<EmpleadoResponse, 'idUsuario' | 'activo' | 'rol'> & { password?: string }} EmpleadoRequest
 */

/**
 * @typedef {Object} PageResponse
 * @property {EmpleadoResponse[]} content - Lista de empleados en la página.
 * @property {boolean} last - Si es la última página.
 * @property {number} totalPages - Número total de páginas.
 * @property {number} totalElements - Número total de elementos.
 * @property {number} size - Tamaño de la página actual.
 * @property {number} number - Índice de la página actual (cero basado).
 * @property {boolean} first - Si es la primera página.
 * @property {number} numberOfElements - Número de elementos en la página actual.
 */

// ----------------------------------------------------------------

// URL base de tu API Spring Boot
const API_BASE_URL = 'http://localhost:8080/empleado'; 

/**
 * Hook personalizado de React para manejar las operaciones CRUD y paginadas 
 * de los empleados, interactuando con la API REST utilizando Axios.
 * * @returns {object} Un objeto con estados (loading, error, empleados, totalPages) 
 * y las funciones para interactuar con la API.
 */
const useEmpleados = () => {
    const [empleados, setEmpleados] = useState([]);
    const [empleadoDetalle, setEmpleadoDetalle] = useState(null);
    const [totalPages, setTotalPages] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    // eslint-disable-next-line react-hooks/exhaustive-deps
    const handleError = (err, defaultMessage) => {
        const errorMessage = err.response?.data?.message || err.message || defaultMessage;
        console.error("Error en la solicitud:", err);
        setError(errorMessage);
        return errorMessage; 
    };
    
    const clearError = useCallback(() => setError(null), []);

    const getEmpleados = useCallback(async (page = 0, size = 10, username = '') => {
        setIsLoading(true);
        setError(null);
        try {
            const response = await axios.get(API_BASE_URL, {
                params: { page, size, username },
                withCredentials: true
            });
            
            const data = response.data; 
            
            setEmpleados(data.content || []);
            setTotalPages(data.totalPages || 0);
            return data;
        } catch (err) {
            handleError(err, "Error al obtener la lista de empleados.");
            return { content: [], totalPages: 0 };
        } finally {
            setIsLoading(false);
        }
    }, []);

    const getEmpleadoById = useCallback(async (id) => {
        setIsLoading(true);
        setError(null);
        try {
            const response = await axios.get(`${API_BASE_URL}/${id}`, {
                withCredentials: true
            });
            /** @type {EmpleadoResponse} */
            const data = response.data;
            setEmpleadoDetalle(data);
            return data;
        } catch (err) {
            handleError(err, `Error al obtener el empleado con ID: ${id}`);
            setEmpleadoDetalle(null);
            return null;
        } finally {
            setIsLoading(false);
        }
    }, []);

    const createEmpleado = useCallback(async (empleadoData) => {
        setIsLoading(true);
        setError(null);
        try {
            const response = await axios.post(API_BASE_URL, empleadoData, {
                withCredentials: true
            });
            
            return { success: true, data: response.data }; 

        } catch (err) {
            const errorMessage = handleError(err, "Error al crear el nuevo empleado.");
            return { success: false, error: errorMessage };

        } finally {
            setIsLoading(false);
        }
    }, [handleError]);

    const updateEmpleado = useCallback(async (id, empleadoData) => {
        setIsLoading(true);
        setError(null);
        try {
            const response = await axios.put(`${API_BASE_URL}/${id}`, empleadoData, {
                withCredentials: true
            });

            if (empleadoDetalle && empleadoDetalle.idUsuario === id) {
                setEmpleadoDetalle(response.data);
            }
            
            return { success: true, data: response.data };

        } catch (err) {
            const errorMessage = handleError(err, `Error al actualizar el empleado con ID: ${id}`);
            return { success: false, error: errorMessage };
            
        } finally {
            setIsLoading(false);
        }
    }, [empleadoDetalle, handleError]);
    
    const updateEmpleadoActivo = useCallback(async (id, activo) => {
        setIsLoading(true);
        setError(null);
        try {
            await axios.patch(`${API_BASE_URL}/${id}/activo`, null, {
                params: { activo },
                withCredentials: true
            });

            setEmpleados(prevEmpleados => 
                prevEmpleados.map(e => 
                    e.idUsuario === id ? { ...e, activo } : e
                )
            );

            if (empleadoDetalle && empleadoDetalle.idUsuario === id) {
                setEmpleadoDetalle(prevDetalle => ({ ...prevDetalle, activo }));
            }
            
            return { success: true }; 

        } catch (err) {
            const errorMessage = handleError(err, `Error al cambiar el estado activo del empleado ID: ${id}`);
            return { success: false, error: errorMessage };
            
        } finally {
            setIsLoading(false);
        }
    }, [empleadoDetalle, handleError]);


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
        
        clearError,
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
        clearError
    ]);
};

export default useEmpleados;