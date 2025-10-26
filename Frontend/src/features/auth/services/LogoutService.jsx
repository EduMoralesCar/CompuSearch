import axios from "axios";

export const LogoutService = async () => {
  return axios.post("http://localhost:8080/auth/logout", null, {
    withCredentials: true,
  });
};
