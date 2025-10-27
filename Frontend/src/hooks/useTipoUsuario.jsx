// src/hooks/useTipoUsuario.js
import Cookies from "js-cookie";
import jwtDecode from "jwt-decode";

export const useTipoUsuario = () => {
    const token = Cookies.get("access_token");
    if (!token) return null;

    try {
        const decoded = jwtDecode(token);
        return decoded.tipoUsuario || null;
    } catch (err) {
        console.error("Token inválido:", err);
        return null;
    }
};
