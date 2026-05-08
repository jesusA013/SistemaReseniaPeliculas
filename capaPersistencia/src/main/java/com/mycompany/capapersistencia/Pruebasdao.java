/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.capapersistencia;

import Entidades.Actor;
import Entidades.Pelicula;
import Entidades.Playlist;
import Entidades.Usuario;
import IntegracionApi2.ActorTMDB;
import IntegracionApi2.TMDBApiAdapter;
import IntegracionApi2.TMDBResponse;
import dao.ActorDAO;
import dao.PeliculaDAO;
import dao.PlaylistDAO;
import dao.UsuarioDAO;
import java.util.List;

/**
 *
 * @author golea
 */
public class Pruebasdao {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        PeliculaDAO peliculaDAO = new PeliculaDAO();
        TMDBApiAdapter api = new TMDBApiAdapter();

        System.out.println("--- INICIANDO PRUEBAS DE DAOS ---");

        try {
            // 1. PRUEBA DE USUARIO
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre("Goleath");
            nuevoUsuario.setEmail("goleath@example.com");
            nuevoUsuario.setPassword("password123");

            usuarioDAO.guardarUsuario(nuevoUsuario);
            System.out.println("✅ UsuarioDAO: Usuario guardado con éxito.");

            Usuario buscado = usuarioDAO.buscarPorEmail("goleath@example.com");
            System.out.println("✅ UsuarioDAO: Busqueda por email exitosa: " + buscado.getNombre());

            // 2. PRUEBA DE PELÍCULA E INTEGRACIÓN
            // Buscamos una película en la API (ej: El Padrino ID: 238)
            TMDBResponse datosPeli = api.buscarPeliculaDetalle(238);
            if (datosPeli != null) {
                Pelicula peli = new Pelicula();
                peli.setTmdbId(datosPeli.id);
                peli.setTitulo(datosPeli.title);
                peli.setSinopsis(datosPeli.overview);
                peli.setRatingPromedio((float) datosPeli.vote_average);

                peliculaDAO.crearPelicula(peli);
                System.out.println("✅ PeliculaDAO: Película '" + peli.getTitulo() + "' persistida.");
            }

            // 3. PRUEBA DE LISTAR PELÍCULAS
            List<Pelicula> catalogo = peliculaDAO.listarPelicula();
            System.out.println("✅ PeliculaDAO: Total de películas en BD: " + catalogo.size());

            System.out.println("\n--- PRUEBA DE PLAYLIST ---");

// 1. Necesitamos un usuario y películas que ya existan en la BD
            Usuario usuarioOwner = usuarioDAO.buscarPorEmail("goleath@example.com");
            List<Pelicula> todasLasPelis = peliculaDAO.listarPelicula();

            if (usuarioOwner != null && todasLasPelis.size() >= 3) {
                // 2. Crear el objeto Playlist
                Playlist miLista = new Playlist();
                miLista.setNombre("Mis Favoritas de Prueba");
                miLista.setUsuario(usuarioOwner);
                miLista.setEsPublica(true);

                // 3. Agregar 3 películas de la base de datos a la lista
                miLista.setPeliculas(todasLasPelis.subList(0, 3));

                // 4. Persistir la Playlist usando el DAO
                PlaylistDAO playlistDAO = new PlaylistDAO();
                playlistDAO.insertar(miLista);

                System.out.println("✅ PlaylistDAO: Lista '" + miLista.getNombre() + "' creada con 3 películas.");
            } else {
                System.out.println("⚠️ No se pudo crear la playlist: Asegúrate de tener al menos 3 películas en la BD.");
            }

        } catch (Exception e) {
            System.err.println("❌ ERROR EN LAS PRUEBAS: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("--- PRUEBAS FINALIZADAS ---");
    }

}
