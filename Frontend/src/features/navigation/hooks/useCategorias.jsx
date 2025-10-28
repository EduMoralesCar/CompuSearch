import { useState, useEffect } from "react";
import axios from "axios";

export const useCategorias = () => {
    const [categorias, setCategorias] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const baseURL = "http://localhost:8080/categorias";

    // Obtener todas las categorías
    const obtenerCategorias = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.get(baseURL, {withCredentials: true});
            setCategorias(response.data);
            return { success: true, response: response.data}
        } catch (err) {
            setError(err.response.data.message);
            return { success: false, error: err.response.data.message }
        } finally {
            setLoading(false);
        }
    };

    // Crear una nueva categoría
    const crearCategoria = async (categoria) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.post(baseURL, categoria, {withCredentials: true});
            // Agregar la nueva categoría al estado
            setCategorias(prev => [...prev, response.data]);
            return { success: true, response: response.data}
        } catch (err) {
            setError(err.response.data.message);
            return { success: false, error: err.response.data.message }
        } finally {
            setLoading(false);
        }
    };

    // Actualizar una categoría existente
    const actualizarCategoria = async (id, categoria) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.put(`${baseURL}/${id}`, categoria, {withCredentials: true});
            setCategorias(prev => prev.map(cat => cat.idCategoria === id ? response.data : cat));
            return { success: true, response: response.data}
        } catch (err) {
            setError(err.response.data.message);
            return { success: false, error: err.response.data.message }
        } finally {
            setLoading(false);
        }
    };

    // Eliminar una categoría
    const eliminarCategoria = async (id) => {
        setLoading(true);
        setError(null);
        try {
            await axios.delete(`${baseURL}/${id}`, {withCredentials: true});
            setCategorias(prev => prev.filter(cat => cat.idCategoria !== id));
            return { success: true }
        } catch (err) { 
            setError(err.response.data.message);
            return { success: false, error: err.response.data.message }
        } finally {
            setLoading(false);
        }
    };

    // Cargar categorías al montar
    useEffect(() => {
        obtenerCategorias();
    }, []);

    return {
        categorias,
        loading,
        error,
        obtenerCategorias,
        crearCategoria,
        actualizarCategoria,
        eliminarCategoria,
    };
};
