import { useState } from "react";
import axios from "axios";

const BASE_URL = "http://localhost:8080/builds";

export default function useBuilds() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [respuesta, setRespuesta] = useState(null);

  const crearBuild = async ({ nombre, compatible, costoTotal, idUsuario }) => {
    setLoading(true);
    setError(null);
    setRespuesta(null);

    try {
      const res = await axios.post(
        `${BASE_URL}`,
        { nombre, compatible, costoTotal, idUsuario },
        { withCredentials: true }
      );
      setRespuesta(res.data);
      return { success: true, data: res.data };
    } catch (err) {
      setError(err.response?.data?.message || "Error al crear la build");
      return { success: false, error: err };
    } finally {
      setLoading(false);
    }
  };

  const obtenerBuildPorId = async (idBuild) => {
    setLoading(true);
    setError(null);
    setRespuesta(null);

    try {
      const res = await axios.get(`${BASE_URL}/${idBuild}`, { withCredentials: true });
      setRespuesta(res.data);
      return { success: true, data: res.data };
    } catch (err) {
      setError(err.response?.data?.message || "Error al obtener la build");
      return { success: false, error: err };
    } finally {
      setLoading(false);
    }
  };

  const obtenerBuildsPorUsuario = async (idUsuario) => {
    setLoading(true);
    setError(null);
    setRespuesta(null);

    try {
      const res = await axios.get(`${BASE_URL}/usuario/${idUsuario}`, { withCredentials: true });
      setRespuesta(res.data);
      return { success: true, data: res.data };
    } catch (err) {
      setError(err.response?.data?.message || "Error al obtener las builds");
      return { success: false, error: err };
    } finally {
      setLoading(false);
    }
  };

  const eliminarBuild = async (idBuild) => {
    setLoading(true);
    setError(null);
    setRespuesta(null);

    try {
      await axios.delete(`${BASE_URL}/${idBuild}`, { withCredentials: true });
      setRespuesta(`Build ${idBuild} eliminada correctamente`);
      return { success: true };
    } catch (err) {
      setError(err.response?.data?.message || "Error al eliminar la build");
      return { success: false, error: err };
    } finally {
      setLoading(false);
    }
  };

  return {
    crearBuild,
    obtenerBuildPorId,
    obtenerBuildsPorUsuario,
    eliminarBuild,
    loading,
    error,
    respuesta
  };
}
