/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Daos;

import ConexionDB.ConexionDB;
import Entidades.Playlist;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author golea
 */
public class PlaylistDAO {

    public void insertar(Playlist playlist) {
        EntityManager em = ConexionDB.getInstancia().getEntityManager();
        try {
            em.getTransaction().begin();

            Playlist playlistGestionada = em.merge(playlist);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Playlist> obtenerPorUsuario(int idUsuario) {
        EntityManager em = ConexionDB.getInstancia().getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Playlist p WHERE p.usuario.id = :id", Playlist.class)
                    .setParameter("id", idUsuario)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void actualizar(Playlist playlist) {
        EntityManager em = ConexionDB.getInstancia().getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(playlist); // Actualiza la playlist y su relación con películas
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Playlist> obtenerTodasPublicas() {
        EntityManager em = ConexionDB.getInstancia().getEntityManager();
        try {
            // Usamos JOIN FETCH p.peliculas para traer las películas en una sola consulta
            // DISTINCT asegura que no se dupliquen las playlists en el resultado por el JOIN
            String jpql = "SELECT DISTINCT p FROM Playlist p "
                    + "LEFT JOIN FETCH p.peliculas "
                    + "WHERE p.esPublica = true";

            TypedQuery<Playlist> query = em.createQuery(jpql, Playlist.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener playlists públicas: " + e.getMessage());
            return new java.util.ArrayList<>();
        } finally {
            em.close();
        }
    }

    public List<Playlist> obtenerPrivadasPorUsuario(int idUsuario) {
        EntityManager em = ConexionDB.getInstancia().getEntityManager();
        return em.createQuery("SELECT DISTINCT p FROM Playlist p LEFT JOIN FETCH p.peliculas WHERE p.usuario.id = :id AND p.esPublica = false", Playlist.class)
                .setParameter("id", idUsuario)
                .getResultList();
    }

    public List<Playlist> obtenerTodasPorUsuario(int idUsuario) {
        EntityManager em = ConexionDB.getInstancia().getEntityManager();
        return em.createQuery("SELECT DISTINCT p FROM Playlist p LEFT JOIN FETCH p.peliculas WHERE p.usuario.id = :id", Playlist.class)
                .setParameter("id", idUsuario)
                .getResultList();
    }

}
