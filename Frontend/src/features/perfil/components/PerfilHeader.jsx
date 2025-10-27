import React from "react";

const PerfilHeader = ({ nombre, email }) => (
    <div className="text-center">
        <div 
            className="mx-auto mb-3 bg-primary rounded-circle d-flex align-items-center justify-content-center text-white"
            style={{ width: "100px", height: "100px", fontSize: "2.5rem" }}
        >
            {nombre.charAt(0)}
        </div>
        <h5 className="card-title mb-1">{nombre}</h5>
        <p className="card-text text-muted">{email}</p>
    </div>
);

export default PerfilHeader;