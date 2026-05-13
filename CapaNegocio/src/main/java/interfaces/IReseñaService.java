/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import dtos.ResenaDTO;
import java.util.List;

/**
 *
 * @author golea
 */
public interface IReseñaService {

    void publicarResena(ResenaDTO resenaDTO) throws Exception;

    List<ResenaDTO> obtenerResenasPorPelicula(int idPelicula);

    List<ResenaDTO> obtenerResenasPorUsuario(int idUsuario);
}
