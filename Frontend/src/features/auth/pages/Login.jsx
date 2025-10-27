import React, { useState, useEffect } from "react";
import { useAuth } from "../../../context/useAuth";
import LoginCard from "../components/LoginCard";
import RegisterCard from "../components/RegisterCard";
import axios from "axios";
import { validateLogin } from "../validations/validateLogin";

const Login = () => {
    const { login } = useAuth();
    const [ip, setIp] = useState("desconocido");
    const [identifier, setIdentifier] = useState("");
    const [password, setPassword] = useState("");
    const [showPassword, setShowPassword] = useState(false);
    const [errors, setErrors] = useState({});
    const [feedbackMessage, setFeedbackMessage] = useState("");
    const [feedbackType, setFeedbackType] = useState("danger");
    const [rememberMe, setRememberMe] = useState(false);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        axios.get("https://api.ipify.org?format=json")
            .then(res => setIp(res.data.ip))
            .catch(() => setIp("desconocido"));
    }, []);

    const togglePassword = () => setShowPassword(!showPassword);

    const validate = () => {
        const newErrors = validateLogin({ identifier, password });
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!validate()) return;

        setLoading(true);
        const result = await login({
            identifier,
            password,
            ip,
            rememberMe
        });

        if (result.success) {
            setFeedbackMessage("¡Inicio de sesión exitoso! Redirigiendo...");
            setFeedbackType("success");
        } else {
            setFeedbackMessage(result.message);
            setFeedbackType("danger");
        }

        setLoading(false);
    };

    return (
        <div className="container d-flex justify-content-center align-items-center flex-grow-1">
            <div className="row w-100 my-4 gap-4 gap-lg-0">
                <div className="col-lg-6">
                    <LoginCard
                        identifier={identifier}
                        setIdentifier={setIdentifier}
                        password={password}
                        setPassword={setPassword}
                        showPassword={showPassword}
                        togglePassword={togglePassword}
                        rememberMe={rememberMe}
                        setRememberMe={setRememberMe}
                        errors={errors}
                        feedbackMessage={feedbackMessage}
                        feedbackType={feedbackType}
                        handleSubmit={handleSubmit}
                        loading={loading}
                    />
                </div>
                <div className="col-lg-6">
                    <RegisterCard />
                </div>
            </div>
        </div>
    );
};

export default Login;
