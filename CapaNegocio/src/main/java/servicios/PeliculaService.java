/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import Daos.PeliculaDAO;
import Entidades.Pelicula;
import IntegracionApi.ActorTMDB;
import dtos.PeliculasDTO;
import interfaces.IPeliculaService;
import java.util.ArrayList;
import java.util.List;
import IntegracionApi.TMDBApiAdapter;
import dtos.ActorDTO;

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
    public List<PeliculasDTO> obtenerUltimasPeliculas() {
        List<Pelicula> entidades = peliculaDAO.listarPeliculasRecientes(4);
        List<PeliculasDTO> dtos = new ArrayList<>();

        for (Pelicula e : entidades) {
            PeliculasDTO dto = new PeliculasDTO();
            dto.setIdPelicula(e.getId());
            dto.setTmdbId(e.getTmdbId());
            dto.setTitulo(e.getTitulo());
            dto.setSinopsis(e.getSinopsis());
            dto.setRatingPromedio(e.getRatingPromedio());
            dto.setPosterPath(e.getPosterPath());
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public PeliculasDTO buscarOPersistirPelicula(String titulo) {
        Pelicula entidad = peliculaDAO.buscarPorTitulo(titulo);

        if (entidad == null) {
            IntegracionApi.TMDBResponse apiRes = apiAdapter.buscarPeliculaPorNombre(titulo);
            if (apiRes != null) {
                sincronizarApi(apiRes.id);
                entidad = peliculaDAO.buscarPorTitulo(apiRes.title);
            }
        }

        if (entidad != null) {
            PeliculasDTO dto = new PeliculasDTO();
            dto.setIdPelicula(entidad.getId());
            dto.setTitulo(entidad.getTitulo());
            dto.setSinopsis(entidad.getSinopsis());
            dto.setRatingPromedio(entidad.getRatingPromedio());
            dto.setPosterPath(entidad.getPosterPath());
            dto.setFechaEstreno(entidad.getFechaEstreno());
            if (entidad.getListaActores() != null) {
                List<ActorDTO> actoresDto = new ArrayList<>();
                for (Entidades.Actor actorEntidad : entidad.getListaActores()) {
                    ActorDTO aDto = new ActorDTO();
                    aDto.setNombre(actorEntidad.getNombre());
                    aDto.setRutaPerfil(actorEntidad.getRutaPerfil());
                    actoresDto.add(aDto);
                }
                dto.setListaActores(actoresDto);
            }
            return dto;
        }
        return null;
    }

    @Override
    public void sincronizarApi(int tmdbId) {
        IntegracionApi.TMDBResponse peliculaApi = apiAdapter.buscarPeliculaDetalle(tmdbId);
        Entidades.Pelicula existente = peliculaDAO.buscarPorTmdbId(tmdbId);

        if (existente != null) {
            System.out.println("⚠️ La película con TMDB ID " + tmdbId + " ya existe. Evitando duplicado.");
            return; // Salimos del método para no intentar crearla otra vez
        }

        if (peliculaApi != null) {
            Entidades.Pelicula entidad = new Entidades.Pelicula();
            entidad.setTmdbId(peliculaApi.id);
            entidad.setTitulo(peliculaApi.title);
            entidad.setSinopsis(peliculaApi.overview);
            entidad.setRatingPromedio((float) peliculaApi.vote_average);
            entidad.setPosterPath(peliculaApi.poster_path);
            entidad.setFechaEstreno(apiAdapter.parseFecha(peliculaApi.release_date));

            List<IntegracionApi.ActorTMDB> actoresApi = apiAdapter.obtenerActoresDePelicula(tmdbId);
            List<Entidades.Actor> listaActoresParaPeli = new ArrayList<>();

            if (actoresApi != null) {
                int limite = Math.min(actoresApi.size(), 4);

                for (int i = 0; i < limite; i++) {
                    IntegracionApi.ActorTMDB actorApi = actoresApi.get(i);

                    // 1. Verificar si el actor ya está en nuestra base de datos
                    Entidades.Actor actorEntidad = actorDAO.buscarPorTmdbId(actorApi.id);

                    if (actorEntidad == null) {
                        // 2. Si no existe, pausamos brevemente para no saturar la API
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }

                        // 3. Obtenemos el detalle completo (biografía)
                        IntegracionApi.ActorTMDB detalleCompleto = apiAdapter.obtenerDetalleActor(actorApi.id);

                        actorEntidad = new Entidades.Actor();
                        actorEntidad.setTmdbId(actorApi.id);
                        actorEntidad.setNombre(actorApi.name);
                        actorEntidad.setRutaPerfil(actorApi.profile_path);

                        String biografiaFinal = (detalleCompleto != null && detalleCompleto.biography != null && !detalleCompleto.biography.trim().isEmpty())
                                ? detalleCompleto.biography
                                : "Biografía no disponible.";
                        actorEntidad.setBiografia(biografiaFinal);

                        // 4. Guardamos usando el método inteligente del DAO
                        actorDAO.guardar(actorEntidad);

                        // 5. Recuperamos la instancia persistida para asegurar que tenga el ID de la BD
                        actorEntidad = actorDAO.buscarPorTmdbId(actorApi.id);
                    }

                    if (actorEntidad != null) {
                        listaActoresParaPeli.add(actorEntidad);
                    }
                }

            }
            entidad.setListaActores(listaActoresParaPeli);
            peliculaDAO.crearPelicula(entidad);
        }
    }

    private Daos.ActorDAO actorDAO = new Daos.ActorDAO();

    public List<ActorDTO> obtenerActoresRecientes() {
        List<Entidades.Actor> entidades = actorDAO.listarUltimosActores(6);
        List<ActorDTO> dtos = new ArrayList<>();

        for (Entidades.Actor e : entidades) {
            ActorDTO dto = new ActorDTO();
            dto.setId(e.getId());
            dto.setNombre(e.getNombre());
            dto.setBiografia(e.getBiografia());
            dto.setRutaPerfil(e.getRutaPerfil());
            dtos.add(dto);
        }
        return dtos;
    }

}
