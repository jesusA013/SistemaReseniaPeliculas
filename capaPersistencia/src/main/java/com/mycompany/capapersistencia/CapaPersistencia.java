/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.capapersistencia;

import Entidades.Actor;
import Entidades.Pelicula;
import Entidades.Resena;
import Entidades.Usuario;
import IntegracionApi.ActorTMDB;
import IntegracionApi.TMDBApiAdapter;
import IntegracionApi.TMDBResponse;
import com.google.gson.Gson;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Jesus
 */
public class CapaPersistencia {

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
    }
}
