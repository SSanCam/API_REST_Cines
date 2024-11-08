package com.es.diecines.utils;

import com.es.diecines.dto.PeliculaDTO;
import com.es.diecines.model.Pelicula;
import com.es.diecines.repository.PeliculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Mapper {

    private static PeliculaRepository peliculaRepository;

    public static PeliculaDTO createPelicula(PeliculaDTO peliculaDTO) {
        Pelicula pelicula = new Pelicula();
        pelicula.setTitle(peliculaDTO.getTitle());
        pelicula.setDirector(peliculaDTO.getDirector());
        pelicula.setTime(peliculaDTO.getTime());
        pelicula.setTrailer(peliculaDTO.getTrailer());
        pelicula.setPosterImage(peliculaDTO.getPosterImage());
        pelicula.setScreenshot(peliculaDTO.getScreenshot());
        pelicula.setSynopsis(peliculaDTO.getSynopsis());
        pelicula.setRating(peliculaDTO.getRating());
        pelicula = peliculaRepository.save(pelicula);

        return mapToDTO(pelicula);
    }

    public static PeliculaDTO mapToDTO(Pelicula pelicula) {
        PeliculaDTO peliculaDTO = new PeliculaDTO();
        peliculaDTO.setId(pelicula.getId());
        peliculaDTO.setTitle(pelicula.getTitle());
        peliculaDTO.setDirector(pelicula.getDirector());
        peliculaDTO.setTime(pelicula.getTime());
        peliculaDTO.setTrailer(pelicula.getTrailer());
        peliculaDTO.setPosterImage(pelicula.getPosterImage());
        peliculaDTO.setScreenshot(pelicula.getScreenshot());
        peliculaDTO.setSynopsis(pelicula.getSynopsis());
        peliculaDTO.setRating(pelicula.getRating());
        return peliculaDTO;
    }
}
