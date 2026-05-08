/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import Daos.PeliculaDAO;
import Entidades.Pelicula;
import dtos.PeliculasDTO;
import interfaces.IPeliculaService;
import java.util.ArrayList;
import java.util.List;
import IntegracionApi.TMDBApiAdapter;

/**
 *
 * @author golea
 */
public class PeliculaService implements IPeliculaService {

    private TMDBApiAdapter apiAdapter = new TMDBApiAdapter();
    private PeliculaDAO peliculaDAO = new PeliculaDAO();

    @Override
    public List<PeliculasDTO> catalogoPeliculas() {
        List<Pelicula> entidades = peliculaDAO.listarPelicula();
        List<PeliculasDTO> dtos = new ArrayList<>();

        for (Pelicula e : entidades) {
            PeliculasDTO dto = new PeliculasDTO();
            dto.setIdPelicula(e.getId());
            dto.setTmdbId(e.getTmdbId());
            dto.setTitulo(e.getTitulo());
            dto.setSinopsis(e.getSinopsis());
            dto.setRatingPromedio(e.getRatingPromedio());
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public PeliculasDTO buscarPelicula(int id) {
        List<Pelicula> todas = peliculaDAO.listarPelicula();

        Pelicula entidadEncontrada = todas.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);

        if (entidadEncontrada == null) {
            System.out.println("⚠️ Película con ID " + id + " no encontrada en la base de datos.");
            return null;
        }

        PeliculasDTO dto = new PeliculasDTO();
        dto.setIdPelicula(entidadEncontrada.getId());
        dto.setTitulo(entidadEncontrada.getTitulo());
        dto.setTmdbId(entidadEncontrada.getTmdbId());
        dto.setSinopsis(entidadEncontrada.getSinopsis());
        dto.setRatingPromedio(entidadEncontrada.getRatingPromedio());

        dto.setFechaEstreno(entidadEncontrada.getFechaEstreno());

        return dto;
    }

    @Override
    public void sincronizarApi(int tmdbId) {
        TMDBApiAdapter apiAdapter = new TMDBApiAdapter();

        IntegracionApi.TMDBResponse infoApi = apiAdapter.buscarPeliculaDetalle(tmdbId);

        if (infoApi != null) {
            Pelicula entidad = new Pelicula();

            entidad.setTmdbId(infoApi.id);
            entidad.setTitulo(infoApi.title);
            entidad.setSinopsis(infoApi.overview);
            entidad.setRatingPromedio((float) infoApi.vote_average);

            peliculaDAO.crearPelicula(entidad);
            System.out.println("✅ Sincronizada: " + infoApi.title);
        } else {
            System.err.println("❌ No se encontró la película en TMDB con ID: " + tmdbId);
        }
    }
}
