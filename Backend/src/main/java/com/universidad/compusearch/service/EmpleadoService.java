package com.universidad.compusearch.service;

import com.universidad.compusearch.exception.EmpleadoException;
import com.universidad.compusearch.entity.Empleado;
import com.universidad.compusearch.repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Servicio que gestiona las operaciones relacionadas con los empleados.
 * <p>
 * Esta clase se encarga de acceder al repositorio de empleados para realizar
 * operaciones como la búsqueda por ID.
 * </p>
 *
 * @author Jesus
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmpleadoService {

    /** Repositorio para acceder a los datos de los empleados. */
    private final EmpleadoRepository empleadoRepository;

    /**
     * Obtiene un empleado por su identificador único.
     *
     * @param idEmpleado identificador del empleado que se desea obtener
     * @return el objeto {@link Empleado} correspondiente al ID especificado
     * @throws com.universidad.compusearch.exception.EmpleadoException si no se
     *                                                                 encuentra el
     *                                                                 empleado
     */
    public Empleado obtenerPorId(Long idEmpleado) {
        log.info("Buscando empleado con ID: {}", idEmpleado);

        Empleado empleado = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> {
                    log.warn("Empleado con ID {} no encontrado", idEmpleado);
                    return EmpleadoException.notFound();
                });

        log.info("Empleado encontrado: {}", empleado.getNombre());
        return empleado;
    }
}
