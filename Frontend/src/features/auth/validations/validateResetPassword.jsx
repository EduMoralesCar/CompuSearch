export const validateResetPassword = ({ password, confirmPassword }) => {
    const errors = {};
    const regex = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$/;

    // Validar campo vacío
    if (!password || password.trim() === "") {
        errors.password = "La contraseña es obligatoria.";
    } else if (/\s/.test(password)) {
        errors.password = "La contraseña no debe contener espacios.";
    } else if (!regex.test(password)) {
        errors.password = "Debe tener al menos 8 caracteres, incluyendo mayúscula, minúscula, número y símbolo.";
    }

    if (!confirmPassword || confirmPassword.trim() === "") {
        errors.confirmPassword = "Debes confirmar la contraseña.";
    } else if (/\s/.test(confirmPassword)) {
        errors.confirmPassword = "La confirmación no debe contener espacios.";
    } else if (password !== confirmPassword) {
        errors.confirmPassword = "Las contraseñas no coinciden.";
    }

    return errors;
};
