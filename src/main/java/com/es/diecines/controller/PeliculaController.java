package com.es.diecines.controller;

import com.es.diecines.dto.PeliculaDTO;
import com.es.diecines.error.BaseDeDatosExceptioin;
import com.es.diecines.error.ErrorGenerico;
import com.es.diecines.service.PeliculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.prefs.BackingStoreException;

@RestController
@RequestMapping("/peliculas")
public class PeliculaController {

    @Autowired
    private PeliculaService service;

    // CREATE

    /**
     * Crea una película
     */
    @GetMapping("/") // POST localhost:8080/pokemon/
    public PeliculaDTO insert(
            @RequestBody PeliculaDTO peliculaDTO
    ) {

        return null;
    }


    // UPDATE

    /**
     * @return Pelicula modificada
     */
    public PeliculaDTO modify() {
        return null;
    }


    // READ

    /**
     * Consulta una película por su ID
     *
     * @return Info de la película
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable String id
    ) throws BackingStoreException {

        try {
            // Comprobar que el id no viene vacio
            if (id == null || id.isEmpty()) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

            // obtenemos la pelicula
            PeliculaDTO p = service.getById(id);

            // Comprobamos la validez de p para devolver una respuesta
            if (p == null) {
                ErrorGenerico error = new ErrorGenerico("Pelicula no encontrada", "localhost:8080/peliculas/" + id);
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            } else {
                ResponseEntity<PeliculaDTO> respuesta = new ResponseEntity<PeliculaDTO>(
                        p, HttpStatus.CREATED
                );
                return respuesta;
            }
        } catch (BaseDeDatosExceptioin e) {
            ErrorGenerico error = new ErrorGenerico(
                    e.getMessage(),
                    "localhost:8080/peliculas/" + id
            );
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (BackingStoreException e) {
            throw new BackingStoreException("Error inesperado con ID '" + id + "'");
        }

    }

    /**
     * Devuelve una lista de todas las películas
     *
     * @return Lista de todas las peliculas
     */
    public List<PeliculaDTO> getAll() {
        return null;
    }
}
