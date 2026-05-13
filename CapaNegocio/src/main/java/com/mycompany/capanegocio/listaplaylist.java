/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.capanegocio;

import Daos.PeliculaDAO;
import Daos.PlaylistDAO;
import Daos.UsuarioDAO;
import Entidades.Pelicula;
import Entidades.Playlist;
import Entidades.Usuario;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author golea
 */
public class listaplaylist {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic 
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        PeliculaDAO peliculaDAO = new PeliculaDAO();
        PlaylistDAO playlistDAO = new PlaylistDAO();

        try {
            String emailPrueba = "prueba2025@correo.com";

            // 1. Verificar si el usuario existe antes de intentar crearlo
            Usuario usuario = usuarioDAO.buscarPorEmail(emailPrueba);

            if (usuario == null) {
                usuario = new Usuario();
                usuario.setNombre("Usuario Prueba 2025");
                usuario.setEmail(emailPrueba);
                usuario.setPassword("segura123");
                usuario.setDate(new Date());
                usuarioDAO.guardarUsuario(usuario);
                System.out.println("Nuevo usuario creado.");
            } else {
                System.out.println("El usuario ya existía, usando usuario ID: " + usuario.getId());
            }

            // 2. Crear las películas (Usa IDs de TMDB aleatorios para evitar el mismo error con películas)
            List<Pelicula> peliculasParaPlaylist = new ArrayList<>();
            for (int i = 1; i <= 3; i++) {
                Pelicula peli = new Pelicula();
                peli.setTitulo("Película Prueba " + i);
                peli.setTmdbId((int) (Math.random() * 1000000)); // ID aleatorio para evitar duplicados
                peli.setSinopsis("Sinopsis...");
                peli.setRatingPromedio(8.0f);
                peli.setFechaEstreno(new Date());

                peliculaDAO.crearPelicula(peli);
                peliculasParaPlaylist.add(peli);
            }

            // 3. Crear Playlist
            Playlist playlist = new Playlist();
            playlist.setNombre("listaprueba2025");
            playlist.setEsPublica(true);
            playlist.setUsuario(usuario);
            playlist.setPeliculas(peliculasParaPlaylist);

            playlistDAO.insertar(playlist);
            System.out.println("Playlist 'listaprueba2025' creada exitosamente.");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
