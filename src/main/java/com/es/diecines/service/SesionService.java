package com.es.diecines.service;

import com.es.diecines.dto.SesionDTO;
import com.es.diecines.error.BaseDeDatosException;
import com.es.diecines.model.Pelicula;
import com.es.diecines.model.Sesion;
import com.es.diecines.repository.PeliculaRepository;
import com.es.diecines.repository.SesionRepository;
import com.es.diecines.utils.Mapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Servicio para gestionar las operaciones CRUD de la entidad Sesion.
 * <p>
 * Esta clase proporciona métodos para crear, leer, actualizar y eliminar registros de
 * sesiones en la base de datos. Utiliza el {@link SesionRepository} para interactuar
 * con la base de datos y el {@link Mapper} para convertir entre entidades y DTOs.
 * </p>
 * <p>
 * El servicio también maneja excepciones personalizadas relacionadas con la base de datos
 * y el formato de los datos para asegurar que el cliente reciba mensajes de error claros
 * y consistentes.
 * </p>
 *
 * @see SesionDTO
 * @see Sesion
 * @see Pelicula
 * @see SesionRepository
 * @see Mapper
 */
@Service
public class SesionService {

    @Autowired
    private SesionRepository sesionRepository;

    @Autowired
    private PeliculaRepository peliculaRepository;

    /**
     * CREATE
     * Crea un nuevo registro en la entidad Sesion.
     *
     * @param sesionDTO Nueva sesión en formato DTO que se va a insertar.
     *                   Debe contener el ID de la película (`movieId`), el identificador de la sala (`roomId`) y la fecha de la sesión (`date`).
     * @return SesionDTO con la información de la sesión recién creada.
     * @throws BaseDeDatosException si ocurre un error al acceder a la base de datos
     *         o si la película especificada en `movieId` no existe.
     * @throws IllegalArgumentException si el objeto `sesionDTO` es nulo.
     */
    public SesionDTO insert(SesionDTO sesionDTO) {
        try {

            if (sesionDTO != null) {
                Pelicula pelicula = peliculaRepository
                        .findById(sesionDTO.getMovieId())
                        .orElseThrow(() -> new EntityNotFoundException("La película con ID " + sesionDTO.getMovieId() + " no existe."));

                Sesion sesion = Mapper.DtoToEntity(sesionDTO, pelicula);
                sesionRepository.save(sesion);

                return Mapper.entityToDTO(sesion);
            }

        } catch (EntityNotFoundException e) {
            throw new BaseDeDatosException("Error al crear la sesión: película no encontrada", e);
        } catch (Exception e) {
            throw new BaseDeDatosException("Error inesperado al crear la sesión en la base de datos", e);
        }
    }


}
