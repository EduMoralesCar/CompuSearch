import { useState } from "react";
import axios from "axios";

const BASE_URL = "http://localhost:8080/builds";

export default function useBuilds() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [respuesta, setRespuesta] = useState(null);

  const obtenerProductosBuilds = async (categoria, page = 0, size = 8) => {
    setLoading(true);
    setError(null);
    setRespuesta(null);

    try {
      const res = await axios.get(`${BASE_URL}/productos`, {
        params: {
          categoria: categoria,
          page: page,
          size: size
        }, withCredentials: true
      });

      setRespuesta(res.data);
      return { success: true, data: res.data };
    } catch (err) {
      setError(err.response?.data || "Error al obtener productos");
    } finally {
      setLoading(false);
    }
  };

  const crearBuild = async (BuiLdData) => {

    setLoading(true);
    setError(null);
    setRespuesta(null);

    try {
      const res = await axios.post(
        `${BASE_URL}`,
        BuiLdData,
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

  const actualizarBuild = async (idBuild, buildData) => {
    setLoading(true);
    setError(null);
    setRespuesta(null);

    try {
      const res = await axios.put(`${BASE_URL}/${idBuild}`, buildData, { withCredentials: true });
      setRespuesta(res.data);
      return { success: true, data: res.data };
    } catch (err) {
      setError(err.response?.data?.message || "Error al actualizar la build");
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

  const obtenerBuildsPorUsuario = async (idUsuario, page = 0, size = 5) => {
    setLoading(true);
    setError(null);
    setRespuesta(null);

    try {
      const res = await axios.get(`${BASE_URL}/usuario/${idUsuario}`, {
        params: { page, size },
        withCredentials: true
      });

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

  const exportarBuild = async (idBuild) => {
    setLoading(true);
    setError(null);

    try {
      const res = await axios.get(`${BASE_URL}/export/${idBuild}`, {
        responseType: "blob",
        withCredentials: true,
      });

      // Crear un enlace para descargar el archivo
      const url = window.URL.createObjectURL(new Blob([res.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", `build_${idBuild}.xlsx`);
      document.body.appendChild(link);
      link.click();
      link.remove();

      return { success: true };
    } catch (err) {
      setError(err.response?.data?.message || "Error al exportar la build");
      return { success: false, error: err };
    } finally {
      setLoading(false);
    }
  };


  return {
    obtenerProductosBuilds,
    crearBuild,
    actualizarBuild,
    obtenerBuildPorId,
    obtenerBuildsPorUsuario,
    eliminarBuild,
    exportarBuild,
    loading,
    error,
    respuesta
  };
}
