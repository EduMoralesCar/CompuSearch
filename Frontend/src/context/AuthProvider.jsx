import { useState, useEffect } from "react";
import axios from "axios";
import { AuthContext } from "./AuthContext";
import { loginService } from "../features/auth/services/loginService";
import { registerService } from "../features/auth/services/registerService";
import { forgotService } from "../features/auth/services/forgotService";
import { resetService } from "../features/auth/services/resetService";

export const AuthProvider = ({ children }) => {
    const [tipoUsuario, setTipoUsuario] = useState(null);
    const [usuario, setUsuario] = useState(null);
    const [sessionReady, setSessionReady] = useState(false);
    const [sessionError, setSessionError] = useState(null);

    const login = async ({ identifier, password, ip, rememberMe }) => {
        try {
            await loginService({ identifier, password, ip, rememberMe });

            const res = await axios.get("http://localhost:8080/auth/me", {
                withCredentials: true
            });

            setUsuario(res.data);
            setTipoUsuario(res.data.tipoUsuario);

            return { success: true };
        } catch (error) {
            console.error("Error de login:", error);
            return {
                success: false,
                message: error.response?.data?.message || "Error al iniciar sesión"
            };
        }
    };

    const registro = async ({ username, email, password, ip }) => {
        try {
            await registerService({ username, email, password, ip });

            const res = await axios.get("http://localhost:8080/auth/me", {
                withCredentials: true
            });

            setUsuario(res.data);
            setTipoUsuario(res.data.tipoUsuario);

            return { success: true };
        } catch (error) {
            return {
                success: false,
                message: error.response?.data?.message || "Error al registrar"
            };
        }
    };

    const forgotPassword = async ({ email, ip }) => {
        try {
            const res = await forgotService({ email, ip });
            return { success: true, message: res.data.message };
        } catch (err) {
            const message = err.response?.data?.error || "Error de conexión con el servidor";
            return { success: false, message };
        }
    };

    const resetPassword = async ({ token, password }) => {
        try {
            const res = await resetService({ token, password });
            return { success: true, message: res.data.message };
        } catch (err) {
            const message = err.response?.data?.error || "Error de conexión con el servidor";
            return { success: false, message };
        }
    };

    const logout = () => {
        setUsuario(null);
        setTipoUsuario(null);
    };

    useEffect(() => {
        axios.get("http://localhost:8080/auth/me", { withCredentials: true })
            .then(res => {
                setUsuario(res.data);
                setTipoUsuario(res.data.tipoUsuario);
            })
            .catch((err) => {
                setSessionError(err.response?.data?.message || "Error al cargar sesión");
                logout();
            })
            .finally(() => {
                setSessionReady(true);
            });
    }, []);

    return (
        <AuthContext.Provider value={{
            usuario,
            tipoUsuario,
            sessionReady,
            sessionError,
            login,
            logout,
            forgotPassword,
            resetPassword,
            registro
        }}>
            {children}
        </AuthContext.Provider>
    );
};
