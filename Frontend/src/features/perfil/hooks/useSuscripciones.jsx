import { useState, useCallback } from "react";
import axios from "axios";

const API_URL = "http://localhost:8080/suscripciones";

export function useSuscripciones() {
    const [suscripcionActual, setSuscripcionActual] = useState(null);
    const [suscripciones, setSuscripciones] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    // Obtener suscripción actual por tienda
    const fetchSuscripcionActual = useCallback(async (idTienda) => {
        setLoading(true);
        setError(null);

        try {
            const res = await axios.get(`${API_URL}/actual/${idTienda}`, { withCredentials: true });
            setSuscripcionActual(res.data || null);
        } catch (err) {
            setError(err.response?.data || "Error al obtener suscripción.");
        } finally {
            setLoading(false);
        }
    }, []);

    const fetchSuscripcionesPaginadas = useCallback(async (idTienda, page = 0, size = 10) => {
        setLoading(true);
        setError(null);

        try {
            const res = await axios.get(`${API_URL}/${idTienda}`, {
                withCredentials: true,
                params: { page, size },
            });

            if (res.status === 204) {
                setSuscripciones({ content: [], totalElements: 0, number: 0, size });
            } else {
                setSuscripciones(res.data);
            }
        } catch (err) {
            setError(err.response?.data || "Error al obtener suscripciones.");
        } finally {
            setLoading(false);
        }
    }, []);


    // Crear suscripción
    const crearSuscripcion = async (idPlan, idTienda, aceptarPago = true) => {
        try {
            const res = await axios.post(`${API_URL}/crear`, null, {
                params: { idPlan, idTienda, aceptarPago }, withCredentials: true
            });
            return res.data;
        } catch (err) {
            throw err.response?.data || "Error al crear suscripción.";
        }
    };

    // Cancelar suscripción
    const cancelarSuscripcion = async (idTienda) => {
        try {
            const res = await axios.post(`${API_URL}/cancelar/${idTienda}`, null, {
                withCredentials: true
            });
            return res.data;
        } catch (err) {
            throw err.response?.data || "Error al cancelar suscripción.";
        }
    };


    // Cambiar suscripción a otro plan
    const cambiarSuscripcion = async (idPlanNuevo, idTienda, aceptarPago = true) => {
        try {
            const res = await axios.post(`${API_URL}/cambiar`, null, {
                params: { idPlanNuevo, idTienda, aceptarPago }, withCredentials: true,
            });
            return res.data;
        } catch (err) {
            throw err.response?.data || "Error al cambiar suscripción.";
        }
    };

    return {
        suscripcionActual,
        suscripciones,
        fetchSuscripcionesPaginadas,
        loading,
        error,
        fetchSuscripcionActual,
        crearSuscripcion,
        cancelarSuscripcion,
        cambiarSuscripcion,
    };
}
