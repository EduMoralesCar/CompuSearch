import { useEffect, useState } from "react";
import axios from "axios";

export default function useEtiquetas() {
    const [etiquetas, setEtiquetas] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const cargarEtiquetas = async () => {
            setLoading(true);
            setError(null);

            try {
                const response = await axios.get("http://localhost:8080/etiquetas");
                setEtiquetas(response.data || []);
            } catch (err) {
                setError(err.message || "Error al cargar etiquetas");
            } finally {
                setLoading(false);
            }
        };

        cargarEtiquetas();
    }, []);

    return { etiquetas, loading, error };
}
