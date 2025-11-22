import React, { useState, useEffect } from "react";
import RegistroForm from "../components/registro/RegistroForm";
import { validateRegistro } from "../validations/validateRegistro";
import axios from "axios";
import { useAuth } from "../../../context/useAuth";

const Registro = () => {
    const { registro } = useAuth();
    const [ip, setIp] = useState("desconocido");
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [showPassword, setShowPassword] = useState(false);
    const [errors, setErrors] = useState({});
    const [feedbackMessage, setFeedbackMessage] = useState("");
    const [feedbackType, setFeedbackType] = useState("danger");
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        axios.get("https://api.ipify.org?format=json")
            .then(res => setIp(res.data.ip))
            .catch(() => setIp("desconocido"));
    }, []);

    const togglePassword = () => setShowPassword(!showPassword);

    const validate = () => {
        const newErrors = validateRegistro({ username, email, password });
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!validate()) return;

        setLoading(true);
        const result = await registro({ username, email, password, ip });

        if (result.success) {
            setFeedbackMessage("¡Cuenta creada con éxito! Redirigiendo...");
            setFeedbackType("success");
        } else {
            setFeedbackMessage(result.message);
            setFeedbackType("danger");
        }
        setLoading(false);
    };


    return (
        <div className="container my-4 d-flex justify-content-center">
            <div className="card shadow p-4" style={{ maxWidth: "500px", width: "100%" }}>
                <h3 className="text-center text-primary fw-bold mb-4">REGISTRARSE</h3>
                <RegistroForm
                    username={username}
                    setUsername={setUsername}
                    email={email}
                    setEmail={setEmail}
                    password={password}
                    setPassword={setPassword}
                    showPassword={showPassword}
                    togglePassword={togglePassword}
                    errors={errors}
                    feedbackMessage={feedbackMessage}
                    feedbackType={feedbackType}
                    handleSubmit={handleSubmit}
                    loading={loading}
                />

            </div>
        </div>
    );
};

export default Registro;
