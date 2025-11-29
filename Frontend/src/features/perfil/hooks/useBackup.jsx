import { useState, useCallback } from "react";
import axios from "axios";

const API_URL = "http://localhost:8080/backup/ejecutar";

export const useBackup = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(false);

    const ejecutarBackup = useCallback(async () => {
        setLoading(true);
        setError(null);
        setSuccess(false);

        try {
            const res = await axios.post(API_URL, null, { withCredentials: true });
            setSuccess(true);
            return res.data;
        } catch (err) {
            setError(err.response?.data || "Error al ejecutar el backup.");
            setSuccess(false);
        } finally {
            setLoading(false);
        }
    }, []);

    return {
        loading,
        error,
        success,
        ejecutarBackup,
    };
};
