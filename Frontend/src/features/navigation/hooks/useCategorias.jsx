import { useState, useEffect } from "react";
import axios from "axios";

export default function useCategorias() {
    const [categorias, setCategorias] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const cargarCategorias = async () => {
            setLoading(true);
            setError(null);

            try {
                const response = await axios.get("http://localhost:8080/categorias");
                setCategorias(response.data || []);
            } catch (err) {
                setError(err.message || "Error al cargar las categorías");
            } finally {
                setLoading(false);
            }
        };

        cargarCategorias();
    }, []);

    return { categorias, loading, error };
}
