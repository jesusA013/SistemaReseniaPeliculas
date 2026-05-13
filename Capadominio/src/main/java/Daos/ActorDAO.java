/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Daos;

import ConexionDB.ConexionDB;
import Entidades.Actor;
import javax.persistence.EntityManager;

/**
 *
 * @author golea
 */
public class ActorDAO {

    public void guardar(Actor actor) {
        EntityManager em = ConexionDB.getInstancia().getEntityManager();
        try {
            // Verificamos si ya existe por tmdbId antes de intentar guardar
            Actor existente = buscarPorTmdbId(actor.getTmdbId());

            em.getTransaction().begin();
            if (existente != null) {
                // Si existe, actualizamos el ID de la entidad para que merge reconozca que es el mismo
                actor.setId(existente.getId());
                em.merge(actor);
            } else {
                // Si es nuevo, lo persistimos
                em.persist(actor);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al guardar actor: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public java.util.List<Actor> listarUltimosActores(int limite) {
        javax.persistence.EntityManager em = ConexionDB.getInstancia().getEntityManager();
        try {
            return em.createQuery("SELECT a FROM Actor a ORDER BY a.id DESC", Actor.class)
                    .setMaxResults(limite)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Actor buscarPorTmdbId(Integer tmdbId) {
        EntityManager em = ConexionDB.getInstancia().getEntityManager();
        try {
            return em.createQuery("SELECT a FROM Actor a WHERE a.tmdbId = :tmdbId", Actor.class)
                    .setParameter("tmdbId", tmdbId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

}
