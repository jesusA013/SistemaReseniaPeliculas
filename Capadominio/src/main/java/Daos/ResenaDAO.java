/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Daos;

import ConexionDB.ConexionDB;
import Entidades.Pelicula;
import Entidades.Resena;
import Entidades.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author golea
 */
public class ResenaDAO {

    /**
     * Inserta una nueva reseña en la base de datos.
     *
     * @param resena El objeto Resena a persistir.
     */
    public void insertarResena(Resena resena) {
        EntityManager em = ConexionDB.getInstancia().getEntityManager();
        try {
            em.getTransaction().begin();

            // Usamos getReference para no sobrescribir datos existentes con NULLs
            if (resena.getUsuario() != null) {
                resena.setUsuario(em.getReference(Usuario.class, resena.getUsuario().getId()));
            }
            if (resena.getPelicula() != null) {
                resena.setPelicula(em.getReference(Pelicula.class, resena.getPelicula().getId()));
            }

            em.persist(resena);
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

    /**
     * Actualiza una reseña existente en la base de datos. Utiliza merge para
     * guardar los cambios del objeto.
     *
     * @param resena El objeto Resena con los datos actualizados.
     */
    public void actualizarResena(Resena resena) {
        EntityManager em = ConexionDB.getInstancia().getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(resena);
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

    /**
     * Busca una reseña específica por el ID del usuario y el ID de la película.
     *
     * @param idUsuario ID del usuario que hizo la reseña.
     * @param idPelicula ID de la película reseñada.
     * @return El objeto Resena encontrado o null si no existe.
     */
    public Resena buscarResenaUsuarioPelicula(int idUsuario, int idPelicula) {
        EntityManager em = ConexionDB.getInstancia().getEntityManager();
        try {
            TypedQuery<Resena> query = em.createQuery(
                    "SELECT r FROM Resena r WHERE r.usuario.id = :idUsuario AND r.pelicula.id = :idPelicula",
                    Resena.class);
            query.setParameter("idUsuario", idUsuario);
            query.setParameter("idPelicula", idPelicula);

            List<Resena> resultados = query.getResultList();
            return resultados.isEmpty() ? null : resultados.get(0);
        } finally {
            em.close();
        }
    }

    public List<Resena> buscarPorPelicula(int idPelicula) {
        EntityManager em = ConexionDB.getInstancia().getEntityManager();
        try {
            TypedQuery<Resena> query = em.createQuery(
                    "SELECT r FROM Resena r JOIN FETCH r.usuario WHERE r.pelicula.id = :idPelicula",
                    Resena.class
            );
            query.setParameter("idPelicula", idPelicula);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al buscar reseñas por película: " + e.getMessage());
            return new java.util.ArrayList<>();
        } finally {
            em.close();
        }
    }

    public List<Resena> buscarPorUsuario(int idUsuario) {
        EntityManager em = ConexionDB.getInstancia().getEntityManager();
        try {
            TypedQuery<Resena> query = em.createQuery(
                    "SELECT r FROM Resena r WHERE r.usuario.id = :idUsuario",
                    Resena.class);
            query.setParameter("idUsuario", idUsuario);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al buscar reseñas por usuario: " + e.getMessage());
            return new java.util.ArrayList<>();
        } finally {
            em.close();
        }
    }
}
