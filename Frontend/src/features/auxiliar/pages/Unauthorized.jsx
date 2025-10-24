import React from "react";
import StatusPage from "../components/StatusPage";

const Unauthorized = () => (
    <StatusPage
        code={401}
        title="Acceso no autorizado"
        message="No tienes permisos para acceder a esta sección."
        iconClass="bi bi-lock-fill text-secondary"
        buttonVariant="outline-primary"
    />
);

export default Unauthorized;
