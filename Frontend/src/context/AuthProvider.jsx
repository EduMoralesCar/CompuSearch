import { useState, useEffect } from "react";
import { AuthContext } from "./AuthContext";
import { LoginService } from "../features/auth/services/LoginService";
import { RegisterService } from "../features/auth/services/RegisterService";
import { ForgotService } from "../features/auth/services/ForgotService";
import { ResetService } from "../features/auth/services/ResetService";
import { LogoutService } from "../features/auth/services/LogoutService";
import { MeService } from "../features/auth/services/MeService";
import { RefreshService } from "../features/auth/services/RefreshService";

export const AuthProvider = ({ children }) => {
    const [tipoUsuario, setTipoUsuario] = useState(null);
    const [usuario, setUsuario] = useState(null);
    const [idUsuario, setIdUsuario] = useState(null);
    const [rol, setRol] = useState(null);
    const [sessionReady, setSessionReady] = useState(false);
    const [sessionError, setSessionError] = useState(null);

    const login = async ({ identifier, password, ip, rememberMe }) => {
        try {
            await LoginService({ identifier, password, ip, rememberMe });

            const resMe = await MeService();
            setUsuario(resMe.data);
            setTipoUsuario(resMe.data.tipoUsuario);
            setIdUsuario(resMe.data.idUsuario);
            setRol(resMe.data.rol);

            return { success: true };
        } catch (error) {
            console.log(error)
            setSessionError(error.response?.data?.message);
            return {
                success: false,
                message: error.response?.data?.message || "Error al iniciar sesión"
            };
        }
    };

    const registro = async ({ username, email, password, ip }) => {
        try {
            await RegisterService({ username, email, password, ip });

            const resMe = await MeService();

            setUsuario(resMe.data);
            setTipoUsuario(resMe.data.tipoUsuario);
            setIdUsuario(resMe.data.idUsuario);
            setRol(resMe.data.rol);

            return { success: true };
        } catch (error) {
            setSessionError(error.response?.data?.message);
            return {
                success: false,
                message: error.response?.data?.message || "Error al registrar"
            };
        }
    };

    const forgotPassword = async ({ email, ip }) => {
        try {
            const res = await ForgotService({ email, ip });
            return { success: true, message: res.data.message };
        } catch (error) {
            setSessionError(error.response?.data?.message);
            const message = error.response?.data?.error || "Error de conexión con el servidor";
            return { success: false, message };
        }
    };

    const resetPassword = async ({ token, password }) => {
        try {
            const res = await ResetService({ token, password });
            return { success: true, message: res.data.message };
        } catch (error) {
            setSessionError(error.response?.data?.message);
            const message = error.response?.data?.message || "Error de conexión con el servidor";
            return { success: false, message };
        }
    };

    const logout = async () => {
        try {
            await LogoutService();
        } catch (error) {
            setSessionError(error.response?.data?.message);
        } finally {
            setUsuario(null);
            setTipoUsuario(null);
            setIdUsuario(null);
            setRol(null);
        }
    };

    const refreshSession = async () => {
        try {
            await RefreshService();
            const resMe = await MeService();

            setUsuario(resMe.data);
            setTipoUsuario(resMe.data.tipoUsuario);
            setIdUsuario(resMe.data.idUsuario);
            setRol(resMe.data.rol);

            return { success: true };
        } catch (error) {
            setSessionError(error.response?.data?.message);
            setUsuario(null);
            setTipoUsuario(null);
            setIdUsuario(null);
            setRol(null);
            return { success: false };
        }
    };

    useEffect(() => {
        const loadSession = async () => {
            try {
                const refreshed = await refreshSession();
                if (!refreshed.success) {
                    setUsuario(null);
                    setTipoUsuario(null);
                    setIdUsuario(null);
                    setRol(null);
                }
            } catch (error) {
                setUsuario(null);
                setTipoUsuario(null);
                setIdUsuario(null);
                setRol(null);
                setSessionError(error.response?.data?.message || "Error al cargar sesión");
            } finally {
                setSessionReady(true);
            }
        };

        loadSession();
    }, []);


    return (
        <AuthContext.Provider value={{
            usuario,
            tipoUsuario,
            idUsuario,
            rol,
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
