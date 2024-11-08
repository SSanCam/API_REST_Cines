package com.es.diecines.service;

import com.es.diecines.dto.PeliculaDTO;
import com.es.diecines.model.Pelicula;
import com.es.diecines.repository.PeliculaRepository;
import com.es.diecines.utils.Mapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;

@Service
public class PeliculaService {

    @Autowired
    private PeliculaRepository repository;
    @Autowired
    private Mapper mapper;

    /**
     * Creamos un nuevo resgistro en Peliculas
     *
     * @param peliculaDTO Nueva película
     * @return Obtenemos la información de la pelicula
     */
    public PeliculaDTO insert(PeliculaDTO peliculaDTO) {
        PeliculaDTO pelicula = Mapper.createPelicula(peliculaDTO);

        if (peliculaDTO == null) {
            return null;
        } else {
            return Mapper.createPelicula(pelicula);
        }
    }

    /**
     * Obtenemos una película por su ID
     *
     * @param id Identificador de la película
     * @return Película
     */
    public PeliculaDTO getById(String id) throws BackingStoreException {

        Long idL = null;
        try {
            idL = Long.parseLong(id);
            Pelicula pelicula = repository.findById(idL)
                    .orElseThrow(() -> new EntityNotFoundException("La pelicula con id " + id + " no existe."));
            return Mapper.mapToDTO(pelicula);
        } catch (NumberFormatException | DuplicateKeyException e) {
            throw new BackingStoreException("ID '" + id + "' erróneo");
        }
    }


    /**
     * Modifica un registro de una pelicula
     *
     * @return Pelicula modificada
     */
    public PeliculaDTO modify(String id, PeliculaDTO peliculaDTO) {

        try {
            Long idL = Long.parseLong(id);
            Pelicula peliculaExt = repository.findById(idL)
                    .orElseThrow(() -> new EntityNotFoundException("La película con id " + id + " no se ha encontrado."));

            // Se actualizan los campos de la película
            peliculaExt.setTitle(peliculaDTO.getTitle());
            peliculaExt.setDirector(peliculaDTO.getDirector());
            peliculaExt.setTime(peliculaDTO.getTime());
            peliculaExt.setTrailer(peliculaDTO.getTrailer());
            peliculaExt.setPosterImage(peliculaDTO.getPosterImage());
            peliculaExt.setScreenshot(peliculaDTO.getScreenshot());
            peliculaExt.setSynopsis(peliculaDTO.getSynopsis());
            peliculaExt.setRating(peliculaDTO.getRating());
            // Se guardan los datos en la base de datos

            repository.save(peliculaExt);

            return Mapper.mapToDTO(peliculaExt);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID no válido: " + id);
        }
    }


    /**
     * Elimina un  resgisto de pelicula
     */
    public void delete(String id) {
        Long idL;
        try {
            idL = Long.parseLong(id);
            if (repository.existsById(idL)) {
                repository.deleteById(idL);
            } else {
                throw new EntityNotFoundException("La película con ID " + id + " no existe.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID no válido: " + id);
        }
    }

    /**
     * Get All
     *
     * @return Lista de todas las películas registradas
     */
    public List<PeliculaDTO> getAll() {
        List<Pelicula> listaPeliculas = repository.findAll();
        List<PeliculaDTO> listaPeliculaDTO = new ArrayList<>();

        for (Pelicula pelicula : listaPeliculas) {
            listaPeliculaDTO.add(new PeliculaDTO());
        }
        return listaPeliculaDTO;
    }
}