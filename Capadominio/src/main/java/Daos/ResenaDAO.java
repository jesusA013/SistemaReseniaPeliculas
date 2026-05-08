/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Daos;

import Entidades.Resena;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author golea
 */
public class ResenaDAO {
    private EntityManager em;

    public void insertarReseña(Resena resena) {
        em.getTransaction().begin();
        em.persist(resena);
        em.getTransaction().commit();
    }

    public List<Resena> buscarReseñasPorPelicula(Long idPelicula) {
        return em.createQuery("SELECT r FROM Reseña r WHERE r.pelicula.id = :id", Resena.class)
                 .setParameter("id", idPelicula)
                 .getResultList();
    }
}