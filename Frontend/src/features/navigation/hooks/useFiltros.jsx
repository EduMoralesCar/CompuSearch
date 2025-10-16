import { useState, useEffect } from "react";

export default function useFiltros() {
    const [filtroCategoria, setFiltroCategoria] = useState([]);
    const [filtroMarca, setFiltroMarca] = useState([]);
    const [rangoPrecio, setRangoPrecio] = useState(null);
    const [filtroTienda, setFiltroTienda] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const cargarFiltros = async () => {
            try {
                const [resCat, resPre, resMar, resTie] = await Promise.all([
                    fetch("http://localhost:8080/filtro/categorias"),
                    fetch("http://localhost:8080/filtro/precios"),
                    fetch("http://localhost:8080/filtro/marcas"),
                    fetch("http://localhost:8080/filtro/tiendas")
                ]);

                if (!resCat.ok || !resPre.ok || !resMar.ok || !resTie.ok) {
                    throw new Error("Error en una o más respuestas del servidor");
                }

                const [dataCat, dataPre, dataMar, dataTie] = await Promise.all([
                    resCat.json(),
                    resPre.json(),
                    resMar.json(),
                    resTie.json()
                ]);

                setFiltroCategoria(dataCat);
                setRangoPrecio(dataPre);
                setFiltroMarca(dataMar);
                setFiltroTienda(dataTie)
            } catch (err) {
                setError(err);
            } finally {
                setLoading(false);
            }
        };

        cargarFiltros();
    }, []);

    return { filtroCategoria, filtroMarca, rangoPrecio, filtroTienda, loading, error };
}
