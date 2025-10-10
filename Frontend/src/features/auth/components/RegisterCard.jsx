import React from "react";
import { Card } from "react-bootstrap";
import { Link } from "react-router-dom";

const RegisterCard = () => (
    <Card className="shadow-sm p-4 d-flex justify-content-center align-items-center">
        <h3 className="fw-bold text-primary mb-3">REGISTRO</h3>
        <p className="text-muted text-center">
            ¿Aún no tienes cuenta? Regístrate gratis y accede a todos nuestros beneficios.
        </p>
        <Link to="/registro" className="btn btn-outline-primary w-50">Registrarse</Link>
    </Card>
);

export default RegisterCard;
