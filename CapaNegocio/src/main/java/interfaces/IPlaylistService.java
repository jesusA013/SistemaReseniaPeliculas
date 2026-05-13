/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import dtos.PlaylistDTO;
import java.util.List;

/**
 *
 * @author golea
 */
public interface IPlaylistService {

    void crearPlaylist(PlaylistDTO playlistDTO) throws Exception;

    List<PlaylistDTO> verPlaylistsUsuario(int idUsuario);

    public void agregarPeliculaALista(int idPlaylist, int idPelicula) throws Exception;

    List<PlaylistDTO> obtenerPlaylistsPublicas() throws Exception;

    List<PlaylistDTO> obtenerPrivadasPorUsuario(int idUsuario) throws Exception;

    List<PlaylistDTO> obtenerTodasPorUsuario(int idUsuario) throws Exception;

}
