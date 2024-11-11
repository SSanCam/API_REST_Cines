package com.es.diecines.controller;

import com.es.diecines.dto.SesionDTO;
import com.es.diecines.error.BaseDeDatosException;
import com.es.diecines.error.ErrorGenerico;
import com.es.diecines.service.SesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.prefs.BackingStoreException;


/**
 * Controlador REST para gestionar las operaciones CRUD de la entidad Sesion.
 * <p>
 * Esta clase expone los endpoints HTTP para crear, leer, actualizar y eliminar
 * registros de sesiones en la base de datos. Utiliza el servicio {@link SesionService}
 * para realizar la lógica de negocio y responde con objetos {@link ResponseEntity}
 * que contienen los códigos de estado HTTP apropiados.
 * </p>
 * <p>EndPoints expuestos:</p>
 * <ul>
 *     <li>POST /sesiones - Crea una nueva sesión.</li>
 *     <li>GET /sesiones/{id} - Consulta una sesión por su ID.</li>
 *     <li>PUT /sesiones/{id} - Modifica una sesión existente.</li>
 *     <li>GET /sesiones - Consulta la lista completa de sesiones.</li>
 *     <li>DELETE /sesiones/{id} - Elimina una sesión por su ID.</li>
 * </ul>
 *
 * @see SesionService
 * @see SesionDTO
 * @see ResponseEntity
 */
@RestController
@RequestMapping("/sesiones")
public class SesionController {

    @Autowired
    private SesionService sesionService;

    /**
     * CREATE
     * Crea una nueva sesión en la base de datos.
     * <p>
     * Este método utiliza un POST -> @PostMapping y el recurso es `localhost:8080/sesiones`.
     * La información de la nueva sesión viene en el cuerpo de la petición como un {@link SesionDTO}.
     * </p>
     *
     * @param sesionDTO Objeto SesionDTO con los datos de la sesión a crear.
     * @return ResponseEntity que contiene el objeto SesionDTO creado y el estado HTTP correspondiente.
     * Responde con BAD_REQUEST si los datos son nulos, o INTERNAL_SERVER_ERROR si ocurre un error inesperado.
     */
    @PostMapping("/")
    public ResponseEntity<?> insert(
            @RequestBody SesionDTO sesionDTO
    ) {
        if (sesionDTO == null) {
            ErrorGenerico error = new ErrorGenerico(
                    "Los datos de la sesión no pueden estar vacíos.",
                    "localhost:8080/sesiones/"
            );
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        try {
            SesionDTO nuevaSesion = sesionService.insert(sesionDTO);
            return new ResponseEntity<>(
                    nuevaSesion,
                    HttpStatus.CREATED
            );

        } catch (IllegalArgumentException e) {
            ErrorGenerico error = new ErrorGenerico(
                    e.getMessage(),
                    "localhost:8080/sesiones/"
            );
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (BaseDeDatosException e) {
            ErrorGenerico error = new ErrorGenerico(
                    e.getMessage(),
                    "localhost:8080/sesiones/"
            );
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            ErrorGenerico error = new ErrorGenerico(
                    "Error inesperado al crear la sesión",
                    "localhost:8080/sesiones/"
            );
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * READ
     * Consulta una sesión por su ID.
     * <p>
     * Este método utiliza un GET -> @GetMapping y el recurso es `localhost:8080/sesiones/{id}`.
     * Recibe un ID de sesión en la URL, verifica su validez y consulta en el servicio si existe
     * una sesión con ese ID. Si la sesión no existe o si hay un error en la búsqueda, responde
     * con un mensaje adecuado y el estado HTTP correspondiente.
     * </p>
     *
     * @param id Identificador de la sesión.
     * @return ResponseEntity con el objeto SesionDTO si se encuentra, o con un objeto
     * ErrorGenerico en caso de error o si no se encuentra la sesión.
     * Responde con NOT_FOUND si la sesión no existe, BAD_REQUEST si el ID es inválido,
     * e INTERNAL_SERVER_ERROR en caso de un error inesperado.
     * @throws BackingStoreException si el ID tiene un formato inválido.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable String id
    ) throws BackingStoreException {
        if (id == null || id.isEmpty()) {
            ErrorGenerico error = new ErrorGenerico(
                    "El id de la sesión no válido.",
                    "localhost:8080/sesiones/" + id
            );
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        try {
            SesionDTO sesion = sesionService.getById(id);

            if (sesion == null) {
                ErrorGenerico error = new ErrorGenerico(
                        "Sesión no encontrada",
                        "localhost:8080/" + id
                );
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(sesion, HttpStatus.OK);

        } catch (BaseDeDatosException e) {
            ErrorGenerico error = new ErrorGenerico(
                    e.getMessage(),
                    "localhost:8080/sesiones/" + id
            );
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    /**
     * UPDATE
     * Modifica una sesión existente en la base de datos.
     * <p>
     * Este método utiliza un PUT -> @PutMapping y el recurso es `localhost:8080/sesiones/{id}`.
     * La información de la sesión actualizada se proporciona en el cuerpo de la petición como un {@link SesionDTO}.
     * </p>
     * <p>
     * El método busca la sesión por su ID proporcionado en la URL. Si la sesión no existe, responde con NOT_FOUND.
     * Si los datos de la sesión son nulos o inválidos, responde con BAD_REQUEST. En caso de error inesperado,
     * responde con INTERNAL_SERVER_ERROR.
     * </p>
     *
     * @param id        Identificador de la sesión que se va a modificar.
     * @param sesionDTO Objeto SesionDTO con los nuevos datos de la sesión.
     * @return ResponseEntity que contiene el objeto SesionDTO actualizado y el estado HTTP correspondiente.
     * Responde con NOT_FOUND si la sesión no se encuentra, BAD_REQUEST si los datos son nulos o inválidos,
     * y INTERNAL_SERVER_ERROR en caso de error inesperado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> modify(
            @PathVariable String id,
            @RequestBody SesionDTO sesionDTO
    ) {

        if (sesionDTO == null) {
            ErrorGenerico error = new ErrorGenerico(
                    "No se proporcionan datos para la sesión",
                    "localhost:8080/sesiones/" + id
            );
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        try {
            SesionDTO sesionModificada = sesionService.modify(id, sesionDTO);

            if (sesionModificada == null) {
                ErrorGenerico error = new ErrorGenerico(
                        "Sesión no encontrada.",
                        "localhost:8080/sesiones/" + id
                );
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(sesionModificada, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            ErrorGenerico error = new ErrorGenerico(
                    e.getMessage(),
                    "localhost:8080/sesiones/" + id
            );
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (BaseDeDatosException e) {
            ErrorGenerico error = new ErrorGenerico(
                    e.getMessage(),
                    "localhost:8080/sesiones" + id
            );
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * DELETE
     * Elimina una sesión existente de la base de datos.
     * <p>
     * Este método utiliza un DELETE -> @DeleteMapping y el recurso es `localhost:8080/sesiones/{id}`.
     * Recibe el ID de la sesión a eliminar en la URL y verifica si esta existe en la base de datos.
     * Si la sesión no se encuentra, responde con NOT_FOUND. Si el ID tiene un formato inválido,
     * responde con BAD_REQUEST. En caso de error inesperado, responde con INTERNAL_SERVER_ERROR.
     * </p>
     *
     * @param id Identificador de la sesión que se va a eliminar.
     * @return ResponseEntity con un mensaje de éxito si la sesión se elimina correctamente, o con un objeto
     * ErrorGenerico en caso de error o si no se encuentra la sesión.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable String id
    ) {
        if (id == null || id.isEmpty()) {
            ErrorGenerico error = new ErrorGenerico(
                    "Id de sesión no válido",
                    "localhost:8080/sesiones" + id
            );
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        try {
            sesionService.delete(id);
            return new ResponseEntity<>("Sesión eliminada correctamente", HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            ErrorGenerico error = new ErrorGenerico(
                    e.getLocalizedMessage(),
                    "localhost:8080/sesiones/" + id
            );
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (BaseDeDatosException e) {
            ErrorGenerico error = new ErrorGenerico(
                    e.getMessage(),
                    "localhost:8080/sesiones/" + id
            );
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * READ ALL
     * Obtiene una lista de todas las sesiones registradas en la base de datos.
     * <p>
     * Este método utiliza un GET -> @GetMapping y el recurso es `localhost:8080/sesiones`.
     * Recupera todas las sesiones disponibles en la base de datos y las devuelve en una lista.
     * Si no existen sesiones registradas, responde con NO_CONTENT.
     * En caso de error inesperado, responde con INTERNAL_SERVER_ERROR.
     * </p>
     *
     * @return ResponseEntity que contiene la lista de objetos SesionDTO. Si no hay sesiones, devuelve un mensaje indicando
     * que no hay sesiones registradas con el estado NO_CONTENT, o un objeto ErrorGenerico en caso de error.
     */
    @GetMapping("/")
    public ResponseEntity<?> getAll() {
        try {
            List<SesionDTO> sesiones = sesionService.getAll();

            if (sesiones.stream().isParallel()){
                return new ResponseEntity<>("Sesiones no registradas.", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(sesiones, HttpStatus.OK);
            }

        } catch (BaseDeDatosException e) {
            ErrorGenerico error = new ErrorGenerico(
                    e.getMessage(),
                    "localhost:8080/sesiones/"
            );
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            ErrorGenerico error = new ErrorGenerico(
                    "Error inesperado obteniendo la lista de sesiones.",
                    "localhost:8080/sesiones/"
            );
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);        }
    }

}
