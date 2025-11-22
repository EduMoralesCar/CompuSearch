import { useState, useCallback } from 'react';
import axios from 'axios';

// Define la URL base de tu API
const API_URL = 'http://localhost:8080/metricas'; // Asegúrate de usar el puerto correcto

/**
 * Hook personalizado para manejar la lógica de las métricas (CRUD y actualizaciones).
 */
export const useMetricas = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [metricas, setMetricas] = useState([]); // Para almacenar todas las métricas

    // Función genérica para manejar peticiones de actualización
    const handleUpdate = useCallback(async (idProductoTienda, path) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.patch(`${API_URL}/${idProductoTienda}/${path}`);

            // Actualiza la métrica en el estado local de React
            setMetricas(prevMetricas =>
                prevMetricas.map(m =>
                    m.idProductoTienda === idProductoTienda ? response.data : m
                )
            );

            return response.data;
        } catch (err) {
            setError('Error al actualizar la métrica.');
            console.error(`Error en ${path}:`, err);
            return null;
        } finally {
            setLoading(false);
        }
    }, []);

    // 1. Obtener todas las métricas
    const fetchAllMetricas = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.get(API_URL);
            setMetricas(response.data);
            return response.data;
        // eslint-disable-next-line no-unused-vars
        } catch (err) {
            setError('Error al cargar todas las métricas.');
        } finally {
            setLoading(false);
        }
    }, []);

    // 2. Obtener una métrica por ID de Producto
    const fetchMetricaByProductoId = useCallback(async (idProductoTienda) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.get(`${API_URL}/${idProductoTienda}`);
            setLoading(false);
            return response.data;
        // eslint-disable-next-line no-unused-vars
        } catch (err) {
            setError(`Métrica para el producto ${idProductoTienda} no encontrada.`);
            setLoading(false);
            return null;
        }
    }, []);

    // 3. Crear una nueva métrica (POST)
    const createMetrica = useCallback(async (metricaData) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.post(API_URL, metricaData);
            setMetricas(prevMetricas => [...prevMetricas, response.data]);
            return response.data;
        // eslint-disable-next-line no-unused-vars
        } catch (err) {
            setError('Error al crear la métrica.');
            return null;
        } finally {
            setLoading(false);
        }
    }, []);

    // 4. Eliminar una métrica por su ID (DELETE)
    const deleteMetrica = useCallback(async (idMetrica) => {
        setLoading(true);
        setError(null);
        try {
            await axios.delete(`${API_URL}/${idMetrica}`);
            // Eliminar del estado local
            setMetricas(prevMetricas =>
                prevMetricas.filter(m => m.idMetrica !== idMetrica)
            );
            return true;
        // eslint-disable-next-line no-unused-vars
        } catch (err) {
            setError('Error al eliminar la métrica.');
            return false;
        } finally {
            setLoading(false);
        }
    }, []);

    // --- Métodos de Incremento (PATCH) ---

    const incrementarVisitas = (idProductoTienda) => handleUpdate(idProductoTienda, 'visitas');
    const incrementarClicks = (idProductoTienda) => handleUpdate(idProductoTienda, 'clicks');
    const incrementarBuilds = (idProductoTienda) => handleUpdate(idProductoTienda, 'builds');


    return {
        metricas,
        loading,
        error,
        fetchAllMetricas,
        fetchMetricaByProductoId,
        createMetrica,
        deleteMetrica,
        incrementarVisitas,
        incrementarClicks,
        incrementarBuilds,
    };
};