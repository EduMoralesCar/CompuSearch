import { useState, useEffect } from "react";
import axios from "axios";

export default function useTiendas() {
    const [tiendas, setTiendas] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const obtenerTiendas = async () => {
            try {
                const response = await axios.get("http://localhost:8080/tiendas/verificadas");
                setTiendas(response.data);
            } catch (err) {
                setError(err.response?.data?.message || err.message);
            } finally {
                setLoading(false);
            }
        };

        obtenerTiendas();
    }, []);

    return { tiendas, loading, error };
}
