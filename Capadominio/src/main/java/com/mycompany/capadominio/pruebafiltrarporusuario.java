/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.capadominio;

import Daos.ResenaDAO;
import Entidades.Resena;
import java.util.List;

/**
 *
 * @author golea
 */
public class pruebafiltrarporusuario {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here// 1. Instanciamos el DAO de reseñas
        ResenaDAO resenaDAO = new ResenaDAO();

        // 2. Definimos el ID del usuario que queremos consultar
        // Cambia este valor por uno que exista en tu base de datos
        int idUsuarioABuscar = 1; 

        System.out.println("--- Buscando reseñas del usuario con ID: " + idUsuarioABuscar + " ---");

        // 3. Llamamos al nuevo método creado
        List<Resena> listaResenas = resenaDAO.buscarPorUsuario(idUsuarioABuscar);

        // 4. Verificamos y mostramos los resultados
        if (listaResenas != null && !listaResenas.isEmpty()) {
            System.out.println("Se encontraron " + listaResenas.size() + " reseñas:");
            
            for (Resena r : listaResenas) {
                System.out.println("------------------------------------------");
                System.out.println("Película: " + r.getPelicula().getTitulo());
                System.out.println("Calificación: " + r.getCalificacion() + "/5");
                System.out.println("Comentario: " + r.getComentario());
                System.out.println("Fecha: " + r.getFecha());
            }
        } else {
            System.out.println("El usuario no tiene reseñas registradas o no existe.");
        }
        
        // El cierre del EntityManager se maneja internamente en cada método del DAO
    
    }
    
    
}
