package com.universidad.compusearch.exception;

/**
 * Excepción para manejar errores relacionados con categorías.
 *
 * <p>
 * Esta clase extiende {@link CustomException} y proporciona métodos estáticos
 * para generar excepciones predefinidas cuando una categoría no se encuentra.
 * </p>
 */
public class CategoriaException extends CustomException {

    /**
     * Constructor principal de la excepción.
     *
     * @param message Mensaje descriptivo del error
     * @param status  Código HTTP asociado
     * @param code    Código único que identifica la excepción
     */
    public CategoriaException(String message, int status, String code) {
        super(message, status, code);
    }

    /**
     * Excepción cuando una categoría no es encontrada.
     *
     * @return Instancia de CategoriaException
     */
    public static CategoriaException notFound() {
        return new CategoriaException(
                "Categoria no encontrado",
                404,
                "CATEGORY_NOT_FOUND");
    }

    /**
     * Excepción cuando una categoría está en uso y no se puede eliminar.
     *
     * @return Instancia de {@link CategoriaException} indicando conflicto
     */
    public static CategoriaException inUse() {
        return new CategoriaException(
                "Categoria en uso por otros productos",
                409,
                "CATEGORY_IN_USE");
    }

    /**
     * Excepción cuando una categoría con el mismo nombre existe
     *
     * @return Instancia de {@link CategoriaException} indicando conflicto
     */
    public static CategoriaException exist() {
        return new CategoriaException(
                "Ya existe una categoría con el nombre proporcionado",
                400,
                "CATEGORY_ALREADY_EXISTS");
    }

}
