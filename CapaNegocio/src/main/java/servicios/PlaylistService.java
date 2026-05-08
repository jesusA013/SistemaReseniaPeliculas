/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import Daos.PlaylistDAO;
import Daos.UsuarioDAO;
import Entidades.Playlist;
import Entidades.Usuario;
import dtos.PlaylistDTO;
import interfaces.IPlaylistService;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author golea
 */
public class PlaylistService implements IPlaylistService {

    private PlaylistDAO playlistDAO = new PlaylistDAO();

    private UsuarioDAO usuarioDAO = new UsuarioDAO(); 

    @Override
    public void crearPlaylist(PlaylistDTO dto) throws Exception {
        if (dto.getNombre() == null || dto.getNombre().isEmpty()) {
            throw new Exception("El nombre es obligatorio.");
        }

        Playlist entidad = new Playlist();
        entidad.setNombre(dto.getNombre());
        entidad.setEsPublica(dto.isEsPublica());

        Usuario usuarioReal = usuarioDAO.buscarPorEmail(dto.getUsuario().getEmail());

        if (usuarioReal == null) {
            throw new Exception("El usuario dueño de la lista no existe.");
        }
        entidad.setUsuario(usuarioReal);

        playlistDAO.insertar(entidad);
    }

    @Override
    public List<PlaylistDTO> verPlaylistsUsuario(int idUsuario) {
        List<Playlist> entidades = playlistDAO.obtenerPorUsuario(idUsuario);
        List<PlaylistDTO> dtos = new ArrayList<>();

        for (Playlist e : entidades) {
            PlaylistDTO dto = new PlaylistDTO();
            dto.setNombre(e.getNombre());
            dtos.add(dto);
        }
        return dtos;
    }
}
