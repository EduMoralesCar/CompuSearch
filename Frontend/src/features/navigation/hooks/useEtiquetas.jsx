import { useEffect, useState } from "react";

export default function useEtiquetas() {
    const [etiquetas, setEtiquetas] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const cargarEtiquetas = async () => {
            try {
                const response = await fetch("http://localhost:8080/etiquetas");
                if (!response.ok) {
                    throw new Error(`Error ${response.status}: ${response.statusText}`);
                }
                const data = await response.json();
                setEtiquetas(data);
            } catch (err) {
                setError(err);
            } finally {
                setLoading(false);
            }
        };

        cargarEtiquetas();
    }, []);

    return { etiquetas, loading, error };
}
