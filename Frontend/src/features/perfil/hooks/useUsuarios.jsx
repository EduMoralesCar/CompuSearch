import { useState } from "react";
import axios from "axios";

export const useUsuarios = () => {
    const [usuarios, setUsuarios] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [totalPages, setTotalPages] = useState(0);
    const [totalElements, setTotalElements] = useState(0);

    const obtenerUsuariosPorTipo = async (tipo, page = 0, size = 10) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.get(
                `http://localhost:8080/usuario/tipo/${tipo}`,
                {
                    params: { page, size },
                    withCredentials: true,
                }
            );
            setUsuarios(response.data.content || []);
            setTotalPages(response.data.totalPages || 0);
            setTotalElements(response.data.totalElements || 0);
            return response.data;
        } catch (err) {
            const errorMsg = err.response?.data?.message || "Error al obtener usuarios";
            setError(errorMsg);
            throw err;
        } finally {
            setLoading(false);
        }
    };

    const cambiarEstadoUsuario = async (id, habilitado) => {
        try {
            await axios.put(
                `http://localhost:8080/usuario/${id}/estado`,
                { habilitado },
                { withCredentials: true }
            );
            return true;
        } catch (err) {
            const errorMsg = err.response?.data?.message || "Error al cambiar estado";
            setError(errorMsg);
            throw err;
        }
    };

    const actualizarUsuario = async (id, cambios) => {
        try {
            await axios.put(
                `http://localhost:8080/usuario/${id}`,
                cambios,
                { withCredentials: true }
            );
            return true;
        } catch (err) {
            const errorMsg = err.response?.data?.message || "Error al actualizar usuario";
            setError(errorMsg);
            throw err;
        }
    };

    const eliminarUsuario = async (id) => {
        try {
            await axios.delete(
                `http://localhost:8080/usuario/${id}`,
                { withCredentials: true }
            );
            return true;
        } catch (err) {
            const errorMsg = err.response?.data?.message || "Error al eliminar usuario";
            setError(errorMsg);
            throw err;
        }
    };

    return {
        usuarios,
        loading,
        error,
        totalPages,
        totalElements,
        obtenerUsuariosPorTipo,
        cambiarEstadoUsuario,
        actualizarUsuario,
        eliminarUsuario,
    };
};
