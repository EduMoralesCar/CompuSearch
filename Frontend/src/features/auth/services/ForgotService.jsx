import axios from "axios";

export const ForgotService = async ( { email, ip } ) => {
    return axios.post("http://localhost:8080/auth/password/forgot", {
        email: email,
        dispositivo: ip
    }, {
        withCredentials: true
    });
};
