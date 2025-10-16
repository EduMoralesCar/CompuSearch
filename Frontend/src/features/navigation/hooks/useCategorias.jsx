import { useState, useEffect } from "react";

export default function useCategorias() {
    const [categorias, setCategorias] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetch("http://localhost:8080/categorias")
            .then((res) => {
                if (!res.ok) throw new Error("Error al cargar las categorias");
                return res.json();
            })
            .then((data) => {
                setCategorias(data);
                setLoading(false);
            })
            .catch((err) => {
                setError(err.message);
                setLoading(false);
            });
    }, []);

    return { categorias, loading, error };
}
