import { useState, useEffect } from "react";
import axios from "axios";

const BASE_URL = "http://localhost:8080/etiquetas";

export default function useEtiquetas() {
    // Estado para la lista sin paginación (manteniendo la compatibilidad)
    const [etiquetas, setEtiquetas] = useState([]);
    
    // ✅ Nuevos estados para la paginación
    const [etiquetasPaginadas, setEtiquetasPaginadas] = useState([]); // Contenido de la página actual
    const [totalPages, setTotalPages] = useState(0); 
    const [number, setNumber] = useState(0); // Número de página actual (índice 0)
    const [totalElements, setTotalElements] = useState(0); // Total de elementos
    
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    // --- MÉTODOS DE LECTURA ---

    /**
     * ✅ NUEVO MÉTODO: Obtiene etiquetas con paginación desde el endpoint principal.
     * @param {number} page - Índice de la página (0-based).
     * @param {number} size - Número de elementos por página.
     * @param {string} [sort='idEtiqueta,asc'] - Criterio de ordenación.
     */
    const cargarEtiquetasPaginadas = async (page = 0, size = 10, sort = 'idEtiqueta,asc') => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.get(BASE_URL, { 
                params: { page, size, sort }, 
                withCredentials: true 
            });
            const data = response.data;
            
            // Actualizar los estados con la metadata de la respuesta Page de Spring
            setEtiquetasPaginadas(data.content || []);
            setTotalPages(data.totalPages || 0);
            setNumber(data.number || 0);
            setTotalElements(data.totalElements || 0);
            
        } catch (err) {
            setError(err.response?.data?.message || "Error al cargar etiquetas paginadas");
        } finally {
            setLoading(false);
        }
    };

    // Obtener todas las etiquetas sin paginacion (Mantiene el endpoint /todas)
    const cargarEtiquetas = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.get(`${BASE_URL}/todas`, { withCredentials: true });
            setEtiquetas(response.data || []);
        } catch (err) {
            setError(err.response?.data?.message || "Error al cargar todas las etiquetas");
        } finally {
            setLoading(false);
        }
    };

    // --- MÉTODOS DE ESCRITURA (Create, Update, Delete) ---

    // Crear nueva etiqueta
    const crearEtiqueta = async (nombreEtiqueta) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.post(BASE_URL, nombreEtiqueta, {
                headers: { "Content-Type": "text/plain" },
                withCredentials: true
            });
            // NOTA: Si usas paginación, necesitarás llamar a cargarEtiquetasPaginadas 
            // en el componente que consume el hook para refrescar la lista.
            setEtiquetas((prev) => [...prev, response.data]);
            return response.data;
        } catch (err) {
            setError(err.response?.data?.message || "Error al crear etiqueta");
            throw err;
        } finally {
            setLoading(false);
        }
    };

    // Actualizar etiqueta
    const actualizarEtiqueta = async (id, nombreEtiqueta) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.put(`${BASE_URL}/${id}`, nombreEtiqueta, {
                headers: { "Content-Type": "text/plain" },
                withCredentials: true
            });
            // NOTA: Si usas paginación, la actualización local de setEtiquetasPaginadas 
            // sería ideal, pero la recarga completa es más simple si no se gestiona el estado local.
            setEtiquetas((prev) =>
                prev.map((et) => (et.idEtiqueta === id ? response.data : et))
            );
            return response.data;
        } catch (err) {
            setError(err.response?.data?.message || "Error al actualizar etiqueta");
            throw err;
        } finally {
            setLoading(false);
        }
    };

    // Eliminar etiqueta
    const eliminarEtiqueta = async (id) => {
        setLoading(true);
        setError(null);
        try {
            await axios.delete(`${BASE_URL}/${id}`, { withCredentials: true });
            // NOTA: Si usas paginación, necesitarás llamar a cargarEtiquetasPaginadas 
            // en el componente que consume el hook para refrescar la lista.
            setEtiquetas((prev) => prev.filter((et) => et.idEtiqueta !== id));
        } catch (err) {
            setError(err.response?.data?.message || "Error al eliminar etiqueta");
            throw err;
        } finally {
            setLoading(false);
        }
    };

    // Cargar todas las etiquetas (sin paginar) al montar.
    // Si la interfaz usa paginación, es mejor llamar a cargarEtiquetasPaginadas
    // en el componente que lo usa, para controlar los parámetros de la página.
    useEffect(() => {
        cargarEtiquetas();
    }, []);

    return {
        etiquetas, // Lista sin paginación (para selectores/combos)
        etiquetasPaginadas, // ✅ Lista de la página actual
        totalPages, // ✅ Total de páginas
        number, // ✅ Número de página actual
        totalElements, // ✅ Total de registros
        loading,
        error,
        cargarEtiquetas,
        cargarEtiquetasPaginadas, // ✅ Nuevo método
        crearEtiqueta,
        actualizarEtiqueta,
        eliminarEtiqueta
    };
}