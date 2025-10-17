import { useState, useEffect } from "react";
import { categoriasFiltros } from "../utils/categoriasFiltros";

export default function useFiltrosAdicionales(categoriaSeleccionada) {
    const [valoresAtributos, setValoresAtributos] = useState({});
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        const cargarValores = async () => {
            setLoading(true);
            setError(null);

            const atributos = categoriasFiltros[categoriaSeleccionada] || [];

            if (atributos.length === 0) {
                setValoresAtributos({});
                setLoading(false);
                return;
            }

            try {
                const respuestas = await Promise.all(
                    atributos.map((nombreAtributo) =>
                        fetch(`http://localhost:8080/filtro/valores?nombreAtributo=${encodeURIComponent(nombreAtributo)}`)
                    )
                );

                const datos = await Promise.all(
                    respuestas.map(async (res) => {
                        if (!res.ok) return [];
                        return await res.json();
                    })
                );

                const resultado = {};
                atributos.forEach((nombreAtributo, index) => {
                    resultado[nombreAtributo] = datos[index];
                });

                setValoresAtributos(resultado);
            } catch (err) {
                setError(err);
            } finally {
                setLoading(false);
            }
        };

        if (categoriaSeleccionada && categoriaSeleccionada !== "Todas") {
            cargarValores();
        } else {
            setValoresAtributos({});
        }
    }, [categoriaSeleccionada]);

    return { valoresAtributos, loading, error };
}
