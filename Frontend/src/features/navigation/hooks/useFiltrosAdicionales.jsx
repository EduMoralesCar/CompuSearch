import { useState, useEffect } from "react";
import axios from "axios";
import { categoriasFiltrosMap } from "../utils/categoriasFiltrosMap";

export default function useFiltrosAdicionales(categoriaSeleccionada) {
    const [valoresAtributos, setValoresAtributos] = useState({});
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        const cargarValores = async () => {
            setLoading(true);
            setError(null);

            const atributos = categoriasFiltrosMap[categoriaSeleccionada] || [];

            if (atributos.length === 0) {
                setValoresAtributos({});
                setLoading(false);
                return;
            }

            try {
                const respuestas = await Promise.all(
                    atributos.map((nombreAtributo) =>
                        axios.get(`http://localhost:8080/filtro/valores`, {
                            params: { nombreAtributo },
                        })
                    )
                );

                const resultado = {};

                atributos.forEach((nombreAtributo, index) => {
                    let data = respuestas[index].data || [];

                    const valoresSeparados = [];
                    const seen = new Set(); // Para evitar duplicados de label

                    data.forEach((valorCompleto) => {
                        valorCompleto.split(",").forEach((v) => {
                            const trimmed = v.trim();
                            if (!seen.has(trimmed)) {
                                valoresSeparados.push({ label: trimmed, value: valorCompleto });
                                seen.add(trimmed);
                            }
                        });
                    });

                    resultado[nombreAtributo] = valoresSeparados;
                });

                setValoresAtributos(resultado);
            } catch (err) {
                setError(err.message || "Error al cargar filtros adicionales");
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
