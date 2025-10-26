import axios from "axios";

export const ResetService = async ({ token, password }) => {
    return axios.post("http://localhost:8080/auth/password/reset", {
        token: token,
        contrasena: password
    }, {
        withCredentials: true
    });
};