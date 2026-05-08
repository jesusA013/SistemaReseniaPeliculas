/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Daos;

import ConexionDB.ConexionDB;
import Entidades.Pelicula;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author golea
 */
public class PeliculaDAO {

    public void crearPelicula(Pelicula pelicula) {
        EntityManager em = ConexionDB.getInstancia().getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pelicula);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void actualizarPelicula(Pelicula pelicula) {
        EntityManager em = ConexionDB.getInstancia().getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(pelicula);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Pelicula> listarPelicula() {
        EntityManager em = ConexionDB.getInstancia().getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Pelicula p", Pelicula.class)
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void eliminarPelicula(Pelicula pelicula) {
        EntityManager em = ConexionDB.getInstancia().getEntityManager();
        try {
            em.getTransaction().begin();
            Pelicula peliAEliminar = em.find(Pelicula.class, pelicula.getId());
            if (peliAEliminar != null) {
                em.remove(peliAEliminar);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Pelicula> listarPeliculasRecientes(int limite) {
        EntityManager em = ConexionDB.getInstancia().getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Pelicula p ORDER BY p.id DESC", Pelicula.class)
                    .setMaxResults(limite)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
