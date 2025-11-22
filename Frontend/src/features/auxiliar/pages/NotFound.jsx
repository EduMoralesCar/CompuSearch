import React from "react";
import StatusPage from "../components/StatusPage";

const NotFound = () => (
    <StatusPage
        code={404}
        title="Página no encontrada"
        message="La página que estás buscando no existe o fue movida."
        iconClass="bi bi-exclamation-triangle text-warning"
        buttonVariant="primary"
    />
);

export default NotFound;
