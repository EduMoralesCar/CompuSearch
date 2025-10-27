export const validateForgotPassword = ({ email }) => {
    const errors = {};
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!email || email.trim() === "") {
        errors.email = "El email es obligatorio";
    } else if (/\s/.test(email.trim())) {
        errors.email = "El correo no debe contener espacios.";
    } else if (!emailRegex.test(email)) {
        errors.email = "Formato de correo inválido.";
    }

    return errors;
};
