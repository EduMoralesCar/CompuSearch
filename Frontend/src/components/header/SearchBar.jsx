import React, { useState, useEffect, useRef } from "react";
import { useNavigate, Link } from "react-router-dom";
import axios from "axios";

const SearchBar = () => {
    const [query, setQuery] = useState("");
    const [suggestions, setSuggestions] = useState([]);
    const [totalResults, setTotalResults] = useState(0);
    const [loading, setLoading] = useState(false);
    // Estado para controlar la visibilidad del dropdown
    const [isDropdownVisible, setIsDropdownVisible] = useState(false);

    const navigate = useNavigate();
    const searchRef = useRef(null);
    const timerRef = useRef(null);

    // LÓGICA DE BÚSQUEDA "DEBOUNCED"
    useEffect(() => {
        if (timerRef.current) {
            clearTimeout(timerRef.current);
        }

        if (query.trim().length < 2) {
            setSuggestions([]);
            setTotalResults(0);
            setLoading(false);
            setIsDropdownVisible(false);
            return;
        }

        setLoading(true);
        setIsDropdownVisible(true); // Mostrar dropdown al buscar

        timerRef.current = setTimeout(async () => {
            try {
                const res = await axios.get(
                    `http://localhost:8080/componentes/buscar`,
                    {
                        params: {
                            nombre: query,
                            page: 0,
                            size: 5
                        }
                    }
                );

                setSuggestions(res.data.content || []);
                setTotalResults(res.data.totalElements || 0);

            // eslint-disable-next-line no-unused-vars
            } catch (err) {
                setSuggestions([]);
                setTotalResults(0);
            } finally {
                setLoading(false);
            }
        }, 300);

        return () => {
            if (timerRef.current) {
                clearTimeout(timerRef.current);
            }
        };

    }, [query]);

    // CERRAR DROPDOWN AL HACER CLIC FUERA
    useEffect(() => {
        const handleClickOutside = (event) => {
            if (searchRef.current && !searchRef.current.contains(event.target)) {
                // Simplemente oculta el dropdown
                setIsDropdownVisible(false);
            }
        };

        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, []);

    // MANEJADOR DEL SUBMIT (Enter o botón 'Ver más')
    const handleSubmit = (e) => {
        e.preventDefault();
        if (query.trim() !== "") {
            navigate(`/componentes?search=${encodeURIComponent(query)}`);
            setQuery("");
            setSuggestions([]);
            setTotalResults(0);
            setIsDropdownVisible(false);
        }
    };

    // Función para limpiar al hacer clic en un item
    const handleSuggestionClick = () => {
        setQuery("");
        setSuggestions([]);
        setTotalResults(0);
        setIsDropdownVisible(false);
    };

    // Mostrar dropdown si se hace foco en el input
    const handleInputFocus = () => {
        if (query.trim().length > 1) {
            setIsDropdownVisible(true);
        }
    };

    return (
        <div
            className="d-flex col-12 col-lg-6 mx-auto mt-3 mt-lg-0 order-lg-2"
            ref={searchRef}
            style={{ position: 'relative' }}
        >
            <form
                className="w-100"
                role="search"
                onSubmit={handleSubmit}
            >
                <div className="input-group">
                    <input
                        className="form-control"
                        type="search"
                        placeholder="¿Qué estás buscando?"
                        aria-label="Buscar"
                        value={query}
                        onChange={(e) => setQuery(e.target.value)}
                        onFocus={handleInputFocus}
                        autoComplete="off"
                    />
                    <button className="btn btn-outline-light" type="submit">
                        <i className="bi bi-search fs-6 me-0"></i>
                    </button>
                </div>
            </form>

            {isDropdownVisible && query.length > 1 && (
                <div
                    className="list-group"
                    style={{
                        position: 'absolute',
                        top: '100%',
                        left: 0,
                        right: 0,
                        zIndex: 1000,
                        maxHeight: '400px',
                        overflowY: 'auto',
                    }}
                >
                    {loading && (
                        <span className="list-group-item list-group-item-action text-muted">
                            Buscando...
                        </span>
                    )}

                    {!loading && suggestions.length > 0 && (
                        <>
                            {suggestions.map((item) => (
                                <Link
                                    key={item.idProductoTienda}
                                    to={`/producto/${encodeURIComponent(item.nombreProducto)}`}
                                    className="list-group-item list-group-item-action d-flex align-items-center"
                                    onClick={handleSuggestionClick}
                                >
                                    <img
                                        src={item.urlImagen || 'https://via.placeholder.com/50'}
                                        alt={item.nombreProducto}
                                        style={{ width: '50px', height: '50px', objectFit: 'contain', marginRight: '15px' }}
                                    />
                                    <div style={{ flex: 1, minWidth: 0 }}>
                                        <p className="mb-0 text-dark text-truncate">{item.nombreProducto}</p>
                                        <strong className="text-primary">S/ {item.precio.toFixed(2)}</strong>
                                    </div>
                                </Link>
                            ))}

                            {totalResults > 5 && (
                                <button
                                    type="submit"
                                    onClick={handleSubmit}
                                    className="list-group-item list-group-item-action text-center text-primary fw-bold"
                                >
                                    Ver más ({totalResults - 5} resultados)
                                </button>
                            )}
                        </>
                    )}

                    {!loading && suggestions.length === 0 && (
                        <span className="list-group-item list-group-item-action text-muted">
                            No se encontraron resultados para "{query}"
                        </span>
                    )}
                </div>
            )}
        </div>
    );
};

export default SearchBar;