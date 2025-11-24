import { useState, useEffect } from "react";
import axios from "axios";

const BASE_URL = "http://localhost:8080/etiquetas";

export default function useEtiquetas() {
    const [etiquetas, setEtiquetas] = useState([]);
    
    const [etiquetasPaginadas, setEtiquetasPaginadas] = useState([]);
    const [totalPages, setTotalPages] = useState(0); 
    const [number, setNumber] = useState(0);
    const [totalElements, setTotalElements] = useState(0);
    
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const cargarEtiquetasPaginadas = async (page = 0, size = 10, sort = 'idEtiqueta,asc') => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.get(BASE_URL, { 
                params: { page, size, sort }, 
                withCredentials: true 
            });
            const data = response.data;
            
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


    // Crear nueva etiqueta
    const crearEtiqueta = async (nombreEtiqueta) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.post(BASE_URL, nombreEtiqueta, {
                headers: { "Content-Type": "text/plain" },
                withCredentials: true
            });

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
            setEtiquetas((prev) => prev.filter((et) => et.idEtiqueta !== id));
        } catch (err) {
            setError(err.response?.data?.message || "Error al eliminar etiqueta");
            throw err;
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        cargarEtiquetas();
    }, []);

    return {
        etiquetas,
        etiquetasPaginadas,
        totalPages,
        number,
        totalElements,
        loading,
        error,
        cargarEtiquetas,
        cargarEtiquetasPaginadas,
        crearEtiqueta,
        actualizarEtiqueta,
        eliminarEtiqueta
    };
}