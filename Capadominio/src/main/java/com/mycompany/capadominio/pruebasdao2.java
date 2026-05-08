/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.capadominio;

import Daos.PeliculaDAO;
import Daos.UsuarioDAO;
import Entidades.Pelicula;
import Entidades.Usuario;
import IntegracionApi.TMDBApiAdapter;
import IntegracionApi.TMDBResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author golea
 */
public class pruebasdao2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParseException {
        // TODO code application logic here
        PeliculaDAO peliculaDAO = new PeliculaDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        TMDBApiAdapter apiAdapter = new TMDBApiAdapter();

        // 1. Agregar 3 películas buscando sus datos en TMDB
        String[] titulosBusqueda = {"Avengers", "Chainsaw Man", "The Lego Movie"};

        for (String titulo : titulosBusqueda) {
            System.out.println("Buscando y agregando: " + titulo);

            // Buscamos en la API
            List<TMDBResponse> resultados = apiAdapter.buscarPeliculasPorNombre(titulo);

            if (resultados != null && !resultados.isEmpty()) {
                TMDBResponse res = resultados.get(0);

                Pelicula p = new Pelicula();
                p.setTitulo(res.title);
                p.setTmdbId(res.id);
                p.setSinopsis(res.overview);
                p.setRatingPromedio((float) res.vote_average);
                p.setPosterPath(res.poster_path);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                p.setFechaEstreno(sdf.parse(res.release_date));
                peliculaDAO.crearPelicula(p);
                System.out.println("Guardada: " + p.getTitulo() + " (ID TMDB: " + p.getTmdbId() + ")");
            } else {
                System.out.println("No se encontró información para: " + titulo);
            }
        }

        System.out.println("\nCreando usuario de prueba...");
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre("Cinefilo99");
        nuevoUsuario.setEmail("contacto@ejemplo.com");
        nuevoUsuario.setPassword("password123");

        usuarioDAO.guardarUsuario(nuevoUsuario);
        System.out.println("Usuario agregado con éxito.");
    }
}
