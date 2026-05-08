/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.capanegocio;

import dtos.PeliculasDTO;
import dtos.PlaylistDTO;
import dtos.UsuarioDTO;
import interfaces.IPeliculaService;
import interfaces.IPlaylistService;
import interfaces.IUsuarioService;
import java.util.ArrayList;
import java.util.List;
import servicios.PeliculaService;
import servicios.PlaylistService;
import servicios.UsuarioService;

/**
 *
 * @author golea
 */
public class PeliculasPruebametodos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // 1. Instanciar Servicios
        IPeliculaService peliculaService = new PeliculaService();
        IUsuarioService usuarioService = new UsuarioService();
        IPlaylistService playlistService = new PlaylistService();

        try {
            System.out.println("=== INICIANDO PRUEBA DE FLUJO COMPLETO ===");

            // --- PASO 1: AGREGAR 7 PELÍCULAS DESDE LA API ---
            // Usamos IDs reales de TMDB para evitar duplicados
            int[] idsTMDB = {272, 157336, 155, 299536, 550, 603, 11};
            System.out.println("Cargando 7 películas desde TMDB...");

            for (int id : idsTMDB) {
                peliculaService.sincronizarApi(id); // Este método usa buscarPeliculaDetalle internamente[cite: 1]
            }
            System.out.println("✅ Películas agregadas a la base de datos.");

            // --- PASO 2: BUSCAR UNA DE ELLAS ---
            List<PeliculasDTO> catalogo = peliculaService.catalogoPeliculas();
            if (!catalogo.isEmpty()) {
                PeliculasDTO encontrada = peliculaService.buscarPelicula(catalogo.get(3).getIdPelicula());
                System.out.println("✅ Película encontrada: " + encontrada.getTitulo());
            }

            // --- PASO 3: REGISTRAR USUARIO PARA LA PLAYLIST ---
            UsuarioDTO u = new UsuarioDTO();
            u.setNombre("Tester");
            u.setEmail("test_playlist@cine.com");
            u.setPassword("123");
            usuarioService.registrar(u);
            UsuarioDTO session = usuarioService.iniciarSesion("test_playlist@cine.com", "123");

            // --- PASO 4: CREAR Y GUARDAR PLAYLIST ---
            PlaylistDTO nuevaLista = new PlaylistDTO();
            nuevaLista.setNombre("Mis Favoritas de Prueba");
            nuevaLista.setUsuario(session);
            nuevaLista.setEsPublica(true);

            // Agregamos las primeras 3 películas del catálogo a la lista
            List<PeliculasDTO> listaPelis = new ArrayList<>();
            listaPelis.add(catalogo.get(0));
            listaPelis.add(catalogo.get(1));
            listaPelis.add(catalogo.get(2));
            nuevaLista.setPeliculas(listaPelis);

            playlistService.crearPlaylist(nuevaLista);
            System.out.println("✅ Playlist creada y guardada en la base de datos.");

            // --- PASO 5: MOSTRAR LA PLAYLIST ---
            System.out.println("\n=== MOSTRANDO TUS PLAYLISTS ===");
            List<PlaylistDTO> misListas = playlistService.verPlaylistsUsuario(session.getId());
            for (PlaylistDTO pl : misListas) {
                System.out.println("Lista: " + pl.getNombre());
                if (pl.getPeliculas() != null) {
                    pl.getPeliculas().forEach(p -> System.out.println("   - " + p.getTitulo()));
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Error en el proceso: " + e.getMessage());
        }

        System.out.println("=== FIN DE LA PRUEBA ===");
    }

}
