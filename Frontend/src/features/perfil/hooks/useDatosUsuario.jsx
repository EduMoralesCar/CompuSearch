import { useState, useEffect } from "react";
import axios from "axios";

export const useDatosUsuario = (idUsuario) => {
    const [usuario, setUsuario] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (!idUsuario) return;

        const fetchUsuario = async () => {
            try {
                setLoading(true);
                const response = await axios.get(`http://localhost:8080/usuario/${idUsuario}`, {
                    withCredentials: true,
                });
                setUsuario(response.data);
            } catch (err) {
                setError(err.response?.data?.message || "Error al obtener el usuario");
            } finally {
                setLoading(false);
            }
        };

        fetchUsuario();
    }, [idUsuario]);

    return { usuario, loading, error };
};
