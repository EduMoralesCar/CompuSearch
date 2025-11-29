import { useState, useCallback } from "react";
import axios from "axios";

const API_URL = "http://localhost:8080/pagos/historial";

export const usePagos = () => {
    const [pagos, setPagos] = useState([]);
    const [totalElements, setTotalElements] = useState(0);
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(10);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetchHistorialPagos = useCallback(async (idTienda, pageParam = page, sizeParam = size) => {
        setLoading(true);
        setError(null);

        try {
            const res = await axios.get(`${API_URL}/${idTienda}`, {
                withCredentials: true,
                params: { page: pageParam, size: sizeParam }
            });

            if (res.status === 204 || !res.data.content) {
                setPagos([]);
                setTotalElements(0);
            } else {
                setPagos(res.data.content);
                setTotalElements(res.data.totalElements);
                setPage(res.data.number);
                setSize(res.data.size);
            }
        } catch (err) {
            setError(err.response?.data || "Error al obtener historial de pagos.");
        } finally {
            setLoading(false);
        }
    }, [page, size]);

    return {
        pagos,
        totalElements,
        page,
        size,
        loading,
        error,
        setPage,
        setSize,
        fetchHistorialPagos,
    };
};
