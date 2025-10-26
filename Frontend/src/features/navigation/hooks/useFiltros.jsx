import { useState, useEffect } from "react";
import axios from "axios";

export default function useFiltros(categoriaSeleccionada) {
    const [filtroCategoria, setFiltroCategoria] = useState([]);
    const [filtroMarca, setFiltroMarca] = useState([]);
    const [rangoPrecio, setRangoPrecio] = useState(null);
    const [filtroTienda, setFiltroTienda] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const cargarFiltros = async () => {
            setLoading(true);
            setError(null);

            const params = categoriaSeleccionada && categoriaSeleccionada !== "Todas"
                ? { categoria: categoriaSeleccionada }
                : {};

            try {
                const [resCat, resPre, resMar, resTie] = await Promise.all([
                    axios.get("http://localhost:8080/filtro/categorias"),
                    axios.get("http://localhost:8080/filtro/precios", { params }),
                    axios.get("http://localhost:8080/filtro/marcas", { params }),
                    axios.get("http://localhost:8080/filtro/tiendas", { params })
                ]);

                setFiltroCategoria(resCat.data || []);
                setRangoPrecio(resPre.data || null);
                setFiltroMarca(resMar.data || []);
                setFiltroTienda(resTie.data || []);

            } catch (err) {
                setError(err.message || "Error al cargar filtros");
            } finally {
                setLoading(false);
            }
        };

        cargarFiltros();
    }, [categoriaSeleccionada]);

    return { filtroCategoria, filtroMarca, rangoPrecio, filtroTienda, loading, error };
}
