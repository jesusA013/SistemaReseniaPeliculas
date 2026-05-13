/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.capadominio;

import Daos.ActorDAO;
import Entidades.Actor;
import IntegracionApi.ActorTMDB;
import IntegracionApi.TMDBApiAdapter;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author golea
 */
public class pruebasDaoactores2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        TMDBApiAdapter apiAdapter = new TMDBApiAdapter();
        ActorDAO actorDAO = new ActorDAO();

        // Lista de 10 actores para buscar
        List<String> nombresActores = Arrays.asList(
                "Cillian Murphy", "Robert Downey Jr.", "Scarlett Johansson",
                "Margot Robbie", "Tom Hanks", "Meryl Streep",
                "Leonardo DiCaprio", "Brad Pitt", "Zendaya", "Joaquin Phoenix"
        );

        System.out.println("--- Iniciando carga de actores ---");

        for (String nombre : nombresActores) {
            try {
                // 1. Buscar el actor en TMDB para obtener su ID básico
                ActorTMDB busqueda = apiAdapter.buscarActor(nombre);

                if (busqueda != null) {
                    // 2. Obtener el detalle completo (para traer la biografía y profile_path)
                    ActorTMDB detalle = apiAdapter.obtenerDetalleActor(busqueda.id);

                    if (detalle != null) {
                        // 3. Crear el objeto de nuestra Entidad
                        Actor actorEntidad = new Actor();
                        actorEntidad.setNombre(detalle.name);
                        actorEntidad.setTmdbId(detalle.id);
                        actorEntidad.setBiografia(detalle.biography);
                        actorEntidad.setRutaPerfil(detalle.profile_path);
                        // 4. Guardar en la base de datos a través del DAO
                        actorDAO.guardar(actorEntidad);

                        System.out.println("Guardado con éxito: " + detalle.name);
                    }
                } else {
                    System.out.println("No se encontró información para: " + nombre);
                }
            } catch (Exception e) {
                System.err.println("Error procesando a " + nombre + ": " + e.getMessage());
            }
        }

        System.out.println("--- Proceso finalizado ---");
    }
}
