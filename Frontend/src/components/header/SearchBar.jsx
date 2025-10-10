import React from "react";

const SearchBar = () => (
    <form className="d-flex col-12 col-lg-6 mx-auto mt-3 mt-lg-0 order-lg-2" role="search">
        <div className="input-group">
            <input className="form-control" type="search" placeholder="¿Qué estás buscando?" aria-label="Buscar" />
            <button className="btn btn-outline-light" type="submit">
                <i className="bi bi-search fs-6 me-0"></i>
            </button>
        </div>
    </form>
);

export default SearchBar;
