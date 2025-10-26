import axios from "axios";

export const MeService = async () => {
    return axios.get("http://localhost:8080/auth/me", {
        withCredentials: true,
    });
};
