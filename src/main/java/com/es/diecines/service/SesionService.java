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
import java.util.ArrayList;
import java.util.List;

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
     *                  Debe contener el ID de la película (`movieId`), el identificador de la sala (`roomId`) y la fecha de la sesión (`date`).
     * @return SesionDTO con la información de la sesión recién creada.
     * @throws BaseDeDatosException     si ocurre un error al acceder a la base de datos
     *                                  o si la película especificada en `movieId` no existe.
     * @throws IllegalArgumentException si el objeto `sesionDTO` es nulo.
     */
    public SesionDTO insert(SesionDTO sesionDTO) {
        if (sesionDTO == null) {
            throw new BaseDeDatosException("Error al crear la sesión: película no encontrada");
        }

        try {

            Pelicula pelicula = peliculaRepository
                    .findById(sesionDTO.getMovieId())
                    .orElseThrow(() -> new EntityNotFoundException("La película con ID " + sesionDTO.getMovieId() + " no existe."));

            Sesion sesion = Mapper.DtoToEntity(sesionDTO, pelicula);
            sesionRepository.save(sesion);

            return Mapper.entityToDTO(sesion);

        } catch (Exception e) {
            throw new BaseDeDatosException("Error inesperado al crear la sesión en la base de datos", e);
        }
    }

    /**
     * READ
     * Obtiene una sesión por su ID.
     *
     * @param id Identificador de la sesión en formato String.
     * @return SesionDTO con la información de la sesión encontrada.
     * @throws BaseDeDatosException     si ocurre un error al buscar en la base de datos.
     * @throws IllegalArgumentException si el ID tiene un formato inválido.
     * @throws EntityNotFoundException  si no se encuentra la sesión con el ID proporcionado.
     */
    public SesionDTO getById(String id) {
        try {
            Long idL = Long.parseLong(id);
            Sesion sesion = sesionRepository
                    .findById(idL)
                    .orElseThrow(() -> new EntityNotFoundException("La sesión con id " + id + " no existe."));

            return Mapper.entityToDTO(sesion);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID no válido: " + id, e);
        } catch (EntityNotFoundException e) {
            throw new BaseDeDatosException("Error al buscar en la base de datos: Sesión no encontrada", e);
        } catch (Exception e) {
            throw new BaseDeDatosException("Error inesperado al buscar la sesión en la base de datos", e);
        }
    }


    /**
     * UPDATE
     * Modifica un registro existente de una sesión.
     *
     * @param id        Identificador de la sesión que se va a modificar.
     * @param sesionDTO Objeto DTO con los nuevos datos de la sesión.
     *                  Debe contener el ID de la película (`movieId`), el identificador de la sala (`roomId`) y la fecha de la sesión (`date`).
     * @return SesionDTO con la información de la sesión modificada.
     * @throws BaseDeDatosException     si ocurre un error al acceder a la base de datos,
     *                                  si la sesión o película especificada no existe, o si se produce un error inesperado.
     * @throws IllegalArgumentException si el formato del ID es inválido o si el objeto `sesionDTO` es nulo.
     */
    public SesionDTO modify(String id, SesionDTO sesionDTO) {
        if (sesionDTO == null) {
            throw new BaseDeDatosException("Error al crear la sesión: película no encontrada");
        }
        try {
            Long idL = Long.parseLong(id);
            Pelicula pelicula = peliculaRepository
                    .findById(sesionDTO.getMovieId())
                    .orElseThrow(() -> new EntityNotFoundException("La sesión con ID " + sesionDTO.getMovieId() + " no existe."));

            Sesion sesionExt = sesionRepository
                    .findById(idL)
                    .orElseThrow(() -> new EntityNotFoundException("La sesión con id " + id + " no se ha encontrado."));

            sesionExt.setPelicula(pelicula);
            sesionExt.setRoomId(sesionDTO.getRoomId());
            sesionExt.setDate(sesionDTO.getDate());

            sesionRepository.save(sesionExt);

            return Mapper.entityToDTO(sesionExt);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID no válido: " + id, e);
        } catch (EntityNotFoundException e) {
            throw new BaseDeDatosException("Error al actualizar en la base de datos: Sesión o película no encontrada", e);
        } catch (Exception e) {
            throw new BaseDeDatosException("Error inesperado al actualizar la sesión en la base de datos", e);
        }
    }

    /**
     * DELETE
     * Elimina un registro existente de una sesión.
     *
     * @param id Identificador de la sesión que se desea eliminar.
     * @throws BaseDeDatosException     si ocurre un error al acceder a la base de datos
     *                                  o si se produce un error inesperado durante la operación.
     * @throws IllegalArgumentException si el formato del ID es inválido.
     * @throws EntityNotFoundException  si la sesión con el ID proporcionado no existe en la base de datos.
     */
    public void delete(String id) {

        try {
            Long idL = Long.parseLong(id);
            if (sesionRepository.existsById(idL)) {
                sesionRepository.deleteById(idL);
            } else {
                throw new EntityNotFoundException("La sesión con id " + id + " no encontrada..");
            }

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID no válido: " + id);
        } catch (Exception e) {
            throw new BaseDeDatosException("Error al eliminar la sesión de la base de datos.");
        }
    }

    /**
     * READ ALL
     * Obtiene la lista completa de todas las sesiones registradas en la base de datos.
     *
     * @return Lista de objetos SesionDTO que representan todas las sesiones registradas.
     * Si no hay sesiones registradas, se devuelve una lista vacía.
     * @throws BaseDeDatosException si ocurre un error inesperado al acceder a la base de datos.
     */
    public List<SesionDTO> getAll() {
        try {
            List<Sesion> listaSesiones = sesionRepository.findAll();
            List<SesionDTO> listaSesionesDTO = new ArrayList<>();

            listaSesiones.forEach(sesion -> listaSesionesDTO.add(Mapper.entityToDTO(sesion)));

            return listaSesionesDTO;
        } catch (Exception e) {
            throw new BaseDeDatosException("Error al obtener la lista de sesiones", e);
        }
    }

}
