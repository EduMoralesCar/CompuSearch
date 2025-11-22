import { useState, useEffect } from "react";
import axios from "axios";

export const useCategorias = () => {
    // Estado para la lista completa (sin paginación)
    const [categorias, setCategorias] = useState([]);
    // Estado para la data de la página actual (incluye contenido, totalPages, totalElements, etc.)
    const [categoriasPage, setCategoriasPage] = useState(null);

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const baseURL = "http://localhost:8080/categorias";

    // --- NUEVO MÉTODO: Obtener categorías con paginación ---
    /**
     * @param {number} page - Índice de la página a solicitar (inicia en 0).
     * @param {number} size - Número de elementos por página.
     * @param {string} [sort='idCategoria,asc'] - Campo y dirección de ordenación (ej: 'nombre,asc' o 'fechaAfiliacion,desc').
     */
    const obtenerCategoriasPaginadas = async (page = 0, size = 10, sort = 'idCategoria,asc') => {
        setLoading(true);
        setError(null);
        try {
            // Construye la URL con los parámetros de paginación
            const url = `${baseURL}?page=${page}&size=${size}&sort=${sort}`;

            const response = await axios.get(url, { withCredentials: true });

            // La respuesta.data es el objeto Page de Spring: { content: [], totalPages: 5, ... }
            setCategoriasPage(response.data);

            return { success: true, response: response.data };
        } catch (err) {
            setError(err.response?.data?.message || "Error al obtener categorías paginadas.");
            return { success: false, error: err.response?.data?.message || "Error desconocido" };
        } finally {
            setLoading(false);
        }
    };
    // --------------------------------------------------------


    // Obtener todas las categorías sin paginacion
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

    // Crear una nueva categoría
    const crearCategoria = async (categoria) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.post(baseURL, categoria, { withCredentials: true });
            // Agregar la nueva categoría al estado de lista completa
            setCategorias(prev => [...prev, response.data]);

            // Nota: Si usas la paginación, necesitarás recargar la página actual después de crear.

            return { success: true, response: response.data }
        } catch (err) {
            setError(err.response?.data?.message || "Error al crear categoría.");
            return { success: false, error: err.response?.data?.message || "Error desconocido" }
        } finally {
            setLoading(false);
        }
    };

    // Actualizar una categoría existente
    const actualizarCategoria = async (id, categoria) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.put(`${baseURL}/${id}`, categoria, { withCredentials: true });
            // Actualizar la lista completa
            setCategorias(prev => prev.map(cat => cat.idCategoria === id ? response.data : cat));

            // Nota: Si la entidad actualizada está en la página actual, necesitarás actualizar categoriasPage.

            return { success: true, response: response.data }
        } catch (err) {
            setError(err.response?.data?.message || "Error al actualizar categoría.");
            return { success: false, error: err.response?.data?.message || "Error desconocido" }
        } finally {
            setLoading(false);
        }
    };

    // Eliminar una categoría
    const eliminarCategoria = async (id) => {
        setLoading(true);
        setError(null);
        try {
            await axios.delete(`${baseURL}/${id}`, { withCredentials: true });
            // Eliminar de la lista completa
            setCategorias(prev => prev.filter(cat => cat.idCategoria !== id));

            // Nota: Si usas paginación, la eliminación podría requerir una recarga de la página actual.

            return { success: true }
        } catch (err) {
            setError(err.response?.data?.message || "Error al eliminar categoría.");
            return { success: false, error: err.response?.data?.message || "Error desconocido" }
        } finally {
            setLoading(false);
        }
    };

    // Cargar categorías al montar (se mantiene la versión sin paginar por defecto)
    useEffect(() => {
        obtenerCategorias();
    }, []);

    return {
        categorias,
        categoriasPage, // <-- Agregado el nuevo estado
        loading,
        error,
        obtenerCategorias,
        obtenerCategoriasPaginadas, // <-- Agregado el nuevo método
        crearCategoria,
        actualizarCategoria,
        eliminarCategoria,
    };
};