package com.es.diecines.controller;

import com.es.diecines.dto.PeliculaDTO;
import com.es.diecines.error.BaseDeDatosException;
import com.es.diecines.error.ErrorGenerico;
import com.es.diecines.service.PeliculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.prefs.BackingStoreException;


/**
 * Controlador REST para gestionar las operaciones CRUD de la entidad Pelicula.
 * <p>
 * Esta clase expone los endpoints HTTP para crear, leer, actualizar y eliminar
 * registros de películas en la base de datos. Utiliza el servicio {@link PeliculaService}
 * para realizar la lógica de negocio y se comunica con el cliente a través de
 * objetos de respuesta {@link ResponseEntity} que contienen los códigos de estado HTTP.
 *
 * <p>EndPoints expuestos:</p>
 * <ul>
 *     <li>GET /peliculas/{id} - Consulta una película por su ID.</li>
 *     <li>POST /peliculas/ - Crea una nueva película.</li>
 *     <li>PUT /peliculas/{id} - Modifica una película existente.</li>
 *     <li>GET /peliculas/ - Consulta la lista completa de películas.</li>
 *     <li>DELETE /peliculas/{id} - Elimina una película por su ID.</li>
 * </ul>
 *
 * @see PeliculaService
 * @see PeliculaDTO
 * @see ResponseEntity
 */
@RestController
@RequestMapping("/peliculas")
public class PeliculaController {

    @Autowired
    private PeliculaService peliculaService;

    /**
     * CREATE
     * Crea una nueva película en la base de datos.
     * <p>
     * El método para insertar es un POST -> @PostMapping.
     * El recurso para este método es `localhost:8080/peliculas/`.
     * <p>
     * La información de la nueva película viene en el cuerpo de la petición.
     * Para obtener el cuerpo de la petición se usa la anotación @RequestBody.
     *
     * @param peliculaDTO Objeto PeliculaDTO con los datos de la película a crear.
     * @return ResponseEntity que contiene el objeto PeliculaDTO creado y el estado HTTP correspondiente.
     */
    @PostMapping("/")
    public ResponseEntity<?> insert(
            @RequestBody PeliculaDTO peliculaDTO
    ) {

        if (peliculaDTO == null) {
            ErrorGenerico error = new ErrorGenerico(
                    "Los datos de la película no pueden estar vacíos.", "localhost:8080/peliculas/"
            );
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        try {
            PeliculaDTO nuevaPelicula = peliculaService.insert(peliculaDTO);
            return new ResponseEntity<>(nuevaPelicula, HttpStatus.CREATED);

        } catch (BaseDeDatosException e) {
            ErrorGenerico error = new ErrorGenerico(e.getMessage(), "localhost:8080/peliculas/");
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            ErrorGenerico error = new ErrorGenerico("Error inesperado al crear la película", "localhost:8080/peliculas/");
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * READ
     * Consulta una película por su ID.
     * <p>
     * Este método recibe un ID de película en la URL, verifica su validez y
     * consulta en el servicio si existe una película con ese ID. En caso de que
     * no exista o haya un error en la búsqueda, responde con un mensaje adecuado
     * y el estado HTTP correspondiente.
     *
     * @param id Identificador de la película.
     * @return ResponseEntity con el objeto PeliculaDTO si se encuentra, o con un objeto
     * ErrorGenerico en caso de error o si no se encuentra la película.
     * @throws BackingStoreException si el ID tiene un formato inválido.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable String id
    ) throws BackingStoreException {

        if (id == null || id.isEmpty()) {
            ErrorGenerico error = new ErrorGenerico("El ID de la película no puede estar vacío.", "localhost:8080/peliculas/" + id);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        try {
            PeliculaDTO pelicula = peliculaService.getById(id);

            if (pelicula == null) {
                ErrorGenerico error = new ErrorGenerico(
                        "Película no encontrada",
                        "localhost:8080/peliculas/" + id
                );
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(pelicula, HttpStatus.OK);

        } catch (BaseDeDatosException e) {
            ErrorGenerico error = new ErrorGenerico(
                    e.getMessage(),
                    "localhost:8080/peliculas/" + id
            );
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (BackingStoreException e) {
            throw new BackingStoreException("Error inesperado con ID '" + id);
        }

    }

    /**
     * UPDATE
     * Actualiza un registro existente de una película.
     * <p>
     * Este método permite modificar los datos de una película existente en la base de datos.
     *
     * @param id          Identificador de la película que se va a modificar.
     * @param peliculaDTO Objeto DTO con los nuevos datos de la película.
     * @return ResponseEntity con el objeto PeliculaDTO modificado, o un mensaje de error si no se encuentra la película.
     * @throws IllegalArgumentException si el ID tiene un formato inválido.
     * @throws BaseDeDatosException     si ocurre un error al actualizar en la base de datos.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> modify(
            @PathVariable String id,
            @RequestBody PeliculaDTO peliculaDTO) {

        if (peliculaDTO == null) {
            ErrorGenerico error = new ErrorGenerico(
                    "No se proporcionaron datos para la película.",
                    "localhost:8080/peliculas/" + id
            );
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        try {
            PeliculaDTO peliculaModificada = peliculaService.modify(id, peliculaDTO);

            if (peliculaModificada == null) {
                ErrorGenerico error = new ErrorGenerico(
                        "Película no encontrada",
                        "localhost:8080/peliculas/" + id
                );
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(peliculaModificada, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            ErrorGenerico error = new ErrorGenerico(
                    e.getMessage(),
                    "localhost:8080/peliculas/" + id
            );
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

        } catch (BaseDeDatosException e) {
            ErrorGenerico error = new ErrorGenerico(
                    e.getMessage(),
                    "localhost:8080/peliculas/" + id
            );
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * DELETE
     * Elimina una película existente de la base de datos.
     * <p>
     * Este método utiliza un DELETE -> @DeleteMapping y el recurso es `localhost:8080/peliculas/{id}`.
     * Recibe el ID de la película a eliminar en la URL y verifica si esta existe en la base de datos.
     * Si la película no se encuentra, responde con NOT_FOUND. Si el ID tiene un formato inválido,
     * responde con BAD_REQUEST. En caso de error inesperado, responde con INTERNAL_SERVER_ERROR.
     * </p>
     *
     * @param id Identificador de la película que se va a eliminar.
     * @return ResponseEntity con un mensaje de éxito si la película se elimina correctamente, o con un objeto
     * ErrorGenerico en caso de error o si no se encuentra la película.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable String id
    ) {
        if (id == null || id.isEmpty()) {
            ErrorGenerico error = new ErrorGenerico(
                    "Id de película no válido.",
                    "localhost:8080/peliculas/" + id
            );
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        try {
            peliculaService.delete(id);
            return new ResponseEntity<>("Película eliminada correctamente", HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            ErrorGenerico error = new ErrorGenerico(
                    e.getMessage(),
                    "localhost:8080/peliculas" + id
            );
            return  new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (BaseDeDatosException e) {
            ErrorGenerico error = new ErrorGenerico(
                    e.getMessage(),
                    "localhost:8080/peliculas" + id
            );
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * READ ALL
     * Devuelve una lista de todas las películas registradas en la base de datos.
     *
     * @return ResponseEntity con la lista de PeliculaDTOs. Si no hay películas, devuelve una lista vacía.
     */
    @GetMapping("/")
    public ResponseEntity<?> getAll() {
        try {
            List<PeliculaDTO> peliculas = peliculaService.getAll();

            if (peliculas.isEmpty()) {
                return new ResponseEntity<>("Peliculas no registradas.", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(peliculas, HttpStatus.OK);
            }

        } catch (BaseDeDatosException e) {
            ErrorGenerico error = new ErrorGenerico(
                    e.getMessage(),
                    "localhost:8080/peliculas/"
            );
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            ErrorGenerico error = new ErrorGenerico(
                    "Error inesperado al obtener la lista de películas.",
                    "localhost:8080/peliculas/"
            );
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
