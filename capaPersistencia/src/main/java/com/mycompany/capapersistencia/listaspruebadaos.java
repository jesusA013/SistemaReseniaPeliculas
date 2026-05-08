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
import dao.PeliculaDAO;
import dao.PlaylistDAO;
import dao.UsuarioDAO;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author golea
 */
public class listaspruebadaos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TMDBApiAdapter api = new TMDBApiAdapter();
        TMDBResponse datosApi = api.buscarPeliculaDetalle(550);

        if (datosApi != null) {
            System.out.println("Película encontrada en API: " + datosApi.title);

            Pelicula peli = new Pelicula();
            peli.setTmdbId(datosApi.id);
            peli.setTitulo(datosApi.title);
            peli.setSinopsis(datosApi.overview);
            peli.setRatingPromedio((float) datosApi.vote_average);

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("CinemaPU");
            EntityManager em = emf.createEntityManager();

            em.getTransaction().begin();
            em.persist(peli);
            em.getTransaction().commit();

            System.out.println("Película de la API guardada en la base de datos.");
            em.close();
            emf.close();
        }

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CinemaPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        String[] nombres = {"Rio", "Barbie", "Chainsaw Man – The Movie: Reze Arc"};
        for (String nombre : nombres) {
            List<TMDBResponse> resultados = api.buscarPeliculasPorNombre(nombre);
            if (!resultados.isEmpty()) {
                TMDBResponse p = resultados.get(0);
                Pelicula entidadPeli = new Pelicula();
                entidadPeli.setTmdbId(p.id);
                entidadPeli.setTitulo(p.title);
                entidadPeli.setSinopsis(p.overview);
                entidadPeli.setRatingPromedio((float) p.vote_average);
                em.persist(entidadPeli);
                System.out.println("Guardada: " + p.title);
            }
        }

        // 2. Buscar y agregar al actor Robert Downey Jr.
        ActorTMDB actorApi = api.buscarActor("Robert Downey Jr.");

        if (actorApi != null) {
            ActorTMDB detalleCompleto = api.obtenerDetalleActor(actorApi.id);

            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }

            try {
                Actor entidadActor = new Actor();
                entidadActor.setTmdbId(detalleCompleto.id);
                entidadActor.setNombre(detalleCompleto.name);
                entidadActor.setBiografia(detalleCompleto.biography);

                em.persist(entidadActor);
                em.getTransaction().commit();
                System.out.println("✅ Guardado actor con biografía: " + detalleCompleto.name);
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                System.err.println("Error al persistir actor: " + e.getMessage());
            }
        }

        ////lista peliculas con prueba dqao
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        PeliculaDAO peliculaDAO = new PeliculaDAO();
        PlaylistDAO playlistDAO = new PlaylistDAO();

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre("Goleath");
        nuevoUsuario.setEmail("goleath@example.com");
        nuevoUsuario.setPassword("password123");

        usuarioDAO.guardarUsuario(nuevoUsuario);
        System.out.println("✅ UsuarioDAO: Usuario guardado con exito.");

        Usuario buscado = usuarioDAO.buscarPorEmail("goleath@example.com");
        System.out.println("✅ UsuarioDAO: Busqueda por email exitosa: " + buscado.getNombre());

        System.out.println("=== INICIANDO PRUEBA DE PLAYLIST ===");

        try {
            // 2. Obtener un usuario existente
            Usuario usuario = usuarioDAO.buscarPorEmail("goleath@example.com");
            if (usuario == null) {
                System.out.println("❌ Error: El usuario no existe. Crea uno primero.");
                return;
            }

            // 3. Obtener películas de la base de datos
            List<Pelicula> catalogo = peliculaDAO.listarPelicula();
            if (catalogo.size() < 3) {
                System.out.println("❌ Error: Necesitas al menos 3 películas en la BD. Ejecuta el Main anterior.");
                return;
            }

            // 4. Crear la nueva Playlist
            Playlist miLista = new Playlist();
            miLista.setNombre("Maratón de Fin de Semana");
            miLista.setUsuario(usuario);
            miLista.setEsPublica(true);

            // Seleccionamos las primeras 3 películas del catálogo
            List<Pelicula> seleccionadas = new ArrayList<>();
            seleccionadas.add(catalogo.get(0));
            seleccionadas.add(catalogo.get(1));
            seleccionadas.add(catalogo.get(2));

            miLista.setPeliculas(seleccionadas);

            // 5. Persistir
            System.out.println("Intentando guardar la playlist '" + miLista.getNombre() + "'...");
            playlistDAO.insertar(miLista);

            System.out.println("✅ ¡Éxito! Playlist guardada correctamente.");

            // 6. Verificación final: Listar playlists del usuario
            List<Playlist> misListas = playlistDAO.obtenerPorUsuario(usuario.getId());
            System.out.println("Listas actuales del usuario: " + misListas.size());
            for (Playlist p : misListas) {
                System.out.println("- " + p.getNombre() + " (" + p.getPeliculas().size() + " películas)");
            }

        } catch (Exception e) {
            System.err.println("❌ Fallo crítico en la prueba: " + e.getMessage());
            e.printStackTrace();
        }
    }
}