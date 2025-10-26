import { useState } from "react";
import axios from "axios";

export function useUsuario() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const actualizarInfo = async (idUsuario, infoData) => {
        setLoading(true);
        setError(null);

        try {
            const response = await axios.put(
                `http://localhost:8080/usuario/${idUsuario}`,
                infoData,
                { withCredentials: true }
            );
            return { success: true, data: response.data };
        } catch (err) {
            setError(err.response?.data?.message || "Error al actualizar información");
            return { success: false, error: err.response?.data?.message || "Error al actualizar información" };
        } finally {
            setLoading(false);
        }
    };

    const actualizarPassword = async (idUsuario, passwordData) => {
        setLoading(true);
        setError(null);

        try {
            const response = await axios.put(
                `http://localhost:8080/usuario/password/${idUsuario}`,
                passwordData,
                { withCredentials: true }
            );
            return { success: true, data:response.data};
        } catch (err) {
            setError(err.response?.data?.message || "Error al actualizar la contraseña");
            return { success: false, error: err.response?.data?.message || "Error al actualizar la contraseña" };
        } finally {
            setLoading(false);
        }
    }

    return {
        actualizarInfo,
        actualizarPassword,
        loading,
        error
    };
}
