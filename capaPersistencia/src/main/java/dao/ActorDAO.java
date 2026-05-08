/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import ConexionBD.ConexionDB;
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
            em.getTransaction().begin();
            em.merge(actor); 
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
}
