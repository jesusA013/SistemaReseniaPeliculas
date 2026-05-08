/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import dtos.PeliculasDTO;
import java.util.List;

/**
 *
 * @author golea
 */
public interface IPeliculaService {
    List<PeliculasDTO> catalogoPeliculas();
    PeliculasDTO buscarPelicula(int id);
    List<PeliculasDTO> obtenerUltimasPeliculas();
    void sincronizarApi(int tmdbId);
    }
