import React, { useEffect, useState } from "react";
import { useAuthStatus } from "../hooks/useAuthStatus";

const AuthToast = () => {
    const { isAuthenticated, sessionReady } = useAuthStatus();
    const [visible, setVisible] = useState(false);

    useEffect(() => {
        if (!sessionReady) return;

        setVisible(true);
        const timer = setTimeout(() => setVisible(false), 3000);
        return () => clearTimeout(timer);
    }, [isAuthenticated, sessionReady]);

    if (!visible || !sessionReady) return null;

    return (
        <div
            className={`position-fixed border-light alert alert-${isAuthenticated ? "success" : "warning"} shadow text-center`}
            style={{
                top: "20px",
                left: "50%",
                transform: "translateX(-50%)",
                zIndex: 9999,
                minWidth: "300px",
                maxWidth: "90%",
                padding: "1rem"
            }}
        >
            {isAuthenticated
                ? "Has iniciado sesión correctamente"
                : "No has iniciado sesión"}
        </div>
    );
};

export default AuthToast;
