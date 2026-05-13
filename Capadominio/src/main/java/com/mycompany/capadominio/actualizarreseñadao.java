/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.capadominio;

import Daos.PeliculaDAO;
import Daos.ResenaDAO;
import Daos.UsuarioDAO;
import Entidades.Pelicula;
import Entidades.Resena;
import Entidades.Usuario;
import java.util.Date;

/**
 *
 * @author golea
 */
public class actualizarreseñadao {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // 1. Inicializar DAOs
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        PeliculaDAO peliculaDAO = new PeliculaDAO();
        ResenaDAO resenaDAO = new ResenaDAO();

        try {
            System.out.println("--- INICIO DE PRUEBA ---");

            // 2. Obtener o crear Usuario de prueba
            String email = "golea@test.com";
            Usuario usuario = usuarioDAO.buscarPorEmail(email);
            if (usuario == null) {
                usuario = new Usuario();
                usuario.setNombre("Golea Test");
                usuario.setEmail(email);
                usuario.setPassword("password123");
                usuario.setDate(new Date());
                usuarioDAO.guardarUsuario(usuario);
                System.out.println("[OK] Usuario creado.");
            } else {
                System.out.println("[OK] Usuario encontrado: " + usuario.getNombre());
            }

            // 3. Obtener o crear Película de prueba
            String titulo = "Inception";
            Pelicula pelicula = peliculaDAO.buscarPorTitulo(titulo);
            if (pelicula == null) {
                pelicula = new Pelicula();
                pelicula.setTitulo(titulo);
                pelicula.setTmdbId(27205);
                pelicula.setSinopsis("Un ladrón que roba secretos a través de los sueños.");
                peliculaDAO.crearPelicula(pelicula);
                System.out.println("[OK] Película creada.");
            } else {
                System.out.println("[OK] Película encontrada: " + pelicula.getTitulo());
            }

            // 4. Lógica de Reseña: Buscar si ya existe para decidir si insertar o actualizar
            Resena resenaExistente = resenaDAO.buscarResenaUsuarioPelicula(usuario.getId(), pelicula.getId());

            if (resenaExistente == null) {
                // ESCENARIO A: INSERTAR
                System.out.println("No hay reseña previa. Creando una nueva...");
                Resena nuevaResena = new Resena();
                nuevaResena.setUsuario(usuario);
                nuevaResena.setPelicula(pelicula);
                nuevaResena.setCalificacion(4);
                nuevaResena.setComentario("Muy buena película, me gustó el concepto de los sueños.");
                nuevaResena.setFecha(new Date());

                resenaDAO.insertarResena(nuevaResena);
                System.out.println("[ÉXITO] Reseña insertada por primera vez.");
            } else {
                // ESCENARIO B: ACTUALIZAR
                System.out.println("Reseña previa encontrada. Actualizando...");
                resenaExistente.setComentario("ACTUALIZACIÓN: La volví a ver y le subo la nota, es una obra maestra.");
                resenaExistente.setCalificacion(5);
                resenaExistente.setFecha(new Date());

                resenaDAO.actualizarResena(resenaExistente);
                System.out.println("[ÉXITO] Reseña actualizada correctamente.");
                System.out.println("Nuevo Comentario: " + resenaExistente.getComentario());
            }

            System.out.println("--- FIN DE PRUEBA ---");

        } catch (Exception e) {
            System.err.println("Ocurrió un error durante la ejecución:");
            e.printStackTrace();
        }
    }
}
