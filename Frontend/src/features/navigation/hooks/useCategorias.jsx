import { useState, useEffect } from "react";
import axios from "axios";

export const useCategorias = () => {
    const [categorias, setCategorias] = useState([]);
    const [categoriasPage, setCategoriasPage] = useState(null);

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const baseURL = "http://localhost:8080/categorias";

    const obtenerCategoriasPaginadas = async (page = 0, size = 10, sort = 'idCategoria,asc') => {
        setLoading(true);
        setError(null);
        try {
            const url = `${baseURL}?page=${page}&size=${size}&sort=${sort}`;

            const response = await axios.get(url, { withCredentials: true });

            setCategoriasPage(response.data);

            return { success: true, response: response.data };
        } catch (err) {
            setError(err.response?.data?.message || "Error al obtener categorías paginadas.");
            return { success: false, error: err.response?.data?.message || "Error desconocido" };
        } finally {
            setLoading(false);
        }
    };

    const obtenerCategorias = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.get(`${baseURL}/todas`, { withCredentials: true });
            setCategorias(response.data);
            return { success: true, response: response.data }
        } catch (err) {
            setError(err.response?.data?.message || "Error al obtener todas las categorías.");
            return { success: false, error: err.response?.data?.message || "Error desconocido" }
        } finally {
            setLoading(false);
        }
    };

    const crearCategoria = async (categoria) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.post(baseURL, categoria, { withCredentials: true });
            setCategorias(prev => [...prev, response.data]);
            return { success: true, response: response.data }
        } catch (err) {
            setError(err.response?.data?.message || "Error al crear categoría.");
            return { success: false, error: err.response?.data?.message || "Error desconocido" }
        } finally {
            setLoading(false);
        }
    };

    const actualizarCategoria = async (id, categoria) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.put(`${baseURL}/${id}`, categoria, { withCredentials: true });
            setCategorias(prev => prev.map(cat => cat.idCategoria === id ? response.data : cat));
            return { success: true, response: response.data }
        } catch (err) {
            setError(err.response?.data?.message || "Error al actualizar categoría.");
            return { success: false, error: err.response?.data?.message || "Error desconocido" }
        } finally {
            setLoading(false);
        }
    };

    const eliminarCategoria = async (id) => {
        setLoading(true);
        setError(null);
        try {
            await axios.delete(`${baseURL}/${id}`, { withCredentials: true });
            setCategorias(prev => prev.filter(cat => cat.idCategoria !== id));
            return { success: true }
        } catch (err) {
            setError(err.response?.data?.message || "Error al eliminar categoría.");
            return { success: false, error: err.response?.data?.message || "Error desconocido" }
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        obtenerCategorias();
    }, []);

    return {
        categorias,
        categoriasPage,
        loading,
        error,
        obtenerCategorias,
        obtenerCategoriasPaginadas,
        crearCategoria,
        actualizarCategoria,
        eliminarCategoria,
    };
};