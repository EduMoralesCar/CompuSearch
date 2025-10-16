import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const SearchBar = () => {
    const [query, setQuery] = useState("");
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        if (query.trim() !== "") {
            navigate(`/componentes?search=${encodeURIComponent(query)}`);
        }
    };

    return (
        <form
            className="d-flex col-12 col-lg-6 mx-auto mt-3 mt-lg-0 order-lg-2"
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
                />
                <button className="btn btn-outline-light" type="submit">
                    <i className="bi bi-search fs-6 me-0"></i>
                </button>
            </div>
        </form>
    );
};

export default SearchBar;
