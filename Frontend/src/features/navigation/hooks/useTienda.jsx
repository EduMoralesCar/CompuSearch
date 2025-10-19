import { useState, useEffect } from "react";

export default function useTiendas() {
    const [tiendas, setTiendas] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetch("http://localhost:8080/tiendas/verificadas")
            .then((res) => {
                if (!res.ok) throw new Error("Error al cargar tiendas");
                return res.json();
            })
            .then((data) => {
                setTiendas(data);
                setLoading(false);
            })
            .catch((err) => {
                setError(err.message);
                setLoading(false);
            });
    }, []);

    return { tiendas, loading, error };
}
