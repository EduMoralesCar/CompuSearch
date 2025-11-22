package com.universidad.compusearch.exception;

/**
 * Excepción para manejar errores relacionados con tokens (JWT o de reseteo).
 *
 * <p>
 * Esta clase extiende {@link CustomException} y proporciona métodos estáticos
 * para generar excepciones predefinidas según distintos escenarios:
 * token no encontrado, expirado o inválido.
 * </p>
 */
public class TokenException extends CustomException {

    /**
     * Constructor principal de la excepción.
     *
     * @param message Mensaje descriptivo del error
     * @param status  Código HTTP asociado
     * @param code    Código único que identifica la excepción
     */
    public TokenException(String message, int status, String code) {
        super(message, status, code);
    }

    /**
     * Excepción cuando un token de un tipo específico no se encuentra.
     *
     * @param type Tipo de token (por ejemplo, "Refresh", "Reset")
     * @return Instancia de TokenException
     */
    public static TokenException notFound(String type) {
        return new TokenException(type + " token no encontrado",
                404,
                type.toUpperCase() + "_TOKEN_NOT_FOUND");
    }

    /**
     * Excepción cuando un token de un tipo específico ha expirado.
     *
     * @param type Tipo de token
     * @return Instancia de TokenException
     */
    public static TokenException expired(String type) {
        return new TokenException(type + " token ha expirado",
                401,
                type.toUpperCase() + "_TOKEN_EXPIRED");
    }

    /**
     * Excepción cuando un token de un tipo específico es inválido.
     *
     * @param type Tipo de token
     * @return Instancia de TokenException
     */
    public static TokenException invalid(String type) {
        return new TokenException(type + " token inválido",
                401,
                type.toUpperCase() + "_TOKEN_INVALID");
    }

    /**
     * Excepción genérica para un token inválido sin especificar tipo.
     *
     * @return Instancia de TokenException
     */
    public static TokenException invalidType() {
        return new TokenException("Token inválido", 401, "TOKEN_INVALID");
    }

    /**
     * Excepción genérica para un token expirado sin especificar tipo.
     *
     * @return Instancia de TokenException
     */
    public static TokenException expiredType() {
        return new TokenException("Token expirado", 401, "TOKEN_EXPIRED");
    }
}
