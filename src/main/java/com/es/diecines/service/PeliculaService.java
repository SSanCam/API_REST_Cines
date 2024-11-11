package com.es.diecines.service;

import com.es.diecines.dto.PeliculaDTO;
import com.es.diecines.error.BaseDeDatosException;
import com.es.diecines.model.Pelicula;
import com.es.diecines.repository.PeliculaRepository;
import com.es.diecines.utils.Mapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;

/**
 * Servicio para gestionar las operaciones CRUD de la entidad Pelicula.
 * <p>
 * Esta clase proporciona métodos para crear, leer, actualizar y eliminar registros de
 * películas en la base de datos. Utiliza el {@link PeliculaRepository} para interactuar
 * con la base de datos y el {@link Mapper} para convertir entre entidades y DTOs.
 * </p>
 * <p>
 * Excepciones personalizadas son lanzadas en caso de errores de base de datos o si el
 * formato del ID es inválido.
 * </p>
 *
 * @see PeliculaDTO
 * @see Pelicula
 * @see PeliculaRepository
 * @see Mapper
 */
@Service
public class PeliculaService {

    @Autowired
    private PeliculaRepository repository;


    /**
     * CREATE
     * Crea un nuevo registro en la entidad Peliculas.
     *
     * @param peliculaDTO Nueva película en formato DTO que se va a insertar.
     * @return PeliculaDTO con la información de la película recién creada.
     * @throws IllegalArgumentException si el argumento peliculaDTO es nulo.
     */
    public PeliculaDTO insert(PeliculaDTO peliculaDTO) {
        if (peliculaDTO != null) {
            Pelicula pelicula = Mapper.DtoToEntity(peliculaDTO);
            repository.save(pelicula);
            return Mapper.entityToDTO(pelicula);
        }
        return null;
    }

    /**
     * READ
     * Obtiene una película por su ID.
     *
     * @param id Identificador de la película en formato String.
     * @return PeliculaDTO con la información de la película encontrada.
     * @throws BackingStoreException si el formato del ID no es válido.
     * @throws BaseDeDatosException  si ocurre un error al buscar en la base de datos.
     */
    public PeliculaDTO getById(String id) throws BackingStoreException {
        try {
            Long idL = Long.parseLong(id);
            Pelicula pelicula = repository
                    .findById(idL)
                    .orElseThrow(() -> new EntityNotFoundException("La pelicula con id " + id + " no existe."));
            return Mapper.entityToDTO(pelicula);
        } catch (NumberFormatException e) {
            throw new BackingStoreException("ID no válido: " + id);
        } catch (EntityNotFoundException e) {
            throw new BaseDeDatosException("Error al buscar en la base de datos", e);
        } catch (Exception e) {
            throw new BaseDeDatosException("Error inesperado en la base de datos", e);
        }
    }

    /**
     * UPDATE
     * Modifica un registro existente de una película.
     *
     * @param id          Identificador de la película que se va a modificar.
     * @param peliculaDTO Objeto DTO con los nuevos datos de la película.
     * @return PeliculaDTO con la información de la película modificada.
     * @throws BaseDeDatosException     si ocurre un error al acceder a la base de datos.
     * @throws IllegalArgumentException si el formato del ID no es válido.
     * @throws EntityNotFoundException  si no se encuentra la película con el ID proporcionado.
     */
    public PeliculaDTO modify(String id, PeliculaDTO peliculaDTO) {
        try {
            Long idL = Long.parseLong(id);

            Pelicula peliculaExt = repository
                    .findById(idL)
                    .orElseThrow(() -> new EntityNotFoundException("La película con id " + id + " no se ha encontrado."));

            peliculaExt.setTitle(peliculaDTO.getTitle());
            peliculaExt.setDirector(peliculaDTO.getDirector());
            peliculaExt.setTime(peliculaDTO.getTime());
            peliculaExt.setTrailer(peliculaDTO.getTrailer());
            peliculaExt.setPosterImage(peliculaDTO.getPosterImage());
            peliculaExt.setScreenshot(peliculaDTO.getScreenshot());
            peliculaExt.setSynopsis(peliculaDTO.getSynopsis());
            peliculaExt.setRating(peliculaDTO.getRating());

            repository.save(peliculaExt);

            return Mapper.entityToDTO(peliculaExt);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID no válido: " + id, e);
        } catch (EntityNotFoundException e) {
            throw new BaseDeDatosException("Error al actualizar en la base de datos: Película no encontrada", e);
        } catch (Exception e) {
            throw new BaseDeDatosException("Error inesperado al actualizar la película en la base de datos", e);
        }
    }


    /**
     * DELETE
     * Elimina un registro de una película por su ID.
     *
     * @param id Identificador de la película que se desea eliminar.
     * @throws IllegalArgumentException si el formato del ID no es válido.
     * @throws BaseDeDatosException     si no se encuentra la película en la base de datos
     *                                  o si ocurre un error inesperado durante la operación.
     */
    public void delete(String id) {
        try {
            Long idL = Long.parseLong(id);

            if (repository.existsById(idL)) {
                repository.deleteById(idL);
            } else {
                throw new EntityNotFoundException("La película con ID " + id + " no existe.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID no válido: " + id, e);
        } catch (EntityNotFoundException e) {
            throw new BaseDeDatosException("Error al eliminar de la base de datos: Película no encontrada", e);
        } catch (Exception e) {
            throw new BaseDeDatosException("Error inesperado al eliminar la película en la base de datos", e);
        }
    }

    /**
     * READ ALL
     * Obtiene la lista completa de todas las películas registradas en la base de datos.
     *
     * @return Lista de objetos PeliculaDTO que representan todas las películas registradas.
     * Si no hay películas registradas, se devuelve una lista vacía.
     * @throws BaseDeDatosException si ocurre un error inesperado al acceder a la base de datos.
     */
    public List<PeliculaDTO> getAll() {
        try {
            List<Pelicula> listaPeliculas = repository.findAll();
            List<PeliculaDTO> listaPeliculaDTO = new ArrayList<>();

            for (Pelicula pelicula : listaPeliculas) {
                listaPeliculaDTO.add(Mapper.entityToDTO(pelicula));
            }
            return listaPeliculaDTO;

        } catch (Exception e) {
            throw new BaseDeDatosException("Error al obtener la lista de películas", e);
        }
    }

}