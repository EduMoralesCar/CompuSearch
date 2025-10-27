import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";

export const useDatosProductos = () => {
    const { slug } = useParams();
    const [productoInfo, setProductoInfo] = useState(null);
    const [tiendas, setTiendas] = useState([]);
    const [imagenPrincipal, setImagenPrincipal] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const nombreProducto = decodeURIComponent(slug);
        if (!nombreProducto) {
            setError("Nombre de producto no válido.");
            setLoading(false);
            return;
        }

        const fetchDatos = async () => {
            setLoading(true);
            setError(null);

            try {
                // Obtener todas las tiendas que venden el producto
                const tiendasRes = await axios.get(
                    `http://localhost:8080/componentes/tiendas?nombreProducto=${encodeURIComponent(nombreProducto)}`
                );

                const tiendasData = tiendasRes.data;
                if (!Array.isArray(tiendasData) || tiendasData.length === 0) {
                    throw new Error("No se encontraron tiendas para este producto.");
                }

                // Determinar la tienda más barata
                const tiendaMasBarata = [...tiendasData].sort((a, b) => a.precio - b.precio)[0];
                const nombreTiendaMasBarata = tiendaMasBarata.nombreTienda;     

                // Obtener la información del producto de esa tienda
                const productoRes = await axios.get(
                    `http://localhost:8080/componentes/info?nombreProducto=${encodeURIComponent(nombreProducto)}&nombreTienda=${encodeURIComponent(nombreTiendaMasBarata)}`
                );

                const productoData = productoRes.data;
                if (!productoData) throw new Error("No se encontró información del producto.");

                // Quitar la tienda principal (la más barata) de la lista de tiendas adicionales
                const otrasTiendas = tiendasData.filter(
                    tienda => tienda.nombreTienda !== nombreTiendaMasBarata
                );

                // Actualizar estados
                setProductoInfo(productoData);
                setImagenPrincipal(productoData.urlImagen);
                setTiendas(otrasTiendas);

            } catch (err) {
                setError(err.message || "Error desconocido.");
            } finally {
                setLoading(false);
            }
        };

        fetchDatos();
    }, [slug]);

    return { productoInfo, tiendas, imagenPrincipal, loading, error };
};
