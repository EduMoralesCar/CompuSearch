import React, { useState, useEffect } from "react";
import { useAuth } from "../../../context/useAuth";
import { useSearchParams, useNavigate } from "react-router-dom";
import { Card, Spinner } from "react-bootstrap";
import { validateResetPassword } from "../validations/validateResetPassword";
import ResetPasswordForm from "../components/resetPassword/ResetPasswordForm";

const ResetPassword = () => {
    const { resetPassword } = useAuth();
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const [token, setToken] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [feedbackMessage, setFeedbackMessage] = useState("");
    const [feedbackType, setFeedbackType] = useState("danger");
    const [loading, setLoading] = useState(false);
    const [errors, setErrors] = useState({});

    useEffect(() => {
        const extractedToken = searchParams.get("token");
        if (extractedToken) {
            setToken(extractedToken);
        } else {
            setFeedbackMessage("Verificando token de acceso...");
            setFeedbackType("warning");
            setTimeout(() => navigate("/forgot-password"), 2500);
        }
    }, [searchParams, navigate]);

    const validate = () => {
        const newErrors = validateResetPassword({ password, confirmPassword });
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!validate()) return;

        setLoading(true);
        setFeedbackMessage("");
        setFeedbackType("danger");

        const result = await resetPassword({ token, password });

        if (result.success) {
            setFeedbackMessage(result.message || "Contraseña actualizada correctamente");
            setFeedbackType("success");
            setTimeout(() => navigate("/login"), 2500);
        } else {
            setFeedbackMessage(result.message);
            setFeedbackType("danger");
        }

        setLoading(false);
    };

    if (!token) {
        return (
            <div className="container d-flex justify-content-center align-items-center flex-grow-1 my-4">
                <div className="text-center">
                    <Spinner animation="border" />
                    <p className="text-muted mt-3">{feedbackMessage}</p>
                </div>
            </div>
        );
    }

    return (
        <div className="container d-flex justify-content-center align-items-center flex-grow-1">
            <div className="row w-100 my-4">
                <div className="col-md-6 offset-md-3">
                    <Card className="shadow-sm p-4">
                        <h3 className="fw-bold text-primary mb-4">RESTABLECER CONTRASEÑA</h3>
                        <ResetPasswordForm
                            password={password}
                            setPassword={setPassword}
                            confirmPassword={confirmPassword}
                            setConfirmPassword={setConfirmPassword}
                            showPassword={showPassword}
                            setShowPassword={setShowPassword}
                            showConfirmPassword={showConfirmPassword}
                            setShowConfirmPassword={setShowConfirmPassword}
                            errors={errors}
                            feedbackMessage={feedbackMessage}
                            feedbackType={feedbackType}
                            loading={loading}
                            handleSubmit={handleSubmit}
                        />
                    </Card>
                </div>
            </div>
        </div>
    );
};

export default ResetPassword;
