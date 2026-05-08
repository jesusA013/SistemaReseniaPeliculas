/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Daos;

import ConexionDB.ConexionDB;
import Entidades.Playlist;
import java.util.List;
import javax.persistence.EntityManager;

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
}
