import React from "react";

const PerfilHeader = ({ username, email }) => {
    return (
    <div className="text-center">
        <div 
            className="mx-auto mb-3 bg-primary rounded-circle d-flex align-items-center justify-content-center text-white"
            style={{ width: "100px", height: "100px", fontSize: "2.5rem" }}
        >
            {username.charAt(0)}
        </div>
        <h5 className="card-title mb-1">{username}</h5>
        <p className="card-text text-muted">{email}</p>
    </div>
    );
};

export default PerfilHeader;