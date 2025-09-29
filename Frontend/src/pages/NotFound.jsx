import React from 'react';
import { Link } from 'react-router-dom';

const NotFound = () => {
    return (
        <div className="text-center my-5">
            <h1>404</h1>
            <h2>¡Ups! Página no encontrada.</h2>
            <p>La página que buscas no existe.</p>
            <Link to="/" className="btn btn-primary mt-3">Volver a la página de inicio</Link>
        </div>
    );
};

export default NotFound;