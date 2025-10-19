import { useState, useEffect } from "react";
import axios from "axios";

export default function useProductosTiendas({
    categoria,
    nombreTienda,
    precioMax,
    precioMin,
    disponible,
    marca,
    nombreProducto,
    page = 0,
    size = 15,
}) {
    const [productos, setProductos] = useState([]);
    const [totalPages, setTotalPages] = useState(0);
    const [totalElements, setTotalElements] = useState(0);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchProductos = async () => {
            setLoading(true);
            setError(null);

            try {
                const params = new URLSearchParams();

                // Filtros generales
                if (nombreTienda && nombreTienda !== "Todas") params.append("nombreTienda", nombreTienda);
                if (marca && marca !== "Todas") params.append("marca", marca);
                if (precioMax) params.append("precioMax", precioMax);
                if (precioMin) params.append("precioMin", precioMin);

                if (disponible === "Disponible") params.append("disponible", true);
                else if (disponible === "No disponible") params.append("disponible", false);

                // Paginación
                params.append("page", page);
                params.append("size", size);

                const baseUrl = "http://localhost:8080/componentes";
                let endpoint = "/filtrar"; // Por defecto

                // Si hay búsqueda por nombre
                if (nombreProducto?.trim()) {
                    params.append("nombre", nombreProducto);
                    endpoint = "/buscar";
                }
                // Si hay categoría específica
                else if (categoria && categoria !== "Todas") {
                    params.append("categoria", categoria);
                }

                const url = `${baseUrl}${endpoint}?${params.toString()}`;

                const { data } = await axios.get(url);

                setProductos(data.content || []);
                setTotalPages(data.totalPages || 0);
                setTotalElements(data.totalElements || 0);
            } catch (err) {
                setError(err.message || "Error al cargar productos");
            } finally {
                setLoading(false);
            }
        };

        fetchProductos();
    }, [categoria, nombreTienda, precioMax, precioMin, disponible, marca, nombreProducto, page, size]);

    return { productos, totalPages, totalElements, loading, error };
}
