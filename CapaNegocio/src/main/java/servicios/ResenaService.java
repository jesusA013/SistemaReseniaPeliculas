/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import Daos.ResenaDAO;
import Entidades.Resena;
import dtos.ResenaDTO;
import interfaces.IReseñaService;

/**
 *
 * @author golea
 */
public class ResenaService implements IReseñaService{
    private ResenaDAO resenaDAO = new ResenaDAO();

    @Override
    public void publicarResena(ResenaDTO dto) throws Exception {
        if (dto.getCalificacion() < 1 || dto.getCalificacion() > 10) {
            throw new Exception("La calificación debe estar entre 1 y 10.");
        }
        if (dto.getComentario() == null || dto.getComentario().length() < 5) {
            throw new Exception("El comentario es demasiado corto.");
        }

        Resena entidad = new Resena();
        entidad.setComentario(dto.getComentario());
        entidad.setCalificacion(dto.getCalificacion());
        
        if (dto.getPelicula() != null && dto.getUsuario() != null) {
            
        }

        resenaDAO.insertarReseña(entidad);
    }
}
