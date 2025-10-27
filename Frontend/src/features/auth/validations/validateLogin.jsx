export const validateLogin = ({ identifier, password }) => {
    const errors = {};

    if (!identifier || identifier.trim() === "") {
        errors.identifier = "El usuario o email es obligatorio";
    } else if (/\s/.test(identifier)) {
        errors.identifier = "El usuario o email no debe contener espacios";
    }

    if (!password || password.trim() === "") {
        errors.password = "La contraseña es obligatoria";
    } else if (/\s/.test(password)) {
        errors.password = "La contraseña no debe contener espacios";
    } else if (password.length < 8) {
        errors.password = "Debe tener al menos 8 caracteres";
    }

    return errors;
};
