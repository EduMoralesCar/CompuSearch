import { useState } from "react";
import axios from "axios";

export function useSolicitudes() {
    const [respuesta, setRespuesta] = useState([]);
    const [totalPages, setTotalPages] = useState(0);
    const [number, setNumber] = useState(0); // <-- Página actual (índice 0)
    const [totalElements, setTotalElements] = useState(0); // <-- Total de registros en la base de datos
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const obtenerSolicitudes = async (idUsuario, page = 0, size = 5) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.get(
                `http://localhost:8080/solicitud/${idUsuario}`,
                {
                    params: { page, size },
                    withCredentials: true
                }
            );
            const data = response.data;
            setRespuesta(data.content || []);
            setTotalPages(data.totalPages || 0);
            setNumber(data.number || 0); // <-- Guardar el número de página
            setTotalElements(data.totalElements || 0); // <-- Guardar el total de elementos
        } catch (err) {
            setError(err.response?.data?.message || "Error al obtener las solicitudes del usuario");
        } finally {
            setLoading(false);
        }
    };

    const obtenerTodasSolicitudes = async (page = 0, size = 10) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.get(`http://localhost:8080/solicitud`, {
                params: { page, size },
                withCredentials: true
            });
            const data = response.data;
            setRespuesta(data.content || []);
            setTotalPages(data.totalPages || 0);
            setNumber(data.number || 0); // <-- Guardar el número de página
            setTotalElements(data.totalElements || 0); // <-- Guardar el total de elementos
        } catch (err) {
            setError(err.response?.data?.message || "Error al obtener todas las solicitudes");
        } finally {
            setLoading(false);
        }
    };

    const actualizarEstadoSolicitud = async (idSolicitud, nuevoEstado, idEmpleado) => {
        try {
            // El loading no se gestiona aquí para permitir la actualización de la UI en el componente
            // que llama a este método (usando el loading de `GestionSolicitudes`).
            
            // Asumo que tu backend en Java espera 'nuevoEstado' y 'idEmpleado' como parámetros de query
            await axios.put(
                `http://localhost:8080/solicitud/${idSolicitud}/estado`,
                null,
                {
                    params: { nuevoEstado, idEmpleado },
                    withCredentials: true
                }
            );

            // Actualiza inmediatamente el estado de la solicitud en la UI localmente
            setRespuesta((prev) =>
                prev.map((solicitud) =>
                    solicitud.idSolicitudTienda === idSolicitud // Usar idSolicitudTienda si es el correcto
                        ? { ...solicitud, estado: nuevoEstado }
                        : solicitud
                )
            );

            // NOTA: Es importante que el componente que llama a esta función (GestionSolicitudes) 
            // llame luego a obtenerTodasSolicitudes(currentPage, size) para asegurar 
            // que la lista completa se refresque correctamente si la solicitud se mueve de página,
            // tal como ya lo tienes configurado en el componente.

        } catch (err) {
            setError(err.response?.data?.message || "Error al actualizar el estado de la solicitud");
            // Lanzar el error para que el componente que llama lo capture y muestre el mensaje de feedback
            throw err; 
        }
    };

    return {
        respuesta,
        totalPages,
        number, // <-- Exportar el número de página actual (índice 0)
        totalElements, // <-- Exportar el total de elementos
        loading,
        error,
        obtenerSolicitudes,
        obtenerTodasSolicitudes,
        actualizarEstadoSolicitud
    };
}