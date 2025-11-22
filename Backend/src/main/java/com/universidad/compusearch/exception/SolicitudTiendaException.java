package com.universidad.compusearch.exception;

/**
 * Excepción personalizada para manejar errores relacionados con las operaciones
 * sobre solicitudes de tiendas.
 *
 * <p>
 * Extiende de {@link CustomException} y proporciona métodos estáticos para crear
 * instancias de errores específicos de este contexto, tales como solicitudes no
 * encontradas o estados inválidos.
 * </p>
 *
 * <p>
 * Cada instancia incluye un mensaje, un código HTTP y un código de error interno
 * para facilitar el manejo y la trazabilidad de los errores.
 * </p>
 *
 */
public class SolicitudTiendaException extends CustomException {

    /**
     * Crea una nueva excepción personalizada para operaciones sobre solicitudes de tienda.
     *
     * @param message mensaje descriptivo del error.
     * @param status  código de estado HTTP asociado al error.
     * @param code    código interno de error para facilitar la identificación.
     */
    public SolicitudTiendaException(String message, int status, String code) {
        super(message, status, code);
    }

    /**
     * Genera una excepción preconfigurada indicando que la solicitud no fue encontrada.
     *
     * <p>
     * Se utiliza cuando el sistema intenta acceder a una solicitud inexistente en la base de datos.
     * </p>
     *
     * @return una instancia de {@link SolicitudTiendaException} con mensaje, estado HTTP y código definidos.
     */
    public static SolicitudTiendaException notFound() {
        return new SolicitudTiendaException(
                "Solicitud no encontrada",
                404,
                "SOLICITUD_NOT_FOUND"
        );
    }

    /**
     * Genera una excepción preconfigurada indicando que el estado proporcionado para la solicitud
     * es incorrecto o no permitido.
     *
     * <p>
     * Se utiliza cuando se intenta actualizar una solicitud con un valor de estado inválido.
     * </p>
     *
     * @return una instancia de {@link SolicitudTiendaException} con mensaje, estado HTTP y código definidos.
     */
    public static SolicitudTiendaException estadoIncorrecto() {
        return new SolicitudTiendaException(
                "Estado incorrecto para la solicitud",
                400,
                "BAD_ESTADO_SOLICITUD"
        );
    }
}
