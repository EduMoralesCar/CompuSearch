import axios from "axios";

export const RefreshService = async () => {
    return axios.post("http://localhost:8080/auth/refresh", null, {
        withCredentials: true,
    });
};
