import React from "react";
import { Card } from "react-bootstrap";
import LoginForm from "./LoginForm";

const LoginCard = (props) => (
    <Card className="shadow-sm p-4">
        <h3 className="text-center text-primary fw-bold mb-4">INICIAR SESIÓN</h3>
        <LoginForm {...props} />
    </Card>
);

export default LoginCard;
