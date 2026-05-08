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
}
