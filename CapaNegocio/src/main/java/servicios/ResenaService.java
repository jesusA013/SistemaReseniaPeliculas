/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import Daos.ResenaDAO;
import Entidades.Pelicula;
import Entidades.Resena;
import Entidades.Usuario;
import dtos.PeliculasDTO;
import dtos.ResenaDTO;
import dtos.UsuarioDTO;
import interfaces.IReseñaService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author golea
 */
public class ResenaService implements IReseñaService {

    private ResenaDAO resenaDAO = new ResenaDAO();

    @Override
    public void publicarResena(ResenaDTO dto) throws Exception {
        // 1. Validaciones de negocio
        if (dto.getCalificacion() < 1 || dto.getCalificacion() > 10) {
            throw new Exception("La calificación debe estar entre 1 y 10.");
        }
        if (dto.getComentario() == null || dto.getComentario().trim().length() < 5) {
            throw new Exception("El comentario es demasiado corto (mínimo 5 caracteres).");
        }
        if (dto.getUsuario() == null || dto.getPelicula() == null) {
            throw new Exception("La reseña debe estar asociada a un usuario y a una película.");
        }

        // 2. Creación de la entidad Resena
        Resena entidad = new Resena();
        entidad.setComentario(dto.getComentario());
        entidad.setCalificacion(dto.getCalificacion());

        // Convertir LocalDateTime de DTO a Date de Entidad si es necesario, 
        // o usar la fecha actual del sistema.
        entidad.setFecha(new Date());

        // 3. Mapeo de Usuario (DTO a Entidad)
        Usuario usuario = new Usuario();
        usuario.setId(dto.getUsuario().getId()); // Solo necesitamos el ID para el merge en el DAO
        entidad.setUsuario(usuario);

        // 4. Mapeo de Pelicula (DTO a Entidad)
        Pelicula pelicula = new Pelicula();
        pelicula.setId(dto.getPelicula().getIdPelicula()); // Solo necesitamos el ID para el merge en el DAO
        entidad.setPelicula(pelicula);

        // 5. Persistencia
        // Verificamos si el usuario ya calificó esta película para actualizar o insertar
        Resena existente = resenaDAO.buscarResenaUsuarioPelicula(usuario.getId(), pelicula.getId());

        if (existente != null) {
            // Si ya existe, actualizamos los datos de la entidad encontrada
            existente.setComentario(entidad.getComentario());
            existente.setCalificacion(entidad.getCalificacion());
            existente.setFecha(entidad.getFecha());
            resenaDAO.actualizarResena(existente);
        } else {
            // Si es nueva, usamos el método insertar
            resenaDAO.insertarResena(entidad);
        }
    }

    @Override
    public List<ResenaDTO> obtenerResenasPorPelicula(int idPelicula) {
        List<Resena> entidades = resenaDAO.buscarPorPelicula(idPelicula);
        List<ResenaDTO> dtos = new ArrayList<>();

        for (Resena r : entidades) {
            ResenaDTO dto = new ResenaDTO();
            dto.setComentario(r.getComentario());
            dto.setCalificacion(r.getCalificacion());

            UsuarioDTO u = new UsuarioDTO();
            u.setNombre(r.getUsuario().getNombre());
            dto.setUsuario(u);

            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<ResenaDTO> obtenerResenasPorUsuario(int idUsuario) {
        // Usamos el método que agregamos al DAO anteriormente
        List<Resena> entidades = resenaDAO.buscarPorUsuario(idUsuario);
        List<ResenaDTO> dtos = new ArrayList<>();

        for (Resena r : entidades) {
            ResenaDTO dto = new ResenaDTO();
            dto.setId(r.getId());
            dto.setComentario(r.getComentario());
            dto.setCalificacion(r.getCalificacion());

            // Cargar datos de la película para la tabla
            PeliculasDTO pDto = new PeliculasDTO();
            pDto.setTitulo(r.getPelicula().getTitulo());
            pDto.setPosterPath(r.getPelicula().getPosterPath());
            dto.setPelicula(pDto);

            dtos.add(dto);
        }
        return dtos;
    }
}
