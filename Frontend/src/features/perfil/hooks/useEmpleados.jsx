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

    /**
     * Función de manejo de errores de Axios.
     * @param {Error} err - Objeto de error de Axios.
     * @param {string} defaultMessage - Mensaje por defecto si no se encuentra un mensaje específico.
     * @returns {string} El mensaje de error extraído.
     */
    const handleError = (err, defaultMessage) => {
        // Extrae el mensaje de error de la respuesta si está disponible
        const errorMessage = err.response?.data?.message || err.message || defaultMessage;
        console.error("Error en la solicitud:", err);
        setError(errorMessage);
        return errorMessage; 
    };
    
    // Función de ayuda para limpiar el error
    const clearError = useCallback(() => setError(null), []);

    // 1. GET (Paginado y con filtro por username) - Similar a obtenerTodosLosPlanes
    const getEmpleados = useCallback(async (page = 0, size = 10, username = '') => {
        setIsLoading(true);
        setError(null);
        try {
            const response = await axios.get(API_BASE_URL, {
                params: { page, size, username },
                withCredentials: true // Asumiendo que usas cookies/sesiones
            });
            
            const data = response.data; 
            /** @type {PageResponse} */
            
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

    // 2. GET (Por ID) - Similar a obtenerPlanPorId
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

    // 3. POST (Crear Empleado) - Retorna { success: bool, data?: EmpleadoResponse, error?: string }
    const createEmpleado = useCallback(async (empleadoData) => {
        setIsLoading(true);
        setError(null);
        try {
            const response = await axios.post(API_BASE_URL, empleadoData, {
                withCredentials: true
            });
            
            // Éxito: Retorna un objeto con success: true
            return { success: true, data: response.data }; 

        } catch (err) {
            // Error: Retorna un objeto con success: false y el mensaje de error
            const errorMessage = handleError(err, "Error al crear el nuevo empleado.");
            return { success: false, error: errorMessage };

        } finally {
            setIsLoading(false);
        }
    }, [handleError]); // Añadir handleError como dependencia

    // 4. PUT (Modificar Empleado) - Retorna { success: bool, data?: EmpleadoResponse, error?: string }
    const updateEmpleado = useCallback(async (id, empleadoData) => {
        setIsLoading(true);
        setError(null);
        try {
            const response = await axios.put(`${API_BASE_URL}/${id}`, empleadoData, {
                withCredentials: true
            });

            // Si el empleado detallado actual es el que se actualizó, se refresca su estado
            if (empleadoDetalle && empleadoDetalle.idUsuario === id) {
                setEmpleadoDetalle(response.data);
            }
            
            // Éxito: Retorna un objeto con success: true
            return { success: true, data: response.data };

        } catch (err) {
            // Error: Retorna un objeto con success: false y el mensaje de error
            const errorMessage = handleError(err, `Error al actualizar el empleado con ID: ${id}`);
            return { success: false, error: errorMessage };
            
        } finally {
            setIsLoading(false);
        }
    }, [empleadoDetalle, handleError]); // Añadir empleadoDetalle y handleError como dependencia
    
    // 5. PATCH (Actualizar estado activo) - Retorna { success: bool, error?: string }
    const updateEmpleadoActivo = useCallback(async (id, activo) => {
        setIsLoading(true);
        setError(null);
        try {
            // Nota: El endpoint en tu controlador original usa "/activo"
            await axios.patch(`${API_BASE_URL}/${id}/activo`, null, {
                params: { activo },
                withCredentials: true
            });

            // Actualiza inmediatamente la lista de empleados si el cambio fue exitoso
            setEmpleados(prevEmpleados => 
                prevEmpleados.map(e => 
                    e.idUsuario === id ? { ...e, activo } : e
                )
            );

            // También actualiza el detalle si es el empleado que se está visualizando
            if (empleadoDetalle && empleadoDetalle.idUsuario === id) {
                setEmpleadoDetalle(prevDetalle => ({ ...prevDetalle, activo }));
            }
            
            // Éxito: Retorna un objeto con success: true
            return { success: true }; 

        } catch (err) {
            // Error: Retorna un objeto con success: false y el mensaje de error
            const errorMessage = handleError(err, `Error al cambiar el estado activo del empleado ID: ${id}`);
            return { success: false, error: errorMessage };
            
        } finally {
            setIsLoading(false);
        }
    }, [empleadoDetalle, handleError]); // Añadir empleadoDetalle y handleError como dependencia


    // Retorna las funciones y los estados
    return useMemo(() => ({
        // Estados
        empleados,
        empleadoDetalle,
        totalPages,
        isLoading,
        error,
        
        // Funciones de la API
        getEmpleados,
        getEmpleadoById,
        createEmpleado,
        updateEmpleado,
        updateEmpleadoActivo,
        
        // Helper para limpiar errores
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