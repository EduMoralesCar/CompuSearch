import axios from "axios";

export const loginService = async ({ identifier, password, ip, rememberMe }) => {
    return axios.post("http://localhost:8080/auth/login", {
        identificador: identifier,
        contrasena: password,
        dispositivo: ip,
        recordar: rememberMe
    }, {
        withCredentials: true
    });
};