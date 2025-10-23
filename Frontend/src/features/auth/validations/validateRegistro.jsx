export const validateRegistro = ({ username, email, password }) => {
    const errors = {};

    if (!username || username.trim() === "") {
        errors.username = "El nombre de usuario es obligatorio";
    } else if (/\s/.test(username)) {
        errors.username = "El nombre de usuario no debe contener espacios";
    } else if (!/^[a-zA-Z0-9_.-]{3,20}$/.test(username)) {
        errors.username = "Debe tener entre 3 y 20 caracteres alfanuméricos";
    }

    if (!email || email.trim() === "") {
        errors.email = "El correo electrónico es obligatorio";
    } else if (!/\S+@\S+\.\S+/.test(email)) {
        errors.email = "El correo electrónico no es válido";
    } else if (/\s/.test(email)){
        errors.username = "El email no debe contener espacios";
    }

    if (!password || password.trim() === "") {
        errors.password = "La contraseña es obligatoria";
    } else if (/\s/.test(password)) {
        errors.password = "La contraseña no debe contener espacios";
    } else {
        const regex = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$/;
        if (!regex.test(password)) {
            errors.password =
                "Debe tener al menos 8 caracteres, incluyendo mayúscula, minúscula, número y carácter especial";
        }
    }

    return errors;
};
