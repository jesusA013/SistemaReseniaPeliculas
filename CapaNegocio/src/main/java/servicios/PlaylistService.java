/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import ConexionDB.ConexionDB;
import Daos.PlaylistDAO;
import Daos.UsuarioDAO;
import Entidades.Pelicula;
import Entidades.Playlist;
import Entidades.Usuario;
import dtos.PeliculasDTO;
import dtos.PlaylistDTO;
import interfaces.IPlaylistService;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.swing.table.DefaultTableModel;

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
            dto.setId(e.getId());
            dto.setNombre(e.getNombre());
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public void agregarPeliculaALista(int idPlaylist, int idPelicula) throws Exception {
        EntityManager em = ConexionDB.getInstancia().getEntityManager();
        try {
            Playlist playlist = em.find(Playlist.class, idPlaylist);
            Pelicula pelicula = em.find(Pelicula.class, idPelicula);

            if (playlist != null && pelicula != null) {
                // Evitar duplicados en la lista
                if (!playlist.getPeliculas().contains(pelicula)) {
                    playlist.getPeliculas().add(pelicula);
                    playlistDAO.actualizar(playlist);
                } else {
                    throw new Exception("La película ya está en esta playlist.");
                }
            }
        } finally {
            em.close();
        }
    }

    @Override
    public List<PlaylistDTO> obtenerPlaylistsPublicas() throws Exception {
        // Necesitas añadir este método en PlaylistDAO o usar JPQL directo
        // SELECT p FROM Playlist p WHERE p.esPublica = true
        List<Playlist> entidades = playlistDAO.obtenerTodasPublicas();
        List<PlaylistDTO> dtos = new ArrayList<>();

        for (Playlist e : entidades) {
            PlaylistDTO dto = new PlaylistDTO();
            dto.setId(e.getId());
            dto.setNombre(e.getNombre());

            // Es vital cargar las películas de la entidad al DTO
            List<PeliculasDTO> peliDtos = new ArrayList<>();
            for (Pelicula p : e.getPeliculas()) {
                PeliculasDTO pDto = new PeliculasDTO();
                pDto.setTitulo(p.getTitulo());
                pDto.setPosterPath(p.getPosterPath());
                peliDtos.add(pDto);
            }
            dto.setPeliculas(peliDtos); // Asegúrate que PlaylistDTO tenga este campo
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<PlaylistDTO> obtenerPrivadasPorUsuario(int idUsuario) throws Exception {
        // Convertimos el int a Long si tu DAO así lo requiere, o ajustamos el DAO
        List<Playlist> entidades = playlistDAO.obtenerPrivadasPorUsuario(idUsuario);
        return convertirEntidadesADTOs(entidades);
    }

    @Override
    public List<PlaylistDTO> obtenerTodasPorUsuario(int idUsuario) throws Exception {
        List<Playlist> entidades = playlistDAO.obtenerTodasPorUsuario(idUsuario);
        return convertirEntidadesADTOs(entidades);
    }

    private List<PlaylistDTO> convertirEntidadesADTOs(List<Playlist> entidades) {
        List<PlaylistDTO> dtos = new ArrayList<>();
        for (Playlist e : entidades) {
            PlaylistDTO dto = new PlaylistDTO();
            dto.setId(e.getId());
            dto.setNombre(e.getNombre());
            dto.setEsPublica(e.isEsPublica());

            List<PeliculasDTO> peliDtos = new ArrayList<>();
            for (Pelicula p : e.getPeliculas()) {
                PeliculasDTO pDto = new PeliculasDTO();
                pDto.setTitulo(p.getTitulo());
                pDto.setPosterPath(p.getPosterPath());
                peliDtos.add(pDto);
            }
            dto.setPeliculas(peliDtos);
            dtos.add(dto);
        }
        return dtos;
    }

}
