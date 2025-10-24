import React, { useState, useEffect } from "react";
import { Card } from "react-bootstrap";
import { useAuth } from "../../../context/useAuth";
import { validateForgotPassword } from "../validations/validateForgotPassword";
import axios from "axios";
import ForgotPasswordForm from "../components/forgotPassword/ForgotPasswordForm";

const ForgotPassword = () => {
    const { forgotPassword } = useAuth();
    const [ip, setIp] = useState("desconocido");
    const [email, setEmail] = useState("");
    const [feedbackMessage, setFeedbackMessage] = useState("");
    const [feedbackType, setFeedbackType] = useState("danger");
    const [loading, setLoading] = useState(false);
    const [errors, setErrors] = useState({});

    useEffect(() => {
        axios.get("https://api.ipify.org?format=json")
            .then(res => setIp(res.data.ip))
            .catch(() => setIp("desconocido"));
    }, []);

    const validate = () => {
        const newErrors = validateForgotPassword({ email });
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!validate()) return;

        setLoading(true);
        setFeedbackMessage("");
        setFeedbackType("danger");

        const result = await forgotPassword({ email, ip });

        if (result.success) {
            setFeedbackMessage(result.message || "Correo enviado correctamente");
            setFeedbackType("success");
        } else {
            setFeedbackMessage(result.message);
            setFeedbackType("danger");
        }

        setLoading(false);
    };

    return (
        <main className="container d-flex justify-content-center align-items-center flex-grow-1">
            <div className="row w-100 my-4">
                <div className="col-md-6 offset-md-3">
                    <Card className="shadow-sm p-4">
                        <h3 className="fw-bold text-primary mb-4">RECUPERAR CONTRASEÑA</h3>
                        <p className="text-muted">
                            Ingresa tu correo electrónico y te enviaremos un enlace para restablecer tu contraseña.
                        </p>
                        <ForgotPasswordForm
                            email={email}
                            setEmail={setEmail}
                            errors={errors}
                            feedbackMessage={feedbackMessage}
                            feedbackType={feedbackType} 
                            loading={loading}
                            handleSubmit={handleSubmit}
                        />

                    </Card>
                </div>
            </div>
        </main>
    );
};

export default ForgotPassword;
