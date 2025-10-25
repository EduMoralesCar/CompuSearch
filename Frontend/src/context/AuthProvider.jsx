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
    const [idUsuario, setIdUsuario] = useState(null);
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
            setIdUsuario(res.data.idUsuario);

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
            setIdUsuario(res.data.idUsuario);

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

    const logout = async () => {
        try {
            await axios.post("http://localhost:8080/auth/logout", null, {
                withCredentials: true,
            });
        } catch (error) {
            console.error("Error al cerrar sesión:", error);
        } finally {
            setUsuario(null);
            setTipoUsuario(null);
            setIdUsuario(null)
        }
    };

    const refreshSession = async () => {
        try {
            await axios.post("http://localhost:8080/auth/refresh", null, {
                withCredentials: true,
            });

            const res = await axios.get("http://localhost:8080/auth/me", {
                withCredentials: true,
            });

            setUsuario(res.data);
            setTipoUsuario(res.data.tipoUsuario);
            setIdUsuario(res.data.idUsuario);
            return { success: true };
        } catch (error) {
            console.error("Error al refrescar sesión:", error);
            return {
                success: false,
                message: error.response?.data?.message || "No se pudo renovar la sesión",
            };
        }
    };

    useEffect(() => {
        if (usuario && tipoUsuario && idUsuario) {
            setSessionReady(true);
            return;
        }

        axios.get("http://localhost:8080/auth/me", { withCredentials: true })
            .then(res => {
                setUsuario(res.data);
                setTipoUsuario(res.data.tipoUsuario);
                setIdUsuario(res.data.idUsuario);
            })
            .catch(async (err) => {
                const status = err.response?.status;
                if (status === 401 || status === 403) {
                    const result = await refreshSession();
                    if (!result.success) {
                        setUsuario(null);
                        setTipoUsuario(null);
                        setIdUsuario(null);
                    }
                } else {
                    setSessionError(err.response?.data?.message || "Error al cargar sesión");
                    logout();
                }
            })
            .finally(() => {
                setSessionReady(true);
            });
    }, [tipoUsuario, usuario, idUsuario]);

    return (
        <AuthContext.Provider value={{
            usuario,
            tipoUsuario,
            idUsuario,
            sessionReady,
            sessionError,
            login,
            logout,
            forgotPassword,
            resetPassword,
            registro,
            refreshSession
        }}>
            {children}
        </AuthContext.Provider>
    );
};
